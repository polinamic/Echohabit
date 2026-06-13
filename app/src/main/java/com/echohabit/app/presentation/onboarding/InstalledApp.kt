package com.echohabit.app.presentation.onboarding

import android.graphics.drawable.Drawable

/**
 * Simple installed app row model used by onboarding and settings exclusion lists.
 */
data class InstalledApp(
    val packageName: String,
    val appName: String,
    val icon: Drawable?
)
