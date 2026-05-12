package com.example.projectpamt.data.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Kategori(
    val idKategori: String,
    val name: String,
    val createdAt: Instant
)
