package com.example.projectpamt.viewmodel.pelanggan.uistate

import com.example.projectpamt.data.model.Pelanggan

sealed class PelangganUiState {
    abstract val message: String?

    object Idle : PelangganUiState() {
        override val message: String? = null
    }

    object Loading : PelangganUiState() {
        override val message: String? = null
    }

    data class Success(val data: List<Pelanggan>) : PelangganUiState() {
        override val message: String? = null
    }

    data class Error(override val message: String) : PelangganUiState()
}
