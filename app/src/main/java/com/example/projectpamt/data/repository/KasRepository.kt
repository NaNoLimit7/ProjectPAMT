package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.Kas
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class KasRepository {
    private val supabase = SupabaseClientProvider.client

    suspend fun getActiveKas(): List<Kas> {
        return supabase.postgrest["kas"].select { filter { eq("aktif", true) } }.decodeList<Kas>()
    }

    suspend fun tambahKas(kas: Kas) {
        supabase.postgrest["kas"].insert(kas)
    }

    suspend fun updateNamaKas(idKas: String, namaBaru: String) {
        supabase.postgrest["kas"].update({
            set("nama", namaBaru)
        }) {
            filter { eq("id_kas", idKas) }
        }
    }

    suspend fun updateSaldo(idKas: String, perubahanSaldo: Double, keterangan: String) {
        supabase.postgrest.rpc(
            "update_saldo_kas", buildJsonObject {
                put("id_kas", idKas)
                put("perubahan_saldo", perubahanSaldo)
                put("keterangan", keterangan)
            }
        )
    }

    suspend fun nonaktifkanKas(idKas: String) {
        supabase.postgrest["kas"].update({
            set("aktif", false)
        }) {
            filter { eq("id_kas", idKas) }
        }
    }
}