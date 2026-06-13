package com.example.projectpamt.viewmodel.kategori

import com.example.projectpamt.data.model.Kategori

sealed class KategoriUiState {
    abstract val message: String?

    object Idle : KategoriUiState() {
        override val message: String? = null
    }
    object Loading : KategoriUiState() {
        override val message: String? = null
    }
    data class Success(val data: List<Kategori>) : KategoriUiState() {
        override val message: String? = null
    }
    data class Error(override val message: String) : KategoriUiState()
}
