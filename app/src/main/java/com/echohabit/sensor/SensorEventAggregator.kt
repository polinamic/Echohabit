package com.echohabit.sensor

import com.echohabit.classifier.BehaviorClassifierOrchestrator
import com.echohabit.classifier.model.BehaviorSnapshot
import com.echohabit.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SensorEventAggregator @Inject constructor(
    private val orchestrator: BehaviorClassifierOrchestrator,
    private val appCategoryMapper: AppCategoryMapper,
    private val usageStatsMonitor: UsageStatsMonitor,
    @ApplicationScope private val scope: CoroutineScope
) {
    private val mutex = Mutex()

    // Mutable state accumulators
    private var currentPackage: String = ""
    private var scrollDistancePx = 0f
    private var lastScrollTimeMs = 0L
    
    private var appSwitchesIn10Min = 0f
    private var reopenCountIn5Min = 0f
    
    // Simplistic tracking for dwell time (time spent without switching)
    private var itemDwellStartTimeMs = 0L
    private var totalDwellTimeMs = 0L
    private var dwellCount = 0

    init {
        startTicker()
    }

    private fun startTicker() {
        scope.launch {
            while (isActive) {
                delay(30_000L) // 30 seconds tick
                flushAndClassify()
            }
        }
    }

    suspend fun onAppSwitched(packageName: String) {
        mutex.withLock {
            if (currentPackage != packageName) {
                // Heuristics for switches & reopens can be refined later
                appSwitchesIn10Min += 1f
                
                if (currentPackage.isNotEmpty()) {
                    // Record dwell time for the previous item/app
                    val dwell = System.currentTimeMillis() - itemDwellStartTimeMs
                    totalDwellTimeMs += dwell
                    dwellCount++
                }
                
                currentPackage = packageName
                itemDwellStartTimeMs = System.currentTimeMillis()
                
                // Extremely basic heuristic for reopen, ideally track a queue of packages
                reopenCountIn5Min += 0.2f 
            }
        }
    }

    suspend fun onScrolled(deltaY: Float) {
        mutex.withLock {
            scrollDistancePx += Math.abs(deltaY)
            lastScrollTimeMs = System.currentTimeMillis()
            
            // Assume each significant scroll resets the dwell time for a specific "content item"
            val dwell = System.currentTimeMillis() - itemDwellStartTimeMs
            if (dwell > 500) { // minimum 500ms to count as a dwell
                totalDwellTimeMs += dwell
                dwellCount++
            }
            itemDwellStartTimeMs = System.currentTimeMillis()
        }
    }

    private suspend fun flushAndClassify() {
        val snapshot = mutex.withLock {
            if (currentPackage.isEmpty()) return@withLock null
            
            val velocity = if (scrollDistancePx > 0) scrollDistancePx / 30f else 0f
            val avgDwell = if (dwellCount > 0) (totalDwellTimeMs / dwellCount).toFloat() else 0f
            
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val isMidnight = hour in 0..4
            
            val snap = BehaviorSnapshot(
                scrollVelocityPxPerSec = velocity,
                avgDwellTimeMs = avgDwell,
                sessionDurationMs = usageStatsMonitor.getCurrentSessionDuration(currentPackage),
                appSwitchFreqPer10Min = appSwitchesIn10Min,
                reopenFrequencyPer5Min = reopenCountIn5Min,
                passiveScrollRatio = 0.5f, // Placeholder, needs view interaction tracking
                midnightUsage = isMidnight,
                appCategory = appCategoryMapper.getCategoryForPackage(currentPackage)
            )

            // Reset accumulators for next window
            scrollDistancePx = 0f
            totalDwellTimeMs = 0L
            dwellCount = 0
            
            snap
        }

        snapshot?.let {
            orchestrator.classify(it)
        }
    }
}
