package com.example.projectpamt.viewmodel.penjualan

sealed class PenjualanUiState {
    object Idle : PenjualanUiState()
    object Loading : PenjualanUiState()
    data class Success(val idPenjualan: String) : PenjualanUiState()
    data class Error(val message: String) : PenjualanUiState()
}
