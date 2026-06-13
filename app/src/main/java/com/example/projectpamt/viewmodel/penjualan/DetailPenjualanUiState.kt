package com.example.projectpamt.viewmodel.penjualan

import com.example.projectpamt.data.model.DetailPenjualan

sealed class DetailPenjualanUiState {
    abstract val message: String?

    object Idle : DetailPenjualanUiState() {
        override val message: String? = null
    }
    object Loading : DetailPenjualanUiState() {
        override val message: String? = null
    }
    data class Success(val data: List<DetailPenjualan>) : DetailPenjualanUiState() {
        override val message: String? = null
    }
    data class Error(override val message: String) : DetailPenjualanUiState()
}
