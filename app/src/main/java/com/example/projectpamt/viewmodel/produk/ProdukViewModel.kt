package com.example.projectpamt.viewmodel.produk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Produk
import com.example.projectpamt.data.repository.ProdukRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement

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
                val result = repository.getProdukAktif()
                _uiState.value = ProdukUiState.Success(result)
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
        detailProduk: JsonElement?
    ) {
        viewModelScope.launch {
            _uiState.value = ProdukUiState.Loading
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
                _uiState.value = ProdukUiState.Error(e.message ?: "Gagal menambahkan produk")
            }
        }
    }

    fun updateInfoProduk(id: String, nama: String, harga: Double) {
        viewModelScope.launch {
            _uiState.value = ProdukUiState.Loading
            try {
                repository.updateInfoProduk(
                    idProduk = id,
                    namaBaru = nama,
                    hargaBaru = harga
                )
                fetchProdukAktif()
            } catch (e: Exception) {
                _uiState.value = ProdukUiState.Error(e.message ?: "Gagal mengubah info Produk")
            }
        }
    }

    fun updateStok(id: String, stok: Double) {
        viewModelScope.launch {
            _uiState.value = ProdukUiState.Loading
            try {
                repository.updateStok(idProduk = id, perubahanStok = stok)
                fetchProdukAktif()
            } catch (e: Exception) {
                _uiState.value = ProdukUiState.Error(e.message ?: "Gagal memperbarui stok")
            }
        }
    }

    fun nonaktifkanProduk(id: String) {
        viewModelScope.launch {
            _uiState.value = ProdukUiState.Loading
            try {
                repository.nonaktifkanProduk(idProduk = id)
                fetchProdukAktif()
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