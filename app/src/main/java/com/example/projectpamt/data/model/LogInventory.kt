package com.example.projectpamt.data.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class LogInventory(
    val idLogInventory: String,
    val idProduk: String,
    val namaLama: String,
    val hargaLama: Double,
    val stokLama: Double,
    val stokBaru: Double,
    val updatedAt: Instant
)
