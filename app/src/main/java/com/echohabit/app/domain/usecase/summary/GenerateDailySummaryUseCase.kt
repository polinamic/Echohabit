package com.echohabit.app.domain.usecase.summary

import com.echohabit.app.domain.model.BehaviorSummary
import com.echohabit.app.domain.repository.BehaviorRepository
import javax.inject.Inject

class GenerateDailySummaryUseCase @Inject constructor(
    private val behaviorRepository: BehaviorRepository
) {
    
    suspend fun execute(date: String): BehaviorSummary {
        val summary = BehaviorSummary(date = date)
        behaviorRepository.saveSummary(summary)
        return summary
    }
}
