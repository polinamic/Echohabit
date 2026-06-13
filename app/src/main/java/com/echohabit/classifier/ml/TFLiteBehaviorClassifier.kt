package com.echohabit.classifier.ml

import android.content.Context
import androidx.annotation.Keep
import com.echohabit.classifier.model.BehaviorLabel
import com.echohabit.classifier.model.BehaviorSnapshot
import com.echohabit.classifier.model.ClassificationResult
import com.echohabit.classifier.model.ClassificationSource
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter

/**
 * TensorFlow Lite backed behavior classifier.
 */
@Keep
class TFLiteBehaviorClassifier @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var interpreter: Interpreter? = null

    /**
     * Returns true when the TFLite model can be loaded from app assets.
     */
    val isModelLoaded: Boolean
        get() = getInterpreterOrNull() != null

    /**
     * Runs model inference for a behavior snapshot.
     */
    suspend fun classify(snapshot: BehaviorSnapshot): ClassificationResult = withContext(Dispatchers.IO) {
        val loadedInterpreter = getInterpreterOrNull()
            ?: return@withContext uncertainResult(emptyList())

        try {
            val input = arrayOf(normalize(snapshot))
            val output = Array(1) { FloatArray(EXPECTED_OUTPUT_SIZE) }
            loadedInterpreter.run(input, output)

            val scores = output.first()
            val bestIndex = scores.indices.maxByOrNull { scores[it] } ?: BehaviorLabel.UNCERTAIN.ordinal
            ClassificationResult(
                label = BehaviorLabel.fromIndex(bestIndex),
                confidence = scores[bestIndex].coerceIn(0f, 1f),
                source = ClassificationSource.ML_MODEL,
                triggeredRules = listOf(TRIGGER_TFLITE_INFERENCE)
            )
        } catch (_: RuntimeException) {
            uncertainResult(emptyList())
        } catch (_: IllegalArgumentException) {
            uncertainResult(emptyList())
        }
    }

    /**
     * Releases interpreter resources.
     */
    fun close() {
        interpreter?.close()
        interpreter = null
    }

    private fun getInterpreterOrNull(): Interpreter? {
        interpreter?.let { return it }
        return try {
            Interpreter(loadModelFile(), Interpreter.Options().setNumThreads(2)).also {
                interpreter = it
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun loadModelFile(): MappedByteBuffer {
        context.assets.openFd(MODEL_FILE).use { descriptor ->
            descriptor.createInputStream().channel.use { channel ->
                return channel.map(
                    FileChannel.MapMode.READ_ONLY,
                    descriptor.startOffset,
                    descriptor.declaredLength
                )
            }
        }
    }

    private fun normalize(snapshot: BehaviorSnapshot): FloatArray {
        return floatArrayOf(
            snapshot.scrollVelocityPxPerSec / 3_000f,
            snapshot.avgDwellTimeMs / 30_000f,
            snapshot.sessionDurationMs.toFloat() / 3_600_000f,
            snapshot.appSwitchFreqPer10Min / 20f,
            snapshot.reopenFrequencyPer5Min / 10f,
            snapshot.passiveScrollRatio,
            if (snapshot.midnightUsage) 1f else 0f,
            snapshot.appCategory.riskWeight
        ).map { it.coerceIn(0f, 1f) }.toFloatArray()
    }

    private fun uncertainResult(triggeredRules: List<String>): ClassificationResult {
        return ClassificationResult(
            label = BehaviorLabel.UNCERTAIN,
            confidence = 0f,
            source = ClassificationSource.ML_MODEL,
            triggeredRules = triggeredRules
        )
    }

    companion object {
        private const val MODEL_FILE = "behavior_classifier.tflite"
        private const val EXPECTED_INPUT_SIZE = 8
        private const val EXPECTED_OUTPUT_SIZE = 4
        private const val TRIGGER_TFLITE_INFERENCE = "tflite_inference"

        /**
         * Returns the expected model input feature count.
         */
        fun getExpectedInputSize(): Int = EXPECTED_INPUT_SIZE

        /**
         * Returns the expected model output class count.
         */
        fun getExpectedOutputSize(): Int = EXPECTED_OUTPUT_SIZE
    }
}
