package com.echohabit.app.domain.usecase.notification

import com.echohabit.app.domain.model.BehaviorSummary
import javax.inject.Inject

class TriggerNudgeUseCase @Inject constructor() {
    
    fun execute(summary: BehaviorSummary): Boolean {
        val shouldTrigger = summary.digitalBehaviorQualityIndex < 0.4f
        if (shouldTrigger) {
            val nudgeMessage = generateNudgeMessage(summary)
            triggerNotification(nudgeMessage)
        }
        return shouldTrigger
    }
    
    private fun generateNudgeMessage(summary: BehaviorSummary): String {
        return when {
            summary.appSwitchCount > 50 -> "Try focusing on one app at a time"
            summary.totalScrollEvents > 100 -> "You've scrolled a lot today. Take a break!"
            summary.avgDwellTimeMs < 1000 -> "Your sessions are quite short. Consider taking longer breaks"
            else -> "Keep up the good digital wellness habits!"
        }
    }
    
    private fun triggerNotification(message: String) {
        // Notification logic will be integrated with InterventionService
    }
}
