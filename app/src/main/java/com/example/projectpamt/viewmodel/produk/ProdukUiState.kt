package com.example.projectpamt.viewmodel.produk

import com.example.projectpamt.data.model.Produk

sealed class ProdukUiState {
    object Idle : ProdukUiState()
    object Loading : ProdukUiState()
    data class Success(val data: List<Produk>) : ProdukUiState()
    data class Error(val message: String) : ProdukUiState()
}
