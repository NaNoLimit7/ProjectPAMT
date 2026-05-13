package com.example.projectpamt.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pelanggan(
    @SerialName("id_pelanggan") val idPelanggan: String? = null,
    val nama: String,
    val telepon: String,
    val aktif: Boolean = true,
    @SerialName("created_at") val createdAt: String? = null
)
