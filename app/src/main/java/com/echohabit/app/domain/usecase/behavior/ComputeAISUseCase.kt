package com.echohabit.app.domain.usecase.behavior

import com.echohabit.app.domain.model.BehavioralEvent
import javax.inject.Inject

class ComputeAISUseCase @Inject constructor() {
    
    fun execute(events: List<BehavioralEvent>): Float {
        if (events.isEmpty()) return 0.5f
        
        val scrollVelocities = events.map { it.scrollVelocity }
        val dwellTimes = events.map { it.dwellTimeMs }
        
        val avgScrollVelocity = scrollVelocities.average().toFloat()
        val avgDwellTime = dwellTimes.average()
        
        val naturalityScore = computeNaturality(avgScrollVelocity, avgDwellTime)
        val consistencyScore = computeConsistency(events)
        
        return (naturalityScore + consistencyScore) / 2f
    }
    
    private fun computeNaturality(avgScrollVelocity: Float, avgDwellTime: Double): Float {
        return when {
            avgScrollVelocity < 100f && avgDwellTime > 3000 -> 0.8f
            avgScrollVelocity in 100f..500f && avgDwellTime in 1000.0..5000.0 -> 0.7f
            else -> 0.5f
        }
    }
    
    private fun computeConsistency(events: List<BehavioralEvent>): Float {
        return if (events.size > 10) 0.7f else 0.5f
    }
}
