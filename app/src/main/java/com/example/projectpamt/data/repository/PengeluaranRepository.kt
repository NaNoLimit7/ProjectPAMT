package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.Kategori
import com.example.projectpamt.data.model.Pengeluaran
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns

class PengeluaranRepository {
    private val supabase = SupabaseClientProvider.client

    //READ: mengambil semua pengeluaran
    suspend fun getAllPengeluaran(): List<Pengeluaran> {
        return supabase.postgrest["pengeluaran"]
            .select(Columns.raw("*, kategori(*), kas(*)"))
            .decodeList<Pengeluaran>()
    }

    //CREATE: Menambah pengeluaran baru
    suspend fun insertPengeluaran(pengeluaran: Pengeluaran) {
        supabase.postgrest["pengeluaran"].insert(pengeluaran)
    }

    //UPDATE: Memperbarui data pengeluaran
    suspend fun updatePengeluaran(id: String, pengeluaran: Pengeluaran) {
        supabase.postgrest["pengeluaran"].update(pengeluaran) {
            filter { eq("id_pengeluaran", id) }
        }
    }

    //DELETE: Menghapus data pengeluaran
    suspend fun deletePengeluaran(id: String) {
        supabase.postgrest["pengeluaran"].delete {
            filter { eq("id_pengeluaran", id) }
        }
    }

//    private val kasRepository = KasRepository()
//
//    suspend fun getPengeluaran(): List<Pengeluaran> {
//        return supabase.postgrest["pengeluaran"].select().decodeList<Pengeluaran>()
//    }
//
//    suspend fun getDaftarKategori(): List<Kategori> {
//        return supabase.postgrest["kategori"].select().decodeList<Kategori>()
//    }
//
//    suspend fun tambahPengeluaran(pengeluaran: Pengeluaran) {
//        supabase.postgrest["pengeluaran"].insert(pengeluaran)
//    }
}