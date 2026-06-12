package com.example.projectpamt.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.time.Instant

@Serializable
data class Kas(
    // Nullable saat insert baru
    @SerialName("id_kas") val idKas: String? = null,
    val nama: String,
    val saldo: Double,
    val aktif: Boolean = true,
    @SerialName("created_at") val createdAt: String? = null,
    val updatedAtText: String? = null
) {
    companion object {
        val KasNavType = object : NavType<Kas>(isNullableAllowed = false) {
            override fun get(bundle: Bundle, key: String): Kas? {
                return bundle.getString(key)?.let { Json.decodeFromString(it) }
            }
            override fun parseValue(value: String): Kas {
                return Json.decodeFromString(Uri.decode(value))
            }
            override fun put(bundle: Bundle, key: String, value: Kas) {
                bundle.putString(key, Json.encodeToString(value))
            }
            override fun serializeAsValue(value: Kas): String {
                return Uri.encode(Json.encodeToString(value))
            }
        }

        val dummyList = listOf(
            Kas(
                idKas = "1",
                nama = "Kas Utama",
                saldo = 15234.50,
                aktif = true,
                updatedAtText = "Diperbarui 2 jam yang lalu"
            ),
            Kas(
                idKas = "2",
                nama = "Kas Laci 1",
                saldo = 28450.00,
                aktif = true,
                updatedAtText = "Diperbarui 5 menit yang lalu"
            ),
            Kas(
                idKas = "3",
                nama = "Kas Gudang",
                saldo = 2205.75,
                aktif = true,
                updatedAtText = "Diperbarui 2 jam yang lalu"
            ),
            Kas(
                idKas = "4",
                nama = "Kas Lama",
                saldo = 0.0,
                aktif = false,
                updatedAtText = "Diperbarui 30 hari yang lalu"
            )
        )
    }
}
