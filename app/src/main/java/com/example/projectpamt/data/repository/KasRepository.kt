package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.Kas
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class KasRepository {
    private val supabase = SupabaseClientProvider.client

    suspend fun getAllKas(): List<Kas> {
        return supabase.postgrest["kas"].select().decodeList<Kas>()
    }

    suspend fun getKasAktif(): List<Kas> {
        return supabase.postgrest["kas"].select { filter { eq("aktif", true) } }.decodeList<Kas>()
    }

    suspend fun tambahKas(nama: String, saldo: Double, aktif: Boolean, keteranganUser: String) {
        supabase.postgrest.rpc(
            "tambah_kas_dan_catat_log", buildJsonObject {
                put("p_nama", nama)
                put("p_saldo", saldo)
                put("p_aktif", aktif)
                put("p_keterangan_user", keteranganUser)
            }
        )
    }

    suspend fun updateKasDanCatatLog(idKas: String, nama: String, aktif: Boolean, keteranganUser: String) {
        supabase.postgrest.rpc(
            "update_kas_dan_catat_log", buildJsonObject {
                put("p_id_kas", idKas)
                put("p_nama", nama)
                put("p_aktif", aktif)
                put("p_keterangan_user", keteranganUser)
            }
        )
    }

    suspend fun updateSaldo(idKas: String, perubahanSaldo: Double, keterangan: String) {
        supabase.postgrest.rpc(
            "update_saldo_kas", buildJsonObject {
                put("p_id_kas", idKas)
                put("p_perubahan_saldo", perubahanSaldo)
                put("p_keterangan", keterangan)
            })
    }

    suspend fun nonaktifkanKas(idKas: String) {
        supabase.postgrest["kas"].update({
            set("aktif", false)
        }) {
            filter { eq("id_kas", idKas) }
        }
    }
}