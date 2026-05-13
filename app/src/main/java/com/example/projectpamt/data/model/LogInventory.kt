package com.example.projectpamt.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class LogInventory(
    @SerialName("id_log_inventory") val idLogInventory: String? = null,
    @SerialName("id_produk") val idProduk: String,
    @SerialName("nama_lama") val namaLama: String,
    @SerialName("harga_lama") val hargaLama: Double,
    @SerialName("stok_lama") val stokLama: Double,
    @SerialName("stok_baru") val stokBaru: Double,
    @SerialName("updated_at") val updatedAt: String? = null
)
