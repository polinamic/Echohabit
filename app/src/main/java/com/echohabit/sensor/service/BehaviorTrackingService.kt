package com.echohabit.sensor.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.echohabit.sensor.SensorEventAggregator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BehaviorTrackingService : AccessibilityService() {

    @Inject
    lateinit var aggregator: SensorEventAggregator

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var lastScrollY = 0

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        serviceScope.launch {
            when (event.eventType) {
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                    event.packageName?.toString()?.let { packageName ->
                        aggregator.onAppSwitched(packageName)
                    }
                }
                AccessibilityEvent.TYPE_VIEW_SCROLLED -> {
                    // Approximate delta Y from the previously observed scroll position.
                    val deltaY = (event.scrollY - lastScrollY).toFloat()
                    lastScrollY = event.scrollY
                    if (deltaY != 0f) {
                        aggregator.onScrolled(deltaY)
                    }
                }
            }
        }
    }

    override fun onInterrupt() {
        // Handle service interruption
    }
}
