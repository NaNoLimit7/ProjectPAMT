package com.example.projectpamt.viewmodel.kas

import com.example.projectpamt.data.model.LogTotalKasSummary

sealed class LogTotalKasUiState {
    object Idle : LogTotalKasUiState()
    object Loading : LogTotalKasUiState()
    data class Success(val data: LogTotalKasSummary) : LogTotalKasUiState()
    data class Error(val message: String) : LogTotalKasUiState()
}
