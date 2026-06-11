package com.example.projectpamt.viewmodel.penjualan

import com.example.projectpamt.data.model.DetailPenjualan

sealed class DetailPenjualanUiState {
    object Idle : DetailPenjualanUiState()
    object Loading : DetailPenjualanUiState()
    data class Success(val data: List<DetailPenjualan>) : DetailPenjualanUiState()
    data class Error(val message: String) : DetailPenjualanUiState()
}
