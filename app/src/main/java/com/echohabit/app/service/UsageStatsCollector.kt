package com.echohabit.app.service

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import java.util.Calendar
import javax.inject.Inject

class UsageStatsCollector @Inject constructor(
    private val context: Context
) {
    
    private val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    
    fun getDailyUsageStats(): Map<String, Long> {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val startTime = calendar.timeInMillis
        
        val usageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
        
        return usageStats.associate { it.packageName to it.totalTimeInForeground }
    }
    
    fun getWeeklyUsageStats(): Map<String, Long> {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        val startTime = calendar.timeInMillis
        
        val usageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
        
        return usageStats.associate { it.packageName to it.totalTimeInForeground }
    }
}
