package com.echohabit.app.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.echohabit.app.data.model.BehaviorSummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BehaviorSummaryDao {
    
    @Insert
    suspend fun insertSummary(summary: BehaviorSummaryEntity)
    
    @Query("SELECT * FROM behavior_summaries WHERE date = :date")
    fun getSummaryByDate(date: String): Flow<BehaviorSummaryEntity?>
    
    @Query("SELECT * FROM behavior_summaries WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getWeeklySummaries(startDate: String, endDate: String): Flow<List<BehaviorSummaryEntity>>
}
