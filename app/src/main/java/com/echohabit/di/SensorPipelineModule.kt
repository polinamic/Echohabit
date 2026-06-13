package com.echohabit.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// Normally we would define @Provides here if we needed complex instantiations,
// but since AppCategoryMapper, UsageStatsMonitor, and SensorEventAggregator
// all use @Inject constructor(), Hilt knows how to create them automatically.
// We define this module as a placeholder for any future interface bindings.

@Module
@InstallIn(SingletonComponent::class)
object SensorPipelineModule {
    // Add @Provides or @Binds methods here if future dependencies require it.
}
