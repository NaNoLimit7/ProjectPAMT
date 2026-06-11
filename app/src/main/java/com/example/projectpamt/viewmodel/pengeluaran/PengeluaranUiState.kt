package com.example.projectpamt.viewmodel.pengeluaran

import com.example.projectpamt.data.model.Pengeluaran

sealed class PengeluaranUiState {
    object Idle : PengeluaranUiState()
    object Loading : PengeluaranUiState()
    data class Success(val data: List<Pengeluaran>) : PengeluaranUiState()
    data class Error(val message: String) : PengeluaranUiState()
}
