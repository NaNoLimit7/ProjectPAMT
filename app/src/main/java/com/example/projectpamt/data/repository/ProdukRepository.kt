package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.Produk
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class ProdukRepository {
    private val supabase = SupabaseClientProvider.client

    suspend fun getProdukAktif(): List<Produk> {
        return supabase.postgrest["produk"]
            .select {
                filter {
                    eq("aktif", true)
                }
            }
            .decodeList<Produk>()
    }

    suspend fun tambahProduk(produk: Produk) {
        supabase.postgrest["produk"].insert(produk)
    }

    suspend fun updateInfoProduk(idProduk: String, namaBaru: String, hargaBaru: Double) {
        supabase.postgrest["produk"].update({
            set("nama", namaBaru)
            set("harga", hargaBaru)
        }) {
            filter { eq("id_produk", idProduk) }
        }
    }

    suspend fun updateStok(idProduk: String, perubahanStok: Double) {
        supabase.postgrest.rpc(
            "update_stok_produk", buildJsonObject {
                put("p_id_produk", idProduk)
                put("p_perubahan_stok", perubahanStok)
            }
        )
    }

    suspend fun nonaktifkanProduk(idProduk: String) {
        supabase.postgrest["produk"].update({
            set("aktif", false)
        }) {
            filter { eq("id_produk", idProduk) }
        }
    }
}