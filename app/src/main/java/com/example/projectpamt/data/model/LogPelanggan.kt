package com.example.projectpamt.data.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class LogPelanggan(
    val idLogPelanggan: String,
    val idPelanggan: String,
    val namaLama: String,
    val teleponLama: String,
    val aktifLama: Boolean,
    val updatedAt: Instant
)
