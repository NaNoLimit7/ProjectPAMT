package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.Kategori
import io.github.jan.supabase.postgrest.postgrest

class KategoriRepository {
    private val supabase = SupabaseClientProvider.client

    suspend fun getAllKategori(): List<Kategori> {
        return supabase.postgrest["kategori"].select().decodeList<Kategori>()
    }

    suspend fun insertKategori(kategori: Kategori) {
        supabase.postgrest["kategori"].insert(kategori)
    }

    suspend fun updateKategori(id: String, kategori: Kategori){
        supabase.postgrest["kategori"].update(kategori){
            filter {
                eq("id_kategori",id)
            }
        }
    }

    suspend fun deleteKategori(id: String){
        supabase.postgrest["kategori"].delete {
            filter {
                eq("id_kategori", id)
            }
        }
    }
}