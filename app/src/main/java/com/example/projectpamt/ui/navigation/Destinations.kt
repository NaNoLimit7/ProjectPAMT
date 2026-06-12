package com.example.projectpamt.ui.navigation

import com.example.projectpamt.data.model.Pelanggan
import com.example.projectpamt.data.model.Produk
import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Register

// Main Bottom Nav Routes
@Serializable
object Dashboard

@Serializable
object PenjualanList

@Serializable
object ProdukList

@Serializable
object PelangganList

@Serializable
object KasList

@Serializable
object PengeluaranList

@Serializable
object KategoriList

// Rute dengan parameter menggunakan NavType custom (sesuai tahap 3)
@Serializable
data class EditProduk(val produk: Produk)

@Serializable
data class EditPelanggan(val pelanggan: Pelanggan)

@Serializable
object TambahPelanggan

@Serializable
object ProsesPembayaran