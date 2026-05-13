package com.example.projectpamt.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class LogKas(
    @SerialName("id_log_kas") val idLogKas: String? = null,
    @SerialName("id_kas") val idKas: String,
    @SerialName("saldo_awal") val saldoAwal: Double,
    @SerialName("saldo_akhir") val saldoAkhir: Double,
    val keterangan: String,
    @SerialName("updated_at") val updatedAt: String? = null
)
