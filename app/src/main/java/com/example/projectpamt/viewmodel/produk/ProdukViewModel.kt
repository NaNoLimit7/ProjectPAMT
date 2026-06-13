package com.example.projectpamt.viewmodel.produk

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Produk
import com.example.projectpamt.data.repository.ProdukRepository
import com.example.projectpamt.ui.utils.toAppError
import com.example.projectpamt.ui.utils.toUserMessage
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

    var isRefreshing by mutableStateOf(false)
        private set

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
                _uiState.value = ProdukUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing = true
            try {
                val list = repository.getProdukAktif()
                _uiState.value = ProdukUiState.Success(list)
            } catch (e: Exception) {
                _uiState.value = ProdukUiState.Error(e.toAppError().toUserMessage())
            } finally {
                isRefreshing = false
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
                _uiState.value = ProdukUiState.Error(e.toAppError().toUserMessage())
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
                _uiState.value = ProdukUiState.Error(e.toAppError().toUserMessage())
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
                _uiState.value = ProdukUiState.Error(e.toAppError().toUserMessage())
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
                _uiState.value = ProdukUiState.Error(e.toAppError().toUserMessage())
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
                _uiState.value = ProdukUiState.Error(e.toAppError().toUserMessage())
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