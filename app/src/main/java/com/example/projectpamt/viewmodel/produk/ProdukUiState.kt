package com.example.projectpamt.viewmodel.produk

import com.example.projectpamt.data.model.Produk

sealed class ProdukUiState {
    abstract val message: String?

    object Idle : ProdukUiState() {
        override val message: String? = null
    }
    object Loading : ProdukUiState() {
        override val message: String? = null
    }
    data class Success(val data: List<Produk>) : ProdukUiState() {
        override val message: String? = null
    }
    data class Error(override val message: String) : ProdukUiState()
}
