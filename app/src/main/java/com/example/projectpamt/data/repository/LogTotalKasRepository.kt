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
        val allLogs = supabase.postgrest["log_kas"].select().decodeList<LogKas>()

        val currentTotalSaldo = allKas.sumOf { it.saldo }
        
        // Buat satu snapshot untuk hari ini berdasarkan data kas saat ini
        val todayStr = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        val todaySnapshot = LogTotalKasDay(
            idLogTotal = "current",
            tanggal = todayStr,
            totalSaldo = currentTotalSaldo,
            breakdown = allKas.map { 
                KasBalanceSnapshot(kasId = it.idKas ?: "", kasNama = it.nama, saldo = it.saldo) 
            }
        )

        // Hitung trend secara sederhana (sementara 0 jika tidak ada cukup data masa lalu untuk dihitung secara akurat)
        val weeklyTrendPercent = 0.0

        return LogTotalKasSummary(
            totalSaldo = currentTotalSaldo,
            weeklyTrendPercent = weeklyTrendPercent,
            dailyLogs = listOf(todaySnapshot)
        )
    }
}
