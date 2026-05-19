package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.LogPelanggan
import com.example.projectpamt.data.model.Pelanggan
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest

class PelangganRepository {
    private val supabase = SupabaseClientProvider.client

    //READ: Mengambil semua data pelanggan
    suspend fun getAllPelanggan(): List<Pelanggan> {
        return supabase.postgrest["pelanggan"].select().decodeList<Pelanggan>()
    }

    //CREATE: Menambah pelanggan baru
    suspend fun insertPelanggan(pelanggan: Pelanggan) {
        supabase.postgrest["pelanggan"].insert(pelanggan)
    }

    //UPDATE: Memperbarui data pelanggan
    suspend fun updatePelanggan(id: String, pelangganBaru: Pelanggan) {
        supabase.postgrest["pelanggan"].update(pelangganBaru) {
            filter { eq("id_pelanggan", id) }
        }
    }

    //DELETE: Menghapus pelanggan
    suspend fun deletePelanggan(id: String) {
        supabase.postgrest["pelanggan"].delete {
            filter { eq("id_pelanggan", id) }
        }
    }
}