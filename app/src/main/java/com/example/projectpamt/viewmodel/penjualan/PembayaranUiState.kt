package com.example.projectpamt.viewmodel.penjualan

sealed class PembayaranUiState {
    object Idle : PembayaranUiState()
    object Loading : PembayaranUiState()
    data class Success(val transactionId: String) : PembayaranUiState()
    data class Error(val message: String) : PembayaranUiState()
}