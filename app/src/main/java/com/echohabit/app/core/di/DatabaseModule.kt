package com.echohabit.app.core.di

import android.content.Context
import androidx.room.Room
import com.echohabit.app.data.local.database.AppDatabase
import com.echohabit.app.data.local.database.BehavioralEventDao
import com.echohabit.app.data.local.database.BehaviorSummaryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "echohabit_db"
        ).build()
    }
    
    @Singleton
    @Provides
    fun provideBehavioralEventDao(database: AppDatabase): BehavioralEventDao {
        return database.behavioralEventDao()
    }
    
    @Singleton
    @Provides
    fun provideBehaviorSummaryDao(database: AppDatabase): BehaviorSummaryDao {
        return database.behaviorSummaryDao()
    }
}
