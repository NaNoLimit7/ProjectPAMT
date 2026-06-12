package com.example.projectpamt.data.repository

import com.example.projectpamt.data.model.KasBalanceSnapshot
import com.example.projectpamt.data.model.LogTotalKasDay
import com.example.projectpamt.data.model.LogTotalKasSummary
import kotlinx.coroutines.delay

class LogTotalKasRepository {

    suspend fun getLogTotalKasSummary(): LogTotalKasSummary {
        // Simulate network delay
        delay(500)
        
        return LogTotalKasSummary(
            totalSaldo = 128450000.0,
            weeklyTrendPercent = 12.4,
            dailyLogs = listOf(
                LogTotalKasDay(
                    idLogTotal = "d9fbc382-7e3f-42cb-b1bb-8db7d78a83ee",
                    tanggal = "2026-05-24T00:00:00Z",
                    totalSaldo = 45890.25,
                    breakdown = listOf(
                        KasBalanceSnapshot(kasId = "1", kasNama = "Kas Utama", saldo = 30000.25),
                        KasBalanceSnapshot(kasId = "2", kasNama = "Kas Laci 1", saldo = 15890.00)
                    )
                ),
                LogTotalKasDay(
                    idLogTotal = "2f9b3c4d-8e5f-46a1-b2c3-4d5e6f7a8b9c",
                    tanggal = "2026-05-23T00:00:00Z",
                    totalSaldo = 42120.50,
                    breakdown = listOf(
                        KasBalanceSnapshot(kasId = "1", kasNama = "Kas Utama", saldo = 28500.50),
                        KasBalanceSnapshot(kasId = "2", kasNama = "Kas Laci 1", saldo = 13620.00)
                    )
                ),
                LogTotalKasDay(
                    idLogTotal = "a1b2c3d4-e5f6-47a8-b9c0-d1e2f3a4b5c6",
                    tanggal = "2026-05-22T00:00:00Z",
                    totalSaldo = 39450.00,
                    breakdown = listOf(
                        KasBalanceSnapshot(kasId = "1", kasNama = "Kas Utama", saldo = 26400.00),
                        KasBalanceSnapshot(kasId = "2", kasNama = "Kas Laci 1", saldo = 13050.00)
                    )
                )
            )
        )
    }
}
