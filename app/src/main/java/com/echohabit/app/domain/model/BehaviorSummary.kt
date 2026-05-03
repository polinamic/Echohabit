package com.echohabit.app.domain.model

data class BehaviorSummary(
    val date: String,
    val totalScrollEvents: Int = 0,
    val avgDwellTimeMs: Long = 0L,
    val appSwitchCount: Int = 0,
    val sessionCount: Int = 0,
    val digitalBehaviorQualityIndex: Float = 0.5f,
    val authenticityImpactScore: Float = 0.5f
)
