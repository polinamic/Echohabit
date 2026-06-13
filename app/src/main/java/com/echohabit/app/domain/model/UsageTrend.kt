package com.echohabit.app.domain.model

/**
 * Represents usage data for a specific time period (day, week, etc).
 *
 * @param label The label for this data point (e.g., "Mon", "May 03")
 * @param durationMs Total screen time for this period
 */
data class UsageTrend(
    val label: String,
    val durationMs: Long = 0L
) {
    /**
     * Format duration to human-readable format
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

    /**
     * Get percentage for chart visualization
     * @param maxDuration The maximum duration to use as reference
     * @return Percentage as a Float (0.0 - 1.0)
     */
    fun getPercentage(maxDuration: Long): Float {
        return if (maxDuration > 0) {
            (durationMs.toFloat() / maxDuration).coerceIn(0f, 1f)
        } else {
            0f
        }
    }
}

/**
 * Enum for time range selection
 */
enum class TimeRange {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}
