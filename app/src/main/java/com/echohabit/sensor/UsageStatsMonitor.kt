package com.echohabit.sensor

import android.app.usage.UsageStatsManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsageStatsMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val usageStatsManager by lazy {
        context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }

    /**
     * Retrieves the total time in foreground for the specified package today.
     * Returns the duration in milliseconds.
     */
    fun getCurrentSessionDuration(packageName: String): Long {
        val endTime = System.currentTimeMillis()
        // Check stats for the last 24 hours to cover the "today" session
        val startTime = endTime - (1000 * 60 * 60 * 24)
        
        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startTime,
            endTime
        )
        
        return stats?.find { it.packageName == packageName }?.totalTimeInForeground ?: 0L
    }
}
