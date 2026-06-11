package com.example.projectpamt.viewmodel.kas

import com.example.projectpamt.data.model.Kas

sealed class KasUiState {
    object Idle : KasUiState()
    object Loading : KasUiState()
    data class Success(val data: List<Kas>) : KasUiState()
    data class Error(val message: String) : KasUiState()
}
