package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.DetailPenjualan
import com.example.projectpamt.data.model.Penjualan
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put

import io.github.jan.supabase.postgrest.query.Count

@Serializable
private data class PenjualanTotal(
    @SerialName("total_harga") val totalHarga: Double
)

class PenjualanRepository {
    private val supabase = SupabaseClientProvider.client

    suspend fun prosesPenjualan(
        idPelanggan: String,
        idKas: String,
        jumlahBayar: Double,
        totalHarga: Double,
        items: List<DetailPenjualan>,
        detailPenjualan: JsonElement? = null
    ): String {
        val itemsJson = Json.encodeToJsonElement(items)

        val idPenjualanBaru = supabase.postgrest.rpc(
            "proses_penjualan",
            buildJsonObject {
                put("p_id_pelanggan", idPelanggan)
                put("p_id_kas", idKas)
                put("p_jumlah_bayar", jumlahBayar)
                put("p_total_harga", totalHarga)
                put("p_items", itemsJson)
                if (detailPenjualan != null) {
                    put("p_detail_penjualan", detailPenjualan)
                } else {
                    put("p_detail_penjualan", JsonNull)
                }
            }
        ).decodeAs<String>()

        return idPenjualanBaru
    }

    suspend fun getTotalTransaksi(): Int {
        return supabase.postgrest["penjualan"]
            .select { count(Count.EXACT) }
            .countOrNull()?.toInt() ?: 0
    }

    suspend fun getTotalNilaiPenjualan(): Double {
        return supabase.postgrest["penjualan"]
            .select(columns = io.github.jan.supabase.postgrest.query.Columns.raw("total_harga"))
            .decodeList<PenjualanTotal>()
            .sumOf { it.totalHarga }
    }

    suspend fun getPenjualanByPelanggan(idPelanggan: String): List<Penjualan> {
        return supabase.postgrest["penjualan"].select {
            filter {
                eq("id_pelanggan", idPelanggan)
            }
        }.decodeList<Penjualan>()
    }

    suspend fun getRiwayatPenjualan(): List<Penjualan> {
        return supabase.postgrest["penjualan"].select(
            columns = io.github.jan.supabase.postgrest.query.Columns.raw("*, pelanggan(*), kas(*)")
        ) {
            order("created_at", order = io.github.jan.supabase.postgrest.query.Order.DESCENDING)
        }.decodeList<Penjualan>()
    }
}