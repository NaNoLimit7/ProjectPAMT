package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.Pengeluaran
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class PengeluaranRepository {
    private val supabase = SupabaseClientProvider.client

    suspend fun getAllPengeluaran(): List<Pengeluaran> {
        return supabase.postgrest["pengeluaran"].select(
            columns = io.github.jan.supabase.postgrest.query.Columns.raw("*, kategori(*), kas(*)")
        ) {
            filter {
                eq("status", "aktif")
            }
        }.decodeList<Pengeluaran>()
    }

    suspend fun insertPengeluaran(pengeluaran: Pengeluaran) {
        val insertData = buildJsonObject {
            put("id_kategori", pengeluaran.idKategori)
            put("id_kas", pengeluaran.idKas)
            put("deskripsi", pengeluaran.deskripsi)
            put("total", pengeluaran.total)
            put("status", pengeluaran.status)
        }
        supabase.postgrest["pengeluaran"].insert(insertData)
    }

    suspend fun updatePengeluaran(id: String, pengeluaran: Pengeluaran) {
        // 1. Fetch old record to calculate balance differences
        val old = supabase.postgrest["pengeluaran"].select {
            filter {
                eq("id_pengeluaran", id)
            }
        }.decodeSingle<Pengeluaran>()

        // 2. Perform the update in the database
        val updateData = buildJsonObject {
            put("id_kategori", pengeluaran.idKategori)
            put("id_kas", pengeluaran.idKas)
            put("deskripsi", pengeluaran.deskripsi)
            put("total", pengeluaran.total)
            put("status", pengeluaran.status)
        }
        
        supabase.postgrest["pengeluaran"].update(updateData) {
            filter {
                eq("id_pengeluaran", id)
            }
        }

        // 3. Update kas balances manually since no UPDATE trigger exists
        if (old.idKas == pengeluaran.idKas) {
            val diff = old.total - pengeluaran.total
            if (diff != 0.0) {
                // Call update_saldo_kas RPC
                supabase.postgrest.rpc(
                    "update_saldo_kas", buildJsonObject {
                        put("p_id_kas", pengeluaran.idKas)
                        put("p_perubahan_saldo", diff)
                        put("p_keterangan", "Koreksi nominal pengeluaran")
                    }
                )
            }
        } else {
            // Restore old kas balance
            supabase.postgrest.rpc(
                "update_saldo_kas", buildJsonObject {
                    put("p_id_kas", old.idKas)
                    put("p_perubahan_saldo", old.total)
                    put("p_keterangan", "Koreksi kas pengeluaran (kembali)")
                }
            )
            // Deduct from new kas balance
            supabase.postgrest.rpc(
                "update_saldo_kas", buildJsonObject {
                    put("p_id_kas", pengeluaran.idKas)
                    put("p_perubahan_saldo", -pengeluaran.total)
                    put("p_keterangan", "Pengeluaran: ${pengeluaran.deskripsi}")
                }
            )
        }
    }

    suspend fun deletePengeluaran(id: String) {
        // 1. Fetch old record to know its kas and total
        val old = supabase.postgrest["pengeluaran"].select {
            filter {
                eq("id_pengeluaran", id)
            }
        }.decodeSingle<Pengeluaran>()

        if (old.status != "batal") {
            // 2. Update status to 'batal'
            supabase.postgrest["pengeluaran"].update(buildJsonObject {
                put("status", "batal")
            }) {
                filter {
                    eq("id_pengeluaran", id)
                }
            }

            // 3. Restore the kas balance
            supabase.postgrest.rpc(
                "update_saldo_kas", buildJsonObject {
                    put("p_id_kas", old.idKas)
                    put("p_perubahan_saldo", old.total)
                    put("p_keterangan", "Pembatalan Pengeluaran: ${old.deskripsi ?: ""}")
                }
            )
        }
    }
}