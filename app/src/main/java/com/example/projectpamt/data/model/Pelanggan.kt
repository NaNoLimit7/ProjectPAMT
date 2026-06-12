package com.example.projectpamt.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Pelanggan(
    @SerialName("id_pelanggan") val idPelanggan: String? = null,
    val nama: String,
    val telepon: String,
    val aktif: Boolean = true,
    @SerialName("created_at") val createdAt: String? = null
) {
    companion object {
        val PelangganNavType = object : NavType<Pelanggan>(isNullableAllowed = false) {
            override fun get(bundle: Bundle, key: String): Pelanggan? {
                return bundle.getString(key)?.let { Json.decodeFromString(it) }
            }
            override fun parseValue(value: String): Pelanggan {
                return Json.decodeFromString(Uri.decode(value))
            }
            override fun put(bundle: Bundle, key: String, value: Pelanggan) {
                bundle.putString(key, Json.encodeToString(value))
            }
            override fun serializeAsValue(value: Pelanggan): String {
                return Uri.encode(Json.encodeToString(value))
            }
        }

        val dummyList = listOf(
            Pelanggan(
                idPelanggan = "1",
                nama = "Sarah Johnson",
                telepon = "082374638283",
                aktif = true
            ),
            Pelanggan(
                idPelanggan = "2",
                nama = "Michael Chen",
                telepon = "082374639325",
                aktif = true
            ),
            Pelanggan(
                idPelanggan = "3",
                nama = "Emily Rodriguez",
                telepon = "082103843432",
                aktif = true
            ),
            Pelanggan(
                idPelanggan = "4",
                nama = "David Kim",
                telepon = "083283642849",
                aktif = false
            )
        )
    }
}
