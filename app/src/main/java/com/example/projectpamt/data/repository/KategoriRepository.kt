package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.Kategori
import io.github.jan.supabase.postgrest.postgrest

class KategoriRepository {
    private val supabase = SupabaseClientProvider.client

    //READ: Mengambil semua kategori pengeluran
    suspend fun getAllKategori(): List<Kategori> {
        return supabase.postgrest["kategori"].select().decodeList<Kategori>()
    }

    //INSERT: Menambah kategori baru
    suspend fun insertKategori(kategori: Kategori) {
        supabase.postgrest["kategori"].insert(kategori)
    }

    //UPDATE: Mengubah nama kategori berdasarkan ID
    suspend fun updateKategori(id: String, kategori: Kategori){
        supabase.postgrest["kategori"].update(kategori){
            filter {
                eq("id_kategori",id)
            }
        }
    }

    //DELETE: Menghapus kategori berdasarkan ID
    suspend fun deleteKategori(id: String){
        supabase.postgrest["kategori"].delete {
            filter {
                eq("id_kategori", id)
            }
        }
    }
}