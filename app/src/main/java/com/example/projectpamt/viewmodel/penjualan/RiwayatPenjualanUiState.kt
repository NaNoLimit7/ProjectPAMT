package com.example.projectpamt.viewmodel.penjualan

import com.example.projectpamt.data.model.Kas
import com.example.projectpamt.data.model.Pelanggan
import com.example.projectpamt.data.model.Penjualan

data class PenjualanWithDetails(
    val penjualan: Penjualan,
    val pelanggan: Pelanggan?,
    val kas: Kas?,
    val items: List<CartItem>
)

enum class RiwayatFilter {
    SEMUA_WAKTU,
    HARI_INI,
    MINGGU_INI,
    BULAN_INI
}

sealed class RiwayatPenjualanUiState {
    abstract val message: String?

    object Idle : RiwayatPenjualanUiState() {
        override val message: String? = null
    }
    object Loading : RiwayatPenjualanUiState() {
        override val message: String? = null
    }
    data class Success(
        val listPenjualan: List<PenjualanWithDetails>,
        val totalPendapatan: Double,
        val totalTransaksi: Int,
        val searchQuery: String,
        val selectedFilter: RiwayatFilter
    ) : RiwayatPenjualanUiState() {
        override val message: String? = null
    }
    data class Error(override val message: String) : RiwayatPenjualanUiState()
}
