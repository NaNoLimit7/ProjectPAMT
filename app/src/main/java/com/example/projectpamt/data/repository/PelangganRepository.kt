package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.Pelanggan
import io.github.jan.supabase.postgrest.postgrest

class PelangganRepository {
    private val supabase = SupabaseClientProvider.client

    suspend fun getAllPelanggan(): List<Pelanggan> {
        return supabase.postgrest["pelanggan"].select {
            order("created_at", order = io.github.jan.supabase.postgrest.query.Order.DESCENDING)
        }.decodeList<Pelanggan>()
    }

    suspend fun insertPelanggan(pelanggan: Pelanggan) {
        supabase.postgrest["pelanggan"].insert(pelanggan)
    }

    suspend fun updatePelanggan(id: String, pelangganBaru: Pelanggan) {
        supabase.postgrest["pelanggan"].update(pelangganBaru) {
            filter { eq("id_pelanggan", id) }
        }
    }

    suspend fun softDeletePelanggan(id: String) {
        supabase.postgrest["pelanggan"].update({
            set("aktif", false)
        }) {
            filter { eq("id_pelanggan", id) }
        }
    }
}