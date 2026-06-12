package com.example.projectpamt.viewmodel.produk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Produk
import com.example.projectpamt.data.repository.ProdukRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement
import kotlin.time.Duration.Companion.milliseconds

class ProdukViewModel(
    private val repository: ProdukRepository = ProdukRepository()
) : ViewModel() {

    private val _produkList = MutableStateFlow(Produk.dummyList)
    private val _uiState = MutableStateFlow<ProdukUiState>(ProdukUiState.Idle)
    val uiState: StateFlow<ProdukUiState> = _uiState.asStateFlow()

    init {
        fetchProdukAktif()
    }

    fun fetchProdukAktif() {
        viewModelScope.launch {
            _uiState.value = ProdukUiState.Loading
            try {
                // Menggunakan data dummy offline in-memory untuk UI sementara waktu
                _uiState.value = ProdukUiState.Success(_produkList.value)
            } catch (e: Exception) {
                _uiState.value = ProdukUiState.Error(e.message ?: "Gagal menampilkan produk")
            }
        }
    }

    fun addProduk(
        nama: String,
        harga: Double,
        stok: Double,
        namaSatuan: String,
        detailProduk: JsonElement?,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.value = ProdukUiState.Loading
            try {
                // Simulasikan delay network agar loading terlihat nyata
                delay(500.milliseconds)
                val newId = (_produkList.value.mapNotNull { it.idProduk?.toIntOrNull() }.maxOrNull() ?: 0) + 1
                val produkBaru = Produk(
                    idProduk = newId.toString(),
                    nama = nama,
                    harga = harga,
                    stok = stok,
                    namaSatuan = namaSatuan,
                    detailProduk = detailProduk,
                    aktif = true
                )
                _produkList.value += produkBaru
                fetchProdukAktif()
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = ProdukUiState.Error(e.message ?: "Gagal menambahkan produk")
            }
        }
    }

    fun updateInfoProduk(id: String, nama: String, harga: Double, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = ProdukUiState.Loading
            try {
                _produkList.value = _produkList.value.map { produk ->
                    if (produk.idProduk == id) {
                        produk.copy(nama = nama, harga = harga)
                    } else {
                        produk
                    }
                }
                fetchProdukAktif()
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = ProdukUiState.Error(e.message ?: "Gagal mengubah info Produk")
            }
        }
    }

    fun updateStok(id: String, stok: Double, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = ProdukUiState.Loading
            try {
                _produkList.value = _produkList.value.map { produk ->
                    if (produk.idProduk == id) {
                        produk.copy(stok = stok)
                    } else {
                        produk
                    }
                }
                fetchProdukAktif()
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = ProdukUiState.Error(e.message ?: "Gagal memperbarui stok")
            }
        }
    }

    fun nonaktifkanProduk(id: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = ProdukUiState.Loading
            try {
                _produkList.value = _produkList.value.map { produk ->
                    if (produk.idProduk == id) {
                        produk.copy(aktif = false)
                    } else {
                        produk
                    }
                }
                fetchProdukAktif()
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = ProdukUiState.Error(e.message ?: "Gagal menonaktifkan produk")
            }
        }
    }

    fun clearUiState() {
        if (_uiState.value is ProdukUiState.Error) {
            _uiState.value = ProdukUiState.Idle
            fetchProdukAktif()
        }
    }
}