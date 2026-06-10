package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.LogKas
import io.github.jan.supabase.postgrest.postgrest

class LogKasRepository {
    private val supabase = SupabaseClientProvider.client

    suspend fun getAllLogKas(): List<LogKas> {
        return supabase.postgrest["log_kas"].select().decodeList<LogKas>()
    }
}