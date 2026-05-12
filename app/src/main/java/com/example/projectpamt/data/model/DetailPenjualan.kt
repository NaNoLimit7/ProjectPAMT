package com.example.projectpamt.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DetailPenjualan(
    val idDetailPenjualan: String,
    val idPenjualan: String,
    val idProduk: String,
    val kuantitas: Double,
    val hargaSatuan: Double
)
