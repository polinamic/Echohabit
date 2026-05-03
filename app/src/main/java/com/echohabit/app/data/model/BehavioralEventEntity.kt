package com.echohabit.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "behavioral_events")
data class BehavioralEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val appPackageName: String,
    val scrollVelocity: Float,
    val dwellTimeMs: Long,
    val sessionId: String,
    val timestamp: Long,
    val eventType: String,
    val date: String
)
