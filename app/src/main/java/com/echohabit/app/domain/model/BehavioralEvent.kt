package com.echohabit.app.domain.model

enum class BehaviorEventType {
    SCROLL,
    APP_SWITCH,
    SESSION_START,
    SESSION_END
}

data class BehavioralEvent(
    val appPackageName: String,
    val scrollVelocity: Float = 0f,
    val dwellTimeMs: Long = 0L,
    val sessionId: String,
    val timestamp: Long,
    val eventType: BehaviorEventType
)
