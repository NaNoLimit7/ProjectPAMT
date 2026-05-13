package com.example.projectpamt.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlin.time.Instant

@Serializable
data class Produk(
    @SerialName("id_produk") val idProduk: String? = null,
    val nama: String,
    val harga: Double,
    val stok: Double,
    @SerialName("nama_satuan") val namaSatuan: String,
    @SerialName("detail_produk") val detailProduk: JsonElement? = null,
    val aktif: Boolean = true,
    @SerialName("created_at") val createdAt: String? = null
)