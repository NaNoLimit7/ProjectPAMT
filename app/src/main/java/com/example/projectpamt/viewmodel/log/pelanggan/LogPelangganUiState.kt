package com.example.projectpamt.viewmodel.log.pelanggan

import com.example.projectpamt.data.model.LogPelanggan

sealed class LogPelangganUiState {
    object Idle : LogPelangganUiState()
    object Loading : LogPelangganUiState()
    data class Success(val data: List<LogPelanggan>) : LogPelangganUiState()
    data class Error(val message: String) : LogPelangganUiState()
}
