package com.example.projectpamt.viewmodel.produk

import android.util.Log
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
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

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
        detailProdukBase: JsonObject,
        imageBytes: ByteArray? = null,
        imageMimeType: String = "image/jpeg",
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.value = ProdukUiState.Loading
            try {
                // 1. Upload gambar ke Storage jika ada, dapatkan public URL
                val imageUrl = if (imageBytes != null) {
                    val fileName = "produk_${System.currentTimeMillis()}.jpg"
                    repository.uploadGambarProduk(fileName, imageBytes, imageMimeType)
                } else null

                // 2. Gabungkan image_url ke detailProduk JSON
                val detailFinal: JsonObject = buildJsonObject {
                    detailProdukBase.forEach { (k, v) -> put(k, v) }
                    if (imageUrl != null) put("image_url", imageUrl)
                }

                val produkBaru = Produk(
                    nama = nama,
                    harga = harga,
                    stok = stok,
                    namaSatuan = namaSatuan,
                    detailProduk = detailFinal,
                    aktif = true
                )
                repository.tambahProduk(produkBaru)
                fetchProdukAktif()
                onSuccess()
            } catch (e: Exception) {
                Log.d("PRODUK", e.message.toString())
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
        detailProdukBase: JsonObject,
        imageBytes: ByteArray? = null,       // null = gambar tidak diganti
        imageMimeType: String = "image/jpeg",
        existingImageUrl: String? = null,     // URL gambar lama dari database
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.value = ProdukUiState.Loading
            try {
                // 1. Upload gambar baru jika ada, atau gunakan URL lama
                val imageUrl = if (imageBytes != null) {
                    val fileName = "produk_${System.currentTimeMillis()}.jpg"
                    repository.uploadGambarProduk(fileName, imageBytes, imageMimeType)
                } else existingImageUrl

                // 2. Gabungkan image_url ke detailProduk JSON
                val detailFinal: JsonObject = buildJsonObject {
                    detailProdukBase.forEach { (k, v) -> put(k, v) }
                    if (imageUrl != null) put("image_url", imageUrl)
                }

                val produkBaru = Produk(
                    idProduk = id,
                    nama = nama,
                    harga = harga,
                    stok = stok,
                    namaSatuan = namaSatuan,
                    detailProduk = detailFinal,
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
}