package com.echohabit.app.presentation.dashboard

import androidx.lifecycle.ViewModel
import com.echohabit.app.domain.usecase.summary.GenerateDailySummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class DashboardState(
    val isLoading: Boolean = false,
    val qualityScore: Float = 0.5f
)

sealed class DashboardEvent {
    object LoadDashboard : DashboardEvent()
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val generateDailySummaryUseCase: GenerateDailySummaryUseCase
) : ViewModel() {
    
    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.LoadDashboard -> loadDashboard()
        }
    }
    
    private fun loadDashboard() {
        // Load dashboard data
    }
}
