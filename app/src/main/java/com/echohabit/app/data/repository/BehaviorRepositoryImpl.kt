package com.echohabit.app.data.repository

import com.echohabit.app.data.local.database.BehavioralEventDao
import com.echohabit.app.data.local.database.BehaviorSummaryDao
import com.echohabit.app.data.model.BehavioralEventEntity
import com.echohabit.app.data.model.BehaviorSummaryEntity
import com.echohabit.app.domain.model.BehavioralEvent
import com.echohabit.app.domain.model.BehaviorEventType
import com.echohabit.app.domain.model.BehaviorSummary
import com.echohabit.app.domain.repository.BehaviorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class BehaviorRepositoryImpl @Inject constructor(
    private val eventDao: BehavioralEventDao,
    private val summaryDao: BehaviorSummaryDao
) : BehaviorRepository {
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    
    override suspend fun recordEvent(event: BehavioralEvent) {
        val entity = BehavioralEventEntity(
            appPackageName = event.appPackageName,
            scrollVelocity = event.scrollVelocity,
            dwellTimeMs = event.dwellTimeMs,
            sessionId = event.sessionId,
            timestamp = event.timestamp,
            eventType = event.eventType.name,
            date = dateFormat.format(event.timestamp)
        )
        eventDao.insertEvent(entity)
    }
    
    override fun getEventsByDate(date: String): Flow<List<BehavioralEvent>> {
        return eventDao.getEventsByDate(date).map { entities ->
            entities.map { entity ->
                BehavioralEvent(
                    appPackageName = entity.appPackageName,
                    scrollVelocity = entity.scrollVelocity,
                    dwellTimeMs = entity.dwellTimeMs,
                    sessionId = entity.sessionId,
                    timestamp = entity.timestamp,
                    eventType = BehaviorEventType.valueOf(entity.eventType)
                )
            }
        }
    }
    
    override fun getWeeklyEvents(startDate: String, endDate: String): Flow<List<BehavioralEvent>> {
        return eventDao.getWeeklyEvents(startDate, endDate).map { entities ->
            entities.map { entity ->
                BehavioralEvent(
                    appPackageName = entity.appPackageName,
                    scrollVelocity = entity.scrollVelocity,
                    dwellTimeMs = entity.dwellTimeMs,
                    sessionId = entity.sessionId,
                    timestamp = entity.timestamp,
                    eventType = BehaviorEventType.valueOf(entity.eventType)
                )
            }
        }
    }
    
    override suspend fun deleteOldEvents(olderThanDate: String) {
        eventDao.deleteOldEvents(olderThanDate)
    }
    
    override suspend fun saveSummary(summary: BehaviorSummary) {
        val entity = BehaviorSummaryEntity(
            date = summary.date,
            totalScrollEvents = summary.totalScrollEvents,
            avgDwellTimeMs = summary.avgDwellTimeMs,
            appSwitchCount = summary.appSwitchCount,
            sessionCount = summary.sessionCount,
            digitalBehaviorQualityIndex = summary.digitalBehaviorQualityIndex,
            authenticityImpactScore = summary.authenticityImpactScore
        )
        summaryDao.insertSummary(entity)
    }
    
    override fun getSummaryByDate(date: String): Flow<BehaviorSummary?> {
        return summaryDao.getSummaryByDate(date).map { entity ->
            entity?.let {
                BehaviorSummary(
                    date = it.date,
                    totalScrollEvents = it.totalScrollEvents,
                    avgDwellTimeMs = it.avgDwellTimeMs,
                    appSwitchCount = it.appSwitchCount,
                    sessionCount = it.sessionCount,
                    digitalBehaviorQualityIndex = it.digitalBehaviorQualityIndex,
                    authenticityImpactScore = it.authenticityImpactScore
                )
            }
        }
    }
    
    override fun getWeeklySummaries(startDate: String, endDate: String): Flow<List<BehaviorSummary>> {
        return summaryDao.getWeeklySummaries(startDate, endDate).map { entities ->
            entities.map { entity ->
                BehaviorSummary(
                    date = entity.date,
                    totalScrollEvents = entity.totalScrollEvents,
                    avgDwellTimeMs = entity.avgDwellTimeMs,
                    appSwitchCount = entity.appSwitchCount,
                    sessionCount = entity.sessionCount,
                    digitalBehaviorQualityIndex = entity.digitalBehaviorQualityIndex,
                    authenticityImpactScore = entity.authenticityImpactScore
                )
            }
        }
    }
}
