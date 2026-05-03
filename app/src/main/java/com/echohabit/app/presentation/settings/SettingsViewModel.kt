package com.echohabit.app.presentation.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class SettingsState(
    val isLoading: Boolean = false,
    val notificationsEnabled: Boolean = true
)

sealed class SettingsEvent {
    data class SetNotificationsEnabled(val enabled: Boolean) : SettingsEvent()
}

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    
    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.SetNotificationsEnabled -> setNotifications(event.enabled)
        }
    }
    
    private fun setNotifications(enabled: Boolean) {
        // Update notification settings
    }
}
