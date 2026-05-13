package com.example.projectpamt.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailPenjualan(
    @SerialName("id_detail_penjualan") val idDetailPenjualan: String? = null,
    @SerialName("id_penjualan") val idPenjualan: String,
    @SerialName("id_produk") val idProduk: String,
    val kuantitas: Double,
    @SerialName("harga_satuan") val hargaSatuan: Double
)