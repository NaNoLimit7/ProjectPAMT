package com.example.projectpamt.viewmodel.kas

import com.example.projectpamt.data.model.LogTotalKasSummary

sealed class LogTotalKasUiState {
    abstract val message: String?

    object Idle : LogTotalKasUiState() {
        override val message: String? = null
    }
    object Loading : LogTotalKasUiState() {
        override val message: String? = null
    }
    data class Success(val data: LogTotalKasSummary) : LogTotalKasUiState() {
        override val message: String? = null
    }
    data class Error(override val message: String) : LogTotalKasUiState()
}
