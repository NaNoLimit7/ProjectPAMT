package com.example.projectpamt.viewmodel.penjualan

sealed class PembayaranUiState {
    object Idle : PembayaranUiState()
    object Loading : PembayaranUiState()
    object Success : PembayaranUiState()
    data class Error(val message: String) : PembayaranUiState()
}