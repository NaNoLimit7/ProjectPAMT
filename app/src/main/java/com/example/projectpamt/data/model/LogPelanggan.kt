package com.example.projectpamt.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class LogPelanggan(
    @SerialName("id_log_pelanggan") val idLogPelanggan: String? = null,
    @SerialName("id_pelanggan") val idPelanggan: String,
    @SerialName("nama_lama") val namaLama: String,
    @SerialName("telepon_lama") val teleponLama: String,
    @SerialName("aktif_lama") val aktifLama: Boolean,
    @SerialName("updated_at") val updatedAt: String? = null
)
