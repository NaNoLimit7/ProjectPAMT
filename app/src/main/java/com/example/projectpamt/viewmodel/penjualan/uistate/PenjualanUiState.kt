package com.example.projectpamt.viewmodel.penjualan.uistate

import com.example.projectpamt.data.model.Pelanggan
import com.example.projectpamt.data.model.Produk
import kotlinx.serialization.Serializable

sealed class PenjualanUiState {
    abstract val message: String?

    object Idle : PenjualanUiState() {
        override val message: String? = null
    }
    object Loading : PenjualanUiState() {
        override val message: String? = null
    }
    data class Success(val idPenjualan: String) : PenjualanUiState() {
        override val message: String? = null
    }
    data class Error(override val message: String) : PenjualanUiState()
}

sealed class PenjualanDataUiState {
    abstract val message: String?

    object Idle : PenjualanDataUiState() {
        override val message: String? = null
    }
    object Loading : PenjualanDataUiState() {
        override val message: String? = null
    }
    data class Success(
        val totalTransaksi: Int,
        val pelangganList: List<Pelanggan>,
        val produkList: List<Produk>
    ) : PenjualanDataUiState() {
        override val message: String? = null
    }
    data class Error(override val message: String) : PenjualanDataUiState()
}

@Serializable
data class CartItem(val produk: Produk, val quantity: Int = 1) {
    val totalHarga: Double get() = produk.harga * quantity
}

