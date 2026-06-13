package com.example.projectpamt.viewmodel.kas.uistate

import com.example.projectpamt.data.model.Kas

sealed class KasUiState {
    abstract val message: String?

    object Idle : KasUiState() {
        override val message: String? = null
    }
    object Loading : KasUiState() {
        override val message: String? = null
    }
    data class Success(val data: List<Kas>) : KasUiState() {
        override val message: String? = null
    }
    data class Error(override val message: String) : KasUiState()
}
