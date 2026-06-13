package com.example.projectpamt.viewmodel.penjualan.uistate

sealed class PembayaranUiState {
    abstract val message: String?

    object Idle : PembayaranUiState() {
        override val message: String? = null
    }
    object Loading : PembayaranUiState() {
        override val message: String? = null
    }
    data class Success(val transactionId: String) : PembayaranUiState() {
        override val message: String? = null
    }
    data class Error(override val message: String) : PembayaranUiState()
}