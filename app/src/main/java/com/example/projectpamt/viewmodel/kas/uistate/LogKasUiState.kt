package com.example.projectpamt.viewmodel.kas.uistate

import com.example.projectpamt.data.model.LogKasItem

sealed class LogKasUiState {
    abstract val message: String?

    object Idle : LogKasUiState() {
        override val message: String? = null
    }
    object Loading : LogKasUiState() {
        override val message: String? = null
    }
    data class Success(val logs: List<LogKasItem>) : LogKasUiState() {
        override val message: String? = null
    }
    data class Error(override val message: String) : LogKasUiState()
}
