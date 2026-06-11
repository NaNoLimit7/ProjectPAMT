package com.example.projectpamt.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlin.time.Instant

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Produk(
    @SerialName("id_produk") val idProduk: String? = null,
    val nama: String,
    val harga: Double,
    val stok: Double,
    @SerialName("nama_satuan") val namaSatuan: String,
    @SerialName("detail_produk") val detailProduk: JsonElement? = null,
    val aktif: Boolean = true,
    @SerialName("created_at") val createdAt: String? = null
) {
    companion object {
        val ProdukNavType = object : NavType<Produk>(isNullableAllowed = false) {
            override fun get(bundle: Bundle, key: String): Produk? {
                return bundle.getString(key)?.let { Json.decodeFromString(it) }
            }
            override fun parseValue(value: String): Produk {
                return Json.decodeFromString(Uri.decode(value))
            }
            override fun put(bundle: Bundle, key: String, value: Produk) {
                bundle.putString(key, Json.encodeToString(value))
            }
            override fun serializeAsValue(value: Produk): String {
                return Uri.encode(Json.encodeToString(value))
            }
        }
    }
}