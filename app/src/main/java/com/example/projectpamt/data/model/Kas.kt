package com.example.projectpamt.data.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Kas(
    val idKas: String,
    val nama: String,
    val saldo: Double,
    val aktif: Boolean,
    val createdAt: Instant
)
