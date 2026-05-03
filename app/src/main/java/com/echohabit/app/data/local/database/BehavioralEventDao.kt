package com.echohabit.app.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.echohabit.app.data.model.BehavioralEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BehavioralEventDao {
    
    @Insert
    suspend fun insertEvent(event: BehavioralEventEntity)
    
    @Query("SELECT * FROM behavioral_events WHERE date = :date ORDER BY timestamp DESC")
    fun getEventsByDate(date: String): Flow<List<BehavioralEventEntity>>
    
    @Query("SELECT * FROM behavioral_events WHERE date BETWEEN :startDate AND :endDate ORDER BY timestamp DESC")
    fun getWeeklyEvents(startDate: String, endDate: String): Flow<List<BehavioralEventEntity>>
    
    @Query("DELETE FROM behavioral_events WHERE date < :olderThanDate")
    suspend fun deleteOldEvents(olderThanDate: String)
    
    @Query("SELECT COUNT(*) FROM behavioral_events WHERE date = :date")
    fun getEventCountByDate(date: String): Flow<Int>
}
