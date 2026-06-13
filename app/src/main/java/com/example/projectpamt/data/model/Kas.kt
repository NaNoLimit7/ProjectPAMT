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

        // Dummy list dihapus
    }
}
