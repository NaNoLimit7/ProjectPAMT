package com.example.projectpamt.viewmodel.dashboard

import com.example.projectpamt.ui.screens.home.dashboard.DashboardState

sealed class DashboardUiState {
    abstract val message: String?

    object Idle : DashboardUiState() {
        override val message: String? = null
    }

    object Loading : DashboardUiState() {
        override val message: String? = null
    }

    data class Success(val state: DashboardState) : DashboardUiState() {
        override val message: String? = null
    }

    data class Error(override val message: String) : DashboardUiState()
}