package com.example.projectpamt.data.model

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

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
    val imageUrl: String?
        get() = detailProduk?.jsonObject?.get("image_url")?.jsonPrimitive?.contentOrNull

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

        val dummyList = listOf(
            Produk(
                idProduk = "1",
                nama = "HP 150 Wireless Mouse",
                harga = 110_000.0,
                stok = 45.0,
                namaSatuan = "pcs",
                aktif = true,
                detailProduk = buildJsonObject {
                    put("image_url", "https://picsum.photos/seed/mouse/400/300")
                }
            ),
            Produk(
                idProduk = "2",
                nama = "Kabel USB Type-C 1m",
                harga = 24_000.0,
                stok = 120.0,
                namaSatuan = "pcs",
                aktif = true,
                detailProduk = kotlinx.serialization.json.buildJsonObject {
                    put("image_url", "https://picsum.photos/seed/cable/400/300")
                }
            ),
            Produk(
                idProduk = "3",
                nama = "EarPods Lightning",
                harga = 320_000.0,
                stok = 8.0,  // sengaja < 10 → badge SISA
                namaSatuan = "pcs",
                aktif = true,
                detailProduk = kotlinx.serialization.json.buildJsonObject {
                    put("image_url", "https://picsum.photos/seed/earpods/400/300")
                }
            ),
            Produk(
                idProduk = "4",
                nama = "Apple Macbook Pro A1990",
                harga = 18_500_000.0,
                stok = 3.0,  // sengaja < 10 → badge SISA
                namaSatuan = "unit",
                aktif = true,
                detailProduk = kotlinx.serialization.json.buildJsonObject {
                    put("image_url", "https://picsum.photos/seed/macbook/400/300")
                }
            ),
            Produk(
                idProduk = "5",
                nama = "SSD External 1TB Samsung T7",
                harga = 1_250_000.0,
                stok = 22.0,
                namaSatuan = "pcs",
                aktif = true,
                detailProduk = kotlinx.serialization.json.buildJsonObject {
                    put("image_url", "https://picsum.photos/seed/ssd/400/300")
                }
            ),
            Produk(
                idProduk = "6",
                nama = "Keyboard Mechanical Rexus",
                harga = 650_000.0,
                stok = 15.0,
                namaSatuan = "pcs",
                aktif = true,
                detailProduk = null  // tanpa gambar → tampil placeholder
            ),
        )
    }
}