package com.example.projectpamt.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LogKasItem(
    @SerialName("id_log_kas") val idLogKas: String? = null,
    @SerialName("id_kas") val idKas: String,
    @SerialName("saldo_awal") val saldoAwal: Double,
    @SerialName("saldo_akhir") val saldoAkhir: Double,
    val keterangan: String,
    @SerialName("updated_at") val updatedAt: String
) {
    // Derived properties for UI presentation
    val tipeAktivitas: String
        get() {
            return when {
                keterangan.contains("pembuatan", ignoreCase = true) || keterangan.contains("dibuat", ignoreCase = true) -> "Pembuatan Akun"
                keterangan.contains("penyesuaian", ignoreCase = true) || keterangan.contains("disesuaikan", ignoreCase = true) -> "Saldo disesuaikan oleh Admin"
                keterangan.contains("non-aktif", ignoreCase = true) || keterangan.contains("tidak aktif", ignoreCase = true) -> "Status berubah menjadi Tidak Aktif"
                keterangan.contains("aktif", ignoreCase = true) -> "Status berubah menjadi Aktif"
                else -> "Aktivitas Kas"
            }
        }
        
    val pelaku: String
        get() {
            val match = Regex("oleh:\\s*([^;\\n]+)", RegexOption.IGNORE_CASE).find(keterangan)
            return match?.groupValues?.get(1)?.trim() ?: "Admin Sistem"
        }

    val detailKeterangan: String
        get() {
            // Remove "oleh: ..." suffix or similar if present
            return keterangan.replace(Regex(";\\s*oleh:\\s*([^\\n]+)", RegexOption.IGNORE_CASE), "").trim()
        }
}
