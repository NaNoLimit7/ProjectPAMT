package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.Produk
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
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
                order("created_at", order = io.github.jan.supabase.postgrest.query.Order.DESCENDING)
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

    suspend fun setStok(idProduk: String, stokBaru: Double) {
        supabase.postgrest["produk"].update({
            set("stok", stokBaru)
        }) {
            filter { eq("id_produk", idProduk) }
        }
    }

    suspend fun nonaktifkanProduk(idProduk: String) {
        supabase.postgrest["produk"].update({
            set("aktif", false)
        }) {
            filter { eq("id_produk", idProduk) }
        }
    }

    suspend fun updateProduk(idProduk: String, produkBaru: Produk) {
        supabase.postgrest["produk"].update(produkBaru) {
            filter { eq("id_produk", idProduk) }
        }
    }

    /**
     * Upload bytes gambar ke Supabase Storage bucket "produk-images".
     * @return Public URL gambar yang dapat diakses dari perangkat manapun.
     */
    suspend fun uploadGambarProduk(
        fileName: String,
        imageBytes: ByteArray,
        mimeType: String = "image/jpeg"
    ): String {
        val bucket = supabase.storage["produk-images"]
        bucket.upload(fileName, imageBytes) { upsert = true }
        return bucket.publicUrl(fileName)
    }
}