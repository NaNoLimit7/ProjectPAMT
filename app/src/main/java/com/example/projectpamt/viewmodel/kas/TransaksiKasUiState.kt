package com.example.projectpamt.viewmodel.kas

import com.example.projectpamt.data.model.LogKasItem

sealed class TransaksiKasUiState {
    object Idle : TransaksiKasUiState()
    object Loading : TransaksiKasUiState()
    data class Success(val transactions: List<LogKasItem>) : TransaksiKasUiState()
    data class Error(val message: String) : TransaksiKasUiState()
}
