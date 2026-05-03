package com.echohabit.app.domain.usecase.behavior

import com.echohabit.app.domain.model.BehavioralEvent
import com.echohabit.app.domain.model.BehaviorClassification
import javax.inject.Inject

class ClassifyBehaviorUseCase @Inject constructor() {
    
    fun execute(events: List<BehavioralEvent>): BehaviorClassification {
        if (events.isEmpty()) {
            return BehaviorClassification(
                classification = "UNKNOWN",
                confidence = 0f,
                category = "N/A"
            )
        }
        
        val scrollEventCount = events.count { it.scrollVelocity > 0 }
        val appSwitchCount = events.size - scrollEventCount
        val avgDwellTime = if (events.isNotEmpty()) {
            events.map { it.dwellTimeMs }.average()
        } else {
            0.0
        }
        
        val classification = when {
            scrollEventCount > appSwitchCount * 2 -> "HIGH_ENGAGEMENT"
            appSwitchCount > scrollEventCount -> "APP_SWITCHING"
            avgDwellTime > 5000 -> "FOCUSED_SESSION"
            else -> "NORMAL_USAGE"
        }
        
        val confidence = when (classification) {
            "HIGH_ENGAGEMENT" -> 0.9f
            "APP_SWITCHING" -> 0.85f
            "FOCUSED_SESSION" -> 0.8f
            else -> 0.7f
        }
        
        return BehaviorClassification(
            classification = classification,
            confidence = confidence,
            category = "USER_BEHAVIOR"
        )
    }
}
