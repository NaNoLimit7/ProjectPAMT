package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.Kas
import com.example.projectpamt.data.model.LogKas
import com.example.projectpamt.data.model.KasBalanceSnapshot
import com.example.projectpamt.data.model.LogTotalKasDay
import com.example.projectpamt.data.model.LogTotalKasSummary
import io.github.jan.supabase.postgrest.postgrest
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class LogTotalKasRepository {
    private val supabase = SupabaseClientProvider.client

    suspend fun getLogTotalKasSummary(): LogTotalKasSummary {
        val allKas = supabase.postgrest["kas"].select().decodeList<Kas>()

        val currentTotalSaldo = allKas.sumOf { it.saldo }

        val todayStr = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        val todaySnapshot = LogTotalKasDay(
            idLogTotal = "current",
            tanggal = todayStr,
            totalSaldo = currentTotalSaldo,
            breakdown = allKas.map { 
                KasBalanceSnapshot(kasId = it.idKas ?: "", kasNama = it.nama, saldo = it.saldo) 
            }
        )

        val weeklyTrendPercent = 0.0

        return LogTotalKasSummary(
            totalSaldo = currentTotalSaldo,
            weeklyTrendPercent = weeklyTrendPercent,
            dailyLogs = listOf(todaySnapshot)
        )
    }
}
