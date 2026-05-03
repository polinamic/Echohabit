package com.echohabit.app.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    
    fun getTodayDate(): String = dateFormat.format(Date())
    
    fun formatDate(timestamp: Long): String = dateFormat.format(timestamp)
    
    fun formatTime(timestamp: Long): String = timeFormat.format(timestamp)
    
    fun getDateSevenDaysAgo(): String {
        val calendar = java.util.Calendar.getInstance()
        calendar.add(java.util.Calendar.DAY_OF_YEAR, -7)
        return dateFormat.format(calendar.time)
    }
}
