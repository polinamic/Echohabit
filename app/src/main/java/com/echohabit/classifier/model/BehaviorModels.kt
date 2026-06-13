package com.echohabit.classifier.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * Labels produced by the behavior classifier.
 */
@Keep
enum class BehaviorLabel(val displayName: String) {
    INTENTIONAL_USE("Intentional Use"),
    BOREDOM_SCROLLING("Boredom Scrolling"),
    COMPULSIVE_LOOP("Compulsive Loop"),
    UNCERTAIN("Uncertain");

    companion object {
        /**
         * Maps a model output index to a behavior label.
         */
        fun fromIndex(index: Int): BehaviorLabel {
            return entries.getOrElse(index) { UNCERTAIN }
        }
    }
}

/**
 * Identifies the classifier layer that produced a result.
 */
@Keep
enum class ClassificationSource {
    RULE_ENGINE,
    ML_MODEL
}

/**
 * App category used to weight behavior risk.
 */
@Keep
enum class AppCategory(val riskWeight: Float) {
    PRODUCTIVE(0.1f),
    NEUTRAL(0.4f),
    HIGH_RISK(1.0f),
    ESSENTIAL(0.0f)
}

/**
 * Snapshot of behavior signals collected during a short monitoring window.
 */
@Keep
@Parcelize
data class BehaviorSnapshot(
    val scrollVelocityPxPerSec: Float,
    val avgDwellTimeMs: Float,
    val sessionDurationMs: Long,
    val appSwitchFreqPer10Min: Float,
    val reopenFrequencyPer5Min: Float,
    val passiveScrollRatio: Float,
    val midnightUsage: Boolean,
    val appCategory: AppCategory
) : Parcelable {
    companion object {
        /**
         * Creates a neutral empty snapshot for fallback and tests.
         */
        fun createEmpty(): BehaviorSnapshot {
            return BehaviorSnapshot(
                scrollVelocityPxPerSec = 0f,
                avgDwellTimeMs = 0f,
                sessionDurationMs = 0L,
                appSwitchFreqPer10Min = 0f,
                reopenFrequencyPer5Min = 0f,
                passiveScrollRatio = 0f,
                midnightUsage = false,
                appCategory = AppCategory.NEUTRAL
            )
        }
    }
}

/**
 * Final classification output with confidence and explainability metadata.
 */
@Keep
@Parcelize
data class ClassificationResult(
    val label: BehaviorLabel,
    val confidence: Float,
    val source: ClassificationSource,
    val triggeredRules: List<String>,
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable {
    /**
     * Returns true when the label is a compulsive loop.
     */
    fun isCompulsive(): Boolean = label == BehaviorLabel.COMPULSIVE_LOOP

    /**
     * Returns true when intervention confidence is high enough.
     */
    fun requiresIntervention(): Boolean = isCompulsive() && confidence >= 0.65f
}
