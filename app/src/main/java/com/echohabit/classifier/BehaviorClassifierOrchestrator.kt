package com.echohabit.classifier

import com.echohabit.classifier.ml.TFLiteBehaviorClassifier
import com.echohabit.classifier.model.BehaviorLabel
import com.echohabit.classifier.model.BehaviorSnapshot
import com.echohabit.classifier.model.ClassificationResult
import com.echohabit.classifier.model.ClassificationSource
import com.echohabit.classifier.rules.BehaviorRuleEngine
import com.echohabit.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Entry point that combines rule-based and ML behavior classification.
 */
@Singleton
class BehaviorClassifierOrchestrator @Inject constructor(
    private val ruleEngine: BehaviorRuleEngine,
    private val tfliteClassifier: TFLiteBehaviorClassifier,
    @ApplicationScope private val scope: CoroutineScope
) {
    private val mutableClassificationFlow = MutableSharedFlow<ClassificationResult>(
        replay = 1,
        extraBufferCapacity = 10
    )

    /**
     * Stream of recent classification results.
     */
    val classificationFlow: SharedFlow<ClassificationResult> = mutableClassificationFlow.asSharedFlow()

    /**
     * Classifies one behavior snapshot using rules first, then ML if needed.
     */
    suspend fun classify(snapshot: BehaviorSnapshot): Result<ClassificationResult> {
        return try {
            val ruleResult = withContext(Dispatchers.Default) {
                ruleEngine.evaluate(snapshot)
            }

            val finalResult = if (ruleResult.label != BehaviorLabel.UNCERTAIN) {
                ruleResult
            } else {
                val mlResult = tfliteClassifier.classify(snapshot)
                if (mlResult.label == BehaviorLabel.UNCERTAIN) fallbackClassify(ruleResult) else mlResult
            }

            emitResult(finalResult)
            Result.Success(finalResult)
        } catch (exception: Throwable) {
            Result.Error(exception)
        }
    }

    /**
     * Classifies snapshots with limited parallelism.
     */
    suspend fun classifyBatch(snapshots: List<BehaviorSnapshot>): List<Result<ClassificationResult>> = coroutineScope {
        val semaphore = Semaphore(4)
        snapshots.map { snapshot ->
            async {
                semaphore.withPermit {
                    classify(snapshot)
                }
            }
        }.awaitAll()
    }

    private fun fallbackClassify(ruleResult: ClassificationResult): ClassificationResult {
        return ruleResult.copy(
            label = BehaviorLabel.BOREDOM_SCROLLING,
            confidence = 0.5f,
            source = ClassificationSource.RULE_ENGINE
        )
    }

    private fun emitResult(result: ClassificationResult) {
        scope.launch {
            mutableClassificationFlow.emit(result)
        }
    }

    /**
     * Result wrapper for classifier operations.
     */
    sealed class Result<out T> {
        data class Success<T>(val data: T) : Result<T>()
        data class Error(val exception: Throwable) : Result<Nothing>()
        object Loading : Result<Nothing>()

        /**
         * Returns contained data for success results.
         */
        fun getOrNull(): T? = when (this) {
            is Success -> data
            is Error -> null
            Loading -> null
        }
    }
}
