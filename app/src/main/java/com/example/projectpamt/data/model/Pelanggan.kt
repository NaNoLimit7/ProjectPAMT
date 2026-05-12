package com.example.projectpamt.data.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Pelanggan(
    val idPelanggan: String,
    val nama: String,
    val telepon: String,
    val aktif: Boolean,
    val createdAt: Instant
)
