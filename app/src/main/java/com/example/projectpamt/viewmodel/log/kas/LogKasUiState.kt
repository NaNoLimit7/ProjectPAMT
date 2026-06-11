package com.example.projectpamt.viewmodel.log.kas

import com.example.projectpamt.data.model.LogKas

sealed class LogKasUiState {
    object Idle : LogKasUiState()
    object Loading : LogKasUiState()
    data class Success(val data: List<LogKas>) : LogKasUiState()
    data class Error(val message: String) : LogKasUiState()
}
