package com.echohabit.classifier.rules

import com.echohabit.classifier.model.AppCategory
import com.echohabit.classifier.model.BehaviorLabel
import com.echohabit.classifier.model.BehaviorSnapshot
import com.echohabit.classifier.model.ClassificationResult
import com.echohabit.classifier.model.ClassificationSource
import javax.inject.Inject

/**
 * Stateless heuristic classifier for fast behavior decisions.
 */
class BehaviorRuleEngine @Inject constructor() {
    /**
     * Evaluates a snapshot and returns a rule-based classification result.
     */
    fun evaluate(snapshot: BehaviorSnapshot): ClassificationResult {
        val fired = listOf(
            checkEssentialApp(snapshot),
            checkHighVelocityLowDwell(snapshot),
            checkReopenLoop(snapshot),
            checkLongPassiveSession(snapshot),
            checkMidnightCompulsion(snapshot),
            checkBoredomPattern(snapshot),
            checkIntentionalUsage(snapshot)
        ).filter { it.fired }

        val essential = fired.firstOrNull { it.labelHint == BehaviorLabel.INTENTIONAL_USE && it.ruleName == RULE_ESSENTIAL_APP }
        if (essential != null) {
            return ClassificationResult(
                label = BehaviorLabel.INTENTIONAL_USE,
                confidence = 1.0f,
                source = ClassificationSource.RULE_ENGINE,
                triggeredRules = fired.map { it.ruleName }
            )
        }

        val grouped = fired.groupBy { it.labelHint }
            .mapValues { entry -> entry.value.sumOf { it.weight.toDouble() }.toFloat() }

        val best = grouped.maxByOrNull { it.value }
        val label = if (best != null && best.value >= CONFIDENT_THRESHOLD) best.key else BehaviorLabel.UNCERTAIN

        return ClassificationResult(
            label = label,
            confidence = (best?.value ?: 0f).coerceAtMost(1.0f),
            source = ClassificationSource.RULE_ENGINE,
            triggeredRules = fired.map { it.ruleName }
        )
    }

    private fun checkHighVelocityLowDwell(s: BehaviorSnapshot): RuleResult {
        return RuleResult(
            fired = s.scrollVelocityPxPerSec > 800f && s.avgDwellTimeMs < 2_000f,
            ruleName = RULE_HIGH_VELOCITY_LOW_DWELL,
            weight = 0.85f,
            labelHint = BehaviorLabel.COMPULSIVE_LOOP
        )
    }

    private fun checkReopenLoop(s: BehaviorSnapshot): RuleResult {
        return RuleResult(
            fired = s.reopenFrequencyPer5Min >= 3f && s.appCategory == AppCategory.HIGH_RISK,
            ruleName = RULE_REOPEN_LOOP,
            weight = 0.90f,
            labelHint = BehaviorLabel.COMPULSIVE_LOOP
        )
    }

    private fun checkLongPassiveSession(s: BehaviorSnapshot): RuleResult {
        return RuleResult(
            fired = s.sessionDurationMs > 900_000L && s.passiveScrollRatio > 0.75f,
            ruleName = RULE_LONG_PASSIVE_SESSION,
            weight = 0.80f,
            labelHint = BehaviorLabel.COMPULSIVE_LOOP
        )
    }

    private fun checkMidnightCompulsion(s: BehaviorSnapshot): RuleResult {
        return RuleResult(
            fired = s.midnightUsage && s.appCategory == AppCategory.HIGH_RISK,
            ruleName = RULE_MIDNIGHT_COMPULSION,
            weight = 0.70f,
            labelHint = BehaviorLabel.COMPULSIVE_LOOP
        )
    }

    private fun checkBoredomPattern(s: BehaviorSnapshot): RuleResult {
        return RuleResult(
            fired = s.appSwitchFreqPer10Min > 5f && s.avgDwellTimeMs < 5_000f && s.sessionDurationMs < 300_000L,
            ruleName = RULE_BOREDOM_PATTERN,
            weight = 0.75f,
            labelHint = BehaviorLabel.BOREDOM_SCROLLING
        )
    }

    private fun checkIntentionalUsage(s: BehaviorSnapshot): RuleResult {
        return RuleResult(
            fired = s.appCategory == AppCategory.PRODUCTIVE && s.avgDwellTimeMs > 10_000f && s.passiveScrollRatio < 0.3f,
            ruleName = RULE_INTENTIONAL_USAGE,
            weight = 0.90f,
            labelHint = BehaviorLabel.INTENTIONAL_USE
        )
    }

    private fun checkEssentialApp(s: BehaviorSnapshot): RuleResult {
        return RuleResult(
            fired = s.appCategory == AppCategory.ESSENTIAL,
            ruleName = RULE_ESSENTIAL_APP,
            weight = 1.0f,
            labelHint = BehaviorLabel.INTENTIONAL_USE
        )
    }

    private data class RuleResult(
        val fired: Boolean,
        val ruleName: String,
        val weight: Float,
        val labelHint: BehaviorLabel
    )

    private companion object {
        const val CONFIDENT_THRESHOLD = 0.75f
        const val RULE_HIGH_VELOCITY_LOW_DWELL = "high_velocity_low_dwell"
        const val RULE_REOPEN_LOOP = "reopen_loop"
        const val RULE_LONG_PASSIVE_SESSION = "long_passive_session"
        const val RULE_MIDNIGHT_COMPULSION = "midnight_compulsion"
        const val RULE_BOREDOM_PATTERN = "boredom_pattern"
        const val RULE_INTENTIONAL_USAGE = "intentional_usage"
        const val RULE_ESSENTIAL_APP = "essential_app"
    }
}
