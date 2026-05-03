package com.echohabit.app.core.utils

object Constants {
    // Database
    const val DATABASE_NAME = "echohabit_db"
    
    // Accessibility Service
    const val ACCESSIBILITY_SERVICE_ID = "com.echohabit.app/.service.AccessibilityTracker"
    const val SCROLL_VELOCITY_THRESHOLD = 100f
    
    // Notifications
    const val NUDGE_NOTIFICATION_CHANNEL_ID = "echohabit_nudges"
    const val NUDGE_NOTIFICATION_ID = 1001
    
    // Quality Index Thresholds
    const val LOW_QUALITY_THRESHOLD = 0.4f
    const val HIGH_QUALITY_THRESHOLD = 0.7f
    
    // Background Tasks
    const val DAILY_SUMMARY_TASK_ID = "echohabit_daily_summary"
    const val CLEANUP_TASK_ID = "echohabit_cleanup"
    
    // Retention Policy
    const val RETENTION_DAYS = 30
}
