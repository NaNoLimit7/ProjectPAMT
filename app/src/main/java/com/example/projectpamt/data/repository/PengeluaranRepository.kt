package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.Kategori
import com.example.projectpamt.data.model.Pengeluaran
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns

class PengeluaranRepository {
    private val supabase = SupabaseClientProvider.client

    suspend fun getAllPengeluaran(): List<Pengeluaran> {
        return supabase.postgrest["pengeluaran"]
            .select(Columns.raw("*, kategori(*), kas(*)"))
            .decodeList<Pengeluaran>()
    }

    suspend fun insertPengeluaran(pengeluaran: Pengeluaran) {
        supabase.postgrest["pengeluaran"].insert(pengeluaran)
    }

    suspend fun updatePengeluaran(id: String, pengeluaran: Pengeluaran) {
        supabase.postgrest["pengeluaran"].update(pengeluaran) {
            filter { eq("id_pengeluaran", id) }
        }
    }

    suspend fun deletePengeluaran(id: String) {
        supabase.postgrest["pengeluaran"].delete {
            filter { eq("id_pengeluaran", id) }
        }
    }
}