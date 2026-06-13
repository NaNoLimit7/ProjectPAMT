package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.LogKas
import com.example.projectpamt.data.model.LogKasItem
import io.github.jan.supabase.postgrest.postgrest

class LogKasRepository {
    private val supabase = SupabaseClientProvider.client

    suspend fun getAllLogKas(): List<LogKas> {
        return supabase.postgrest["log_kas"].select().decodeList<LogKas>()
    }

    suspend fun getLogKas(idKas: String): List<LogKasItem> {
        val allLogs = supabase.postgrest["log_kas"].select {
            filter { eq("id_kas", idKas) }
        }.decodeList<LogKasItem>()
        
        // Filter out business transactions to show only administrative logs
        return allLogs.filterNot { log ->
            val lower = log.keterangan.lowercase()
            lower.contains("penjualan") || lower.contains("pengeluaran")
        }.sortedByDescending { it.updatedAt }
    }

    suspend fun getTransaksiKas(idKas: String): List<LogKasItem> {
        val allLogs = supabase.postgrest["log_kas"].select {
            filter { eq("id_kas", idKas) }
        }.decodeList<LogKasItem>()
        
        // Filter in only business transactions
        return allLogs.filter { log ->
            val lower = log.keterangan.lowercase()
            lower.contains("penjualan") || lower.contains("pengeluaran")
        }.sortedByDescending { it.updatedAt }
    }
}