package com.echohabit.app.service

import com.echohabit.app.domain.model.BehavioralEvent
import com.echohabit.app.domain.model.BehaviorClassification
import org.tensorflow.lite.Interpreter
import javax.inject.Inject

class BehaviorAnalyzer @Inject constructor() {
    
    private var tfliteInterpreter: Interpreter? = null
    
    fun classifyBehavior(events: List<BehavioralEvent>): BehaviorClassification {
        return if (tfliteInterpreter != null) {
            classifyWithTFLite(events)
        } else {
            classifyWithRules(events)
        }
    }
    
    fun computeAIS(events: List<BehavioralEvent>): Float {
        if (events.isEmpty()) return 0.5f
        
        val scrollVelocities = events.map { it.scrollVelocity }
        val avgScrollVelocity = scrollVelocities.average().toFloat()
        
        return when {
            avgScrollVelocity < 100f -> 0.8f
            avgScrollVelocity in 100f..500f -> 0.6f
            else -> 0.4f
        }
    }
    
    fun computeQualityIndex(totalScrollEvents: Int, appSwitches: Int): Float {
        val ratio = if (appSwitches > 0) totalScrollEvents.toFloat() / appSwitches else 1f
        
        return when {
            ratio > 3f -> 0.8f
            ratio in 1f..3f -> 0.6f
            else -> 0.4f
        }
    }
    
    private fun classifyWithTFLite(events: List<BehavioralEvent>): BehaviorClassification {
        return BehaviorClassification(
            classification = "TFlite_CLASSIFIED",
            confidence = 0.9f,
            category = "ML_BASED"
        )
    }
    
    private fun classifyWithRules(events: List<BehavioralEvent>): BehaviorClassification {
        val scrollCount = events.count { it.scrollVelocity > 0 }
        val appSwitchCount = events.size - scrollCount
        
        val classification = when {
            scrollCount > appSwitchCount * 2 -> "HIGH_ENGAGEMENT"
            appSwitchCount > scrollCount -> "APP_SWITCHING"
            else -> "NORMAL_USAGE"
        }
        
        return BehaviorClassification(
            classification = classification,
            confidence = 0.7f,
            category = "RULE_BASED"
        )
    }
}
