package com.example.projectpamt.viewmodel.pengeluaran

import com.example.projectpamt.data.model.Pengeluaran

sealed class PengeluaranUiState {
    abstract val message: String?

    object Idle : PengeluaranUiState() {
        override val message: String? = null
    }
    object Loading : PengeluaranUiState() {
        override val message: String? = null
    }
    data class Success(val data: List<Pengeluaran>) : PengeluaranUiState() {
        override val message: String? = null
    }
    data class Error(override val message: String) : PengeluaranUiState()
}
