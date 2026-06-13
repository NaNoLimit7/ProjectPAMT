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

sealed class AktivitasPelangganUiState {
    abstract val message: String?

    object Idle : AktivitasPelangganUiState() {
        override val message: String? = null
    }
    object Loading : AktivitasPelangganUiState() {
        override val message: String? = null
    }
    data class Success(
        val summary: AktivitasSummary,
        val listAktivitas: List<PelangganAktivitas>,
        val selectedFilter: AktivitasFilter
    ) : AktivitasPelangganUiState() {
        override val message: String? = null
    }
    data class Error(override val message: String) : AktivitasPelangganUiState()
}
