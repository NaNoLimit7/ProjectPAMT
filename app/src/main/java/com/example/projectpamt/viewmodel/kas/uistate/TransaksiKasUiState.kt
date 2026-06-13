package com.example.projectpamt.viewmodel.kas.uistate

import com.example.projectpamt.data.model.LogKasItem

sealed class TransaksiKasUiState {
    abstract val message: String?

    object Idle : TransaksiKasUiState() {
        override val message: String? = null
    }
    object Loading : TransaksiKasUiState() {
        override val message: String? = null
    }
    data class Success(val transactions: List<LogKasItem>) : TransaksiKasUiState() {
        override val message: String? = null
    }
    data class Error(override val message: String) : TransaksiKasUiState()
}
