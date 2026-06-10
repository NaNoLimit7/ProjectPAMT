package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.LogPelanggan
import io.github.jan.supabase.postgrest.postgrest

class LogPelangganRepository {
    private val supabase = SupabaseClientProvider.client

    suspend fun getAllLogPelanggan(): List<LogPelanggan> {
        return supabase.postgrest["log_pelanggan"].select().decodeList<LogPelanggan>()
    }
}