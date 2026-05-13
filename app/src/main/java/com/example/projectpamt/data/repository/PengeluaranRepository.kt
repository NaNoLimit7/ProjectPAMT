package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.Kategori
import com.example.projectpamt.data.model.Pengeluaran
import io.github.jan.supabase.postgrest.postgrest

class PengeluaranRepository {
    private val supabase = SupabaseClientProvider.client

    private val kasRepository = KasRepository()

    suspend fun getPengeluaran(): List<Pengeluaran> {
        return supabase.postgrest["pengeluaran"].select().decodeList<Pengeluaran>()
    }

    suspend fun getDaftarKategori(): List<Kategori> {
        return supabase.postgrest["kategori"].select().decodeList<Kategori>()
    }

    suspend fun tambahPengeluaran(pengeluaran: Pengeluaran) {
        supabase.postgrest["pengeluaran"].insert(pengeluaran)
    }
}