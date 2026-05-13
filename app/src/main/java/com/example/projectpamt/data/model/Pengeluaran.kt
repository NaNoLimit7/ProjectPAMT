package com.example.projectpamt.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Pengeluaran(
    @SerialName("id_pengeluaran") val idPengeluaran: String? = null,
    @SerialName("id_kategori") val idKategori: String,
    @SerialName("id_kas") val idKas: String,
    val deskripsi: String?,
    val total: Double,
    @SerialName("created_at") val createdAt: String? = null
)