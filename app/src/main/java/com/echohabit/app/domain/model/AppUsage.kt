package com.echohabit.app.domain.model

/**
 * Represents app usage data for a specific application.
 *
 * @param appName The display name of the application
 * @param packageName The package name of the application
 * @param durationMs Total time spent in the app (milliseconds)
 * @param sessionCount Number of sessions
 */
data class AppUsage(
    val appName: String,
    val packageName: String,
    val durationMs: Long = 0L,
    val sessionCount: Int = 0
) {
    /**
     * Calculate percentage of total screen time for this app.
     * @param totalDurationMs Total screen time across all apps
     * @return Percentage as a Float (0.0 - 1.0)
     */
    fun getPercentage(totalDurationMs: Long): Float {
        return if (totalDurationMs > 0) {
            (durationMs.toFloat() / totalDurationMs).coerceIn(0f, 1f)
        } else {
            0f
        }
    }

    /**
     * Format duration to human-readable format (e.g., "2h 57m")
     */
    fun getFormattedDuration(): String {
        val hours = durationMs / (1000 * 60 * 60)
        val minutes = (durationMs % (1000 * 60 * 60)) / (1000 * 60)
        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            minutes > 0 -> "${minutes}m"
            else -> "0m"
        }
    }
}
