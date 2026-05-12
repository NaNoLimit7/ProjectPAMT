package com.example.projectpamt.data.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Pengeluaran(
    val idPengeluaran: String,
    val idKategori: String,
    val idKas: String,
    val deskripsi: String,
    val total: Double,
    val createdAt: Instant
)