package com.example.projectpamt.data.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class LogKas(
    val idLogKas: String,
    val idKas: String,
    val saldoAwal: Double,
    val saldoAkhir: Double,
    val keterangan: String,
    val updatedAt: Instant
)
