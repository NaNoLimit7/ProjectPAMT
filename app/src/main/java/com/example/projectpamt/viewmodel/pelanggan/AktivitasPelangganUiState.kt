package com.example.projectpamt.viewmodel.pelanggan

import java.time.LocalDateTime

enum class AktivitasFilter {
    SEMUA_WAKTU,
    BULAN_INI,
    TIGA_BULAN_TERAKHIR,
    TAHUN_INI
}

enum class AktivitasType {
    SELESAI
}

data class PelangganAktivitas(
    val idAktivitas: String,
    val tanggal: String,
    val dateTime: LocalDateTime,
    val tipe: AktivitasType,
    val total: Double,
    val jumlahItem: Int
)

data class AktivitasSummary(
    val totalBelanja: Double,
    val totalTransaksi: Int,
    val terakhirAktif: String
)

sealed interface AktivitasPelangganUiState {
    object Idle : AktivitasPelangganUiState
    object Loading : AktivitasPelangganUiState
    data class Success(
        val summary: AktivitasSummary,
        val listAktivitas: List<PelangganAktivitas>,
        val selectedFilter: AktivitasFilter
    ) : AktivitasPelangganUiState
    data class Error(val message: String) : AktivitasPelangganUiState
}
