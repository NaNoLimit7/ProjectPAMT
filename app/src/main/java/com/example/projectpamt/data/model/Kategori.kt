package com.example.projectpamt.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Kategori(
    @SerialName("id_kategori") val idKategori: String? = null,
    val nama: String,
    @SerialName("created_at") val createdAt: String? = null
)