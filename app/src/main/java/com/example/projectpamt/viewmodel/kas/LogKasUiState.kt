package com.example.projectpamt.viewmodel.kas

import com.example.projectpamt.data.model.LogKasItem

sealed class LogKasUiState {
    object Idle : LogKasUiState()
    object Loading : LogKasUiState()
    data class Success(val logs: List<LogKasItem>) : LogKasUiState()
    data class Error(val message: String) : LogKasUiState()
}
