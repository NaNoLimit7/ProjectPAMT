package com.example.projectpamt.data.repository

import com.example.projectpamt.data.SupabaseClientProvider
import com.example.projectpamt.data.model.LogKas
import com.example.projectpamt.data.model.LogKasItem
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.delay

class LogKasRepository {
    private val supabase = SupabaseClientProvider.client

    suspend fun getAllLogKas(): List<LogKas> {
        return supabase.postgrest["log_kas"].select().decodeList<LogKas>()
    }

    suspend fun getLogKas(idKas: String): List<LogKasItem> {
        // Simulate network delay
        delay(500)

        // Return mock logs matching Figma design
        return listOf(
            LogKasItem(
                idLogKas = "log-4",
                idKas = idKas,
                saldoAwal = 15000000.0,
                saldoAkhir = 15000000.0,
                keterangan = "Audit selesai. Akun kembali aktif dan siap digunakan untuk transaksi kasir; oleh: Admin Sistem",
                updatedAt = "2026-10-16T07:00:00Z"
            ),
            LogKasItem(
                idLogKas = "log-3",
                idKas = idKas,
                saldoAwal = 15000000.0,
                saldoAkhir = 15000000.0,
                keterangan = "Akun dinonaktifkan sementara untuk audit mingguan oleh departemen keuangan; oleh: Siti Aminah (Finance)",
                updatedAt = "2026-10-15T18:00:00Z"
            ),
            LogKasItem(
                idLogKas = "log-2",
                idKas = idKas,
                saldoAwal = 10000000.0,
                saldoAkhir = 15000000.0,
                keterangan = "Penyesuaian saldo sebesar Rp 5.000.000 dilakukan untuk sinkronisasi fisik; oleh: Budi Santoso (Supervisor)",
                updatedAt = "2026-10-14T10:15:00Z"
            ),
            LogKasItem(
                idLogKas = "log-1",
                idKas = idKas,
                saldoAwal = 0.0,
                saldoAkhir = 10000000.0,
                keterangan = "Akun kas baru \"Kas Utama\" telah berhasil didaftarkan dalam sistem; oleh: Admin Sistem",
                updatedAt = "2026-10-12T08:30:00Z"
            )
        )
    }
}