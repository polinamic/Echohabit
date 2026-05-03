package com.echohabit.app.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Build
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class InterventionService : Service(), CoroutineScope {
    
    override val coroutineContext = Dispatchers.Main + SupervisorJob()
    
    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "echohabit_nudges"
    }
    
    override fun onCreate() {
        super.onCreate()
        startForegroundService()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        launch {
            // Process intervention logic
        }
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    
    private fun startForegroundService() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("EchoHabit")
            .setContentText("Monitoring your digital wellness")
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .build()
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }
    
    fun triggerNudge(message: String) {
        launch {
            // Send nudge notification
        }
    }
}
