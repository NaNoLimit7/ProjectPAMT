package com.example.projectpamt.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Kategori(
    @SerialName("id_kategori") val idKategori: String? = null,
    val name: String,
    @SerialName("created_at") val createdAt: String? = null
)