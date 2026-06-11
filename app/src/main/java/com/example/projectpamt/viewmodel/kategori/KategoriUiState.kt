package com.example.projectpamt.viewmodel.kategori

import com.example.projectpamt.data.model.Kategori

sealed class KategoriUiState {
    object Idle : KategoriUiState()
    object Loading : KategoriUiState()
    data class Success(val data: List<Kategori>) : KategoriUiState()
    data class Error(val message: String) : KategoriUiState()
}
