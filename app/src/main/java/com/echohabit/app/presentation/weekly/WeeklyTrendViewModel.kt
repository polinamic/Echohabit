package com.echohabit.app.presentation.weekly

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class WeeklyTrendState(
    val isLoading: Boolean = false,
    val trendData: List<Float> = emptyList()
)

sealed class WeeklyTrendEvent {
    object LoadTrends : WeeklyTrendEvent()
}

@HiltViewModel
class WeeklyTrendViewModel @Inject constructor() : ViewModel() {
    
    fun onEvent(event: WeeklyTrendEvent) {
        when (event) {
            is WeeklyTrendEvent.LoadTrends -> loadTrends()
        }
    }
    
    private fun loadTrends() {
        // Load weekly trend data
    }
}
