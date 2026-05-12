package com.example.projectpamt.data.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Produk(
    val idProduk: String,
    val nama: String,
    val harga: Double,
    val stok: Double,
    val namaSatuan: String,
    val aktif: Boolean,
    val createdAt: Instant
)
