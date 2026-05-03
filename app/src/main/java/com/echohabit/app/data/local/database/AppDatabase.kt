package com.echohabit.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.echohabit.app.data.model.BehavioralEventEntity
import com.echohabit.app.data.model.BehaviorSummaryEntity

@Database(
    entities = [BehavioralEventEntity::class, BehaviorSummaryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun behavioralEventDao(): BehavioralEventDao
    abstract fun behaviorSummaryDao(): BehaviorSummaryDao
}
