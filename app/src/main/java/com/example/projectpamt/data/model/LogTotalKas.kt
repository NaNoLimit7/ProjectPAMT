package com.example.projectpamt.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KasBalanceSnapshot(
    @SerialName("kas_id") val kasId: String,
    @SerialName("kas_nama") val kasNama: String,
    val saldo: Double
)

@Serializable
data class LogTotalKasDay(
    @SerialName("id_log_total") val idLogTotal: String? = null,
    val tanggal: String,
    @SerialName("total_saldo") val totalSaldo: Double,
    val breakdown: List<KasBalanceSnapshot>
)

@Serializable
data class LogTotalKasSummary(
    @SerialName("total_saldo") val totalSaldo: Double,
    @SerialName("weekly_trend_percent") val weeklyTrendPercent: Double,
    @SerialName("daily_logs") val dailyLogs: List<LogTotalKasDay>
)
