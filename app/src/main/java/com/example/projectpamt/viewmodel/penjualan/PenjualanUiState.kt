package com.example.projectpamt.viewmodel.penjualan

import com.example.projectpamt.data.model.Pelanggan
import com.example.projectpamt.data.model.Produk

sealed class PenjualanUiState {
    object Idle : PenjualanUiState()
    object Loading : PenjualanUiState()
    data class Success(val idPenjualan: String) : PenjualanUiState()
    data class Error(val message: String) : PenjualanUiState()
}

sealed class PenjualanDataUiState {
    object Idle : PenjualanDataUiState()
    object Loading : PenjualanDataUiState()
    data class Success(
        val totalTransaksi: Int,
        val pelangganList: List<Pelanggan>,
        val produkList: List<Produk>
    ) : PenjualanDataUiState()
    data class Error(val message: String) : PenjualanDataUiState()
}

data class CartItem(val produk: Produk, var quantity: Int = 1) {
    val totalHarga: Double get() = produk.harga * quantity
}
