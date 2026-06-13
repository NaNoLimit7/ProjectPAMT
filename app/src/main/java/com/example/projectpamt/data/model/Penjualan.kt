package com.example.projectpamt.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlin.time.Instant

@Serializable
data class Penjualan(
    @SerialName("id_penjualan") val idPenjualan: String? = null,
    @SerialName("id_pelanggan") val idPelanggan: String,
    @SerialName("id_kas") val idKas: String,
    @SerialName("jumlah_bayar") val jumlahBayar: Double,
    @SerialName("total_harga") val totalHarga: Double,
    @SerialName("detail_penjualan") val detailPenjualan: JsonElement? = null,
    @SerialName("created_at") val createdAt: String? = null,
    
    // Relationships
    val pelanggan: Pelanggan? = null,
    val kas: Kas? = null
)