package com.echohabit.sensor

import com.echohabit.classifier.model.AppCategory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppCategoryMapper @Inject constructor() {

    // Simple hardcoded mapping for the current phase.
    // In the future, this could be backed by a Room database or remote config.
    private val categoryMap = mapOf(
        "com.whatsapp" to AppCategory.ESSENTIAL,
        "com.google.android.apps.messaging" to AppCategory.ESSENTIAL,
        "com.instagram.android" to AppCategory.HIGH_RISK,
        "com.zhiliaoapp.musically" to AppCategory.HIGH_RISK, // TikTok
        "com.twitter.android" to AppCategory.HIGH_RISK,
        "com.google.android.youtube" to AppCategory.HIGH_RISK,
        "com.google.android.apps.docs" to AppCategory.PRODUCTIVE,
        "com.microsoft.office.word" to AppCategory.PRODUCTIVE,
        "com.anydo" to AppCategory.PRODUCTIVE
    )

    fun getCategoryForPackage(packageName: String): AppCategory {
        return categoryMap[packageName] ?: AppCategory.NEUTRAL
    }
}
