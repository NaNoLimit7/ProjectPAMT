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
data class DetailProduk(val produk: Produk)

@Serializable
data class EditPelanggan(val pelanggan: Pelanggan)

@Serializable
data class AktivitasPelanggan(val pelanggan: Pelanggan)

@Serializable
object TambahPelanggan

@Serializable
object TambahProduk

@Serializable
data class ProsesPembayaran(
    val pelanggan: Pelanggan,
    val cartItemsJson: String,
    val totalHarga: Double
)

@Serializable
data class InfoPembayaranBerhasil(
    val idTransaksi: String,
    val totalPembayaran: Double,
    val kembalian: Double,
    val tanggalWaktu: String
)

@Serializable
object RiwayatPenjualan

@Serializable
object LogInventoryList

@Serializable
object LogTotalKas