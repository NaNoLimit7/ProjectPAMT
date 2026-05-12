package com.example.projectpamt.data.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Penjualan(
    val idPenjualan: String,
    val idPelanggan: String,
    val idKas: String,
    val jumlahBayar: Double,
    val totalHarga: Double,
    val createdAt: Instant
)