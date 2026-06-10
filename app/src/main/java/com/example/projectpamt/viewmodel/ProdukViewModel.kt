package com.example.projectpamt.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Produk
import com.example.projectpamt.data.repository.ProdukRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement

class ProdukViewModel : ViewModel() {
    private val repository = ProdukRepository()

    var produkList by mutableStateOf<List<Produk>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchProdukAktif()
    }

    fun fetchProdukAktif() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
              produkList = repository.getProdukAktif()
            } catch (e: Exception) {
                errorMessage = "Gagal menampilkan produk ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun addProduk(
        nama: String,
        harga: Double,
        stok: Double,
        namaSatuan: String,
        detailProduk: JsonElement?) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val produkBaru = Produk(
                    nama = nama,
                    harga = harga,
                    stok = stok,
                    namaSatuan = namaSatuan,
                    detailProduk = detailProduk
                )
                repository.tambahProduk(produkBaru)

                fetchProdukAktif()
            } catch (e: Exception) {
                errorMessage = "Gagal menambahkan produk ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun updateInfoProduk(id: String, nama: String, harga: Double) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                repository.updateInfoProduk(
                    idProduk = id,
                    namaBaru = nama,
                    hargaBaru = harga
                )

                fetchProdukAktif()
            } catch (e: Exception) {
                errorMessage = "Gagal mengubah info Produk ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun updateStok(id: String, stok: Double) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                repository.updateStok(idProduk = id, perubahanStok = stok)

                fetchProdukAktif()
            } catch (e: Exception) {
                errorMessage = "Gagal memperbarui stok ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun nonaktifkanProduk(id: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                repository.nonaktifkanProduk(idProduk = id)

                fetchProdukAktif()
            } catch (e: Exception) {
                errorMessage = "Gagal menonaktifkan produk ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }
}