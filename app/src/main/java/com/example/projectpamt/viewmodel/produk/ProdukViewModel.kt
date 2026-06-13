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

    private val _uiState = MutableStateFlow<ProdukUiState>(ProdukUiState.Idle)
    val uiState: StateFlow<ProdukUiState> = _uiState.asStateFlow()

    init {
        fetchProdukAktif()
    }

    fun fetchProdukAktif() {
        viewModelScope.launch {
            _uiState.value = ProdukUiState.Loading
            try {
                val list = repository.getProdukAktif()
                _uiState.value = ProdukUiState.Success(list)
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
                val produkBaru = Produk(
                    nama = nama,
                    harga = harga,
                    stok = stok,
                    namaSatuan = namaSatuan,
                    detailProduk = detailProduk,
                    aktif = true
                )
                repository.tambahProduk(produkBaru)
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
                repository.updateInfoProduk(id, nama, harga)
                fetchProdukAktif()
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = ProdukUiState.Error(e.message ?: "Gagal mengubah info Produk")
            }
        }
    }

    fun updateProduk(
        id: String,
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
                val produkBaru = Produk(
                    idProduk = id,
                    nama = nama,
                    harga = harga,
                    stok = stok,
                    namaSatuan = namaSatuan,
                    detailProduk = detailProduk,
                    aktif = true
                )
                repository.updateProduk(id, produkBaru)
                fetchProdukAktif()
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = ProdukUiState.Error(e.message ?: "Gagal memperbarui produk")
            }
        }
    }

    fun updateStok(id: String, stok: Double, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = ProdukUiState.Loading
            try {
                repository.setStok(id, stok)
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
                repository.nonaktifkanProduk(id)
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