package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.DetailPenjualan
import com.example.projectpamt.data.model.Kategori
import io.github.jan.supabase.postgrest.postgrest

class DetailPenjualanRepository {
    private val supabase = SupabaseClientProvider.client

    suspend fun getAllDetailPenjualan(): List<DetailPenjualan> {
        return supabase.postgrest["detail_penjualan"].select().decodeList<DetailPenjualan>()
    }

    suspend fun insertDetailPenjualan(detailPenjualan: DetailPenjualan) {
        supabase.postgrest["detail_penjualan"].insert(detailPenjualan)
    }
}