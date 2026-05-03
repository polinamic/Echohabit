package com.echohabit.app.domain.repository

import com.echohabit.app.domain.model.BehavioralEvent
import com.echohabit.app.domain.model.BehaviorSummary
import kotlinx.coroutines.flow.Flow

interface BehaviorRepository {
    suspend fun recordEvent(event: BehavioralEvent)
    fun getEventsByDate(date: String): Flow<List<BehavioralEvent>>
    fun getWeeklyEvents(startDate: String, endDate: String): Flow<List<BehavioralEvent>>
    suspend fun deleteOldEvents(olderThanDate: String)
    
    suspend fun saveSummary(summary: BehaviorSummary)
    fun getSummaryByDate(date: String): Flow<BehaviorSummary?>
    fun getWeeklySummaries(startDate: String, endDate: String): Flow<List<BehaviorSummary>>
}
