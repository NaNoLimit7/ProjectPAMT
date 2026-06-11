package com.example.projectpamt.viewmodel.pelanggan

import com.example.projectpamt.data.model.Pelanggan

sealed class PelangganUiState {
    object Idle : PelangganUiState()
    object Loading : PelangganUiState()
    data class Success(val data: List<Pelanggan>) : PelangganUiState()
    data class Error(val message: String) : PelangganUiState()
}
