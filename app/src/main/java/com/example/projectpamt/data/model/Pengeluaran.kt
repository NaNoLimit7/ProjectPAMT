package com.example.projectpamt.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Pengeluaran(
    @SerialName("id_pengeluaran") val idPengeluaran: String? = null,
    @SerialName("id_kategori") val idKategori: String,
    @SerialName("id_kas") val idKas: String,
    val deskripsi: String? = null,
    val total: Double,
    val status: String = "aktif",
    @SerialName("created_at") val createdAt: String? = null,
    
    // Relationship mappings (for UI and Supabase joins)
    val kategori: Kategori? = null,
    val kas: Kas? = null
) {
    companion object {
        val PengeluaranNavType = object : NavType<Pengeluaran>(isNullableAllowed = false) {
            override fun get(bundle: Bundle, key: String): Pengeluaran? {
                return bundle.getString(key)?.let { Json.decodeFromString(it) }
            }
            override fun parseValue(value: String): Pengeluaran {
                return Json.decodeFromString(Uri.decode(value))
            }
            override fun put(bundle: Bundle, key: String, value: Pengeluaran) {
                bundle.putString(key, Json.encodeToString(value))
            }
            override fun serializeAsValue(value: Pengeluaran): String {
                return Uri.encode(Json.encodeToString(value))
            }
        }
    }
}