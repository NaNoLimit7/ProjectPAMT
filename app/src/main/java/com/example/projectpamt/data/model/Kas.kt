package com.example.projectpamt.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Kas(
    // Nullable saat insert baru
    @SerialName("id_kas") val idKas: String? = null,
    val nama: String,
    val saldo: Double,
    val aktif: Boolean = true,
    @SerialName("created_at") val createdAt: String? = null
)
