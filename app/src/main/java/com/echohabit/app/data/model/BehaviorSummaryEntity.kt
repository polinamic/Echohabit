package com.echohabit.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "behavior_summaries")
data class BehaviorSummaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val date: String,
    val totalScrollEvents: Int,
    val avgDwellTimeMs: Long,
    val appSwitchCount: Int,
    val sessionCount: Int,
    val digitalBehaviorQualityIndex: Float,
    val authenticityImpactScore: Float
)
