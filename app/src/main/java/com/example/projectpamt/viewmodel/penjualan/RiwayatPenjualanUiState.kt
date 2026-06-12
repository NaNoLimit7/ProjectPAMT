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
    object Idle : RiwayatPenjualanUiState()
    object Loading : RiwayatPenjualanUiState()
    data class Success(
        val listPenjualan: List<PenjualanWithDetails>,
        val totalPendapatan: Double,
        val totalTransaksi: Int,
        val searchQuery: String,
        val selectedFilter: RiwayatFilter
    ) : RiwayatPenjualanUiState()
    data class Error(val message: String) : RiwayatPenjualanUiState()
}
