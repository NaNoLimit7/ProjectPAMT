package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.LogPelanggan
import com.example.projectpamt.data.model.Pelanggan
import io.github.jan.supabase.postgrest.postgrest

class PelangganRepository {
    private val supabase = SupabaseClientProvider.client

    suspend fun getPelanggan(): List<Pelanggan> {
        return supabase.postgrest["pelanggan"].select().decodeList<Pelanggan>()
    }

    suspend fun tambahPelanggan(pelanggan: Pelanggan) {
        supabase.postgrest["pelanggan"].insert(pelanggan)
    }

    suspend fun updatePelanggan(pelangganLama: Pelanggan, pelangganBaru: Pelanggan) {

    }
}