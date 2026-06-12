package com.example.projectpamt.viewmodel.kategori

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Kategori
import com.example.projectpamt.data.repository.KategoriRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class KategoriViewModel(
    private val repository: KategoriRepository = KategoriRepository()
) : ViewModel() {
    private val _kategoriList = MutableStateFlow(defaultCategories)
    private val _uiState = MutableStateFlow<KategoriUiState>(KategoriUiState.Idle)
    val uiState: StateFlow<KategoriUiState> = _uiState.asStateFlow()

    init {
        fetchKategori()
    }

    fun fetchKategori() {
        viewModelScope.launch {
            _uiState.value = KategoriUiState.Loading
            try {
                _uiState.value = KategoriUiState.Success(_kategoriList.value)
            } catch (e: Exception) {
                _uiState.value = KategoriUiState.Error("Gagal memuat data kategori ${e.message}")
            }
        }
    }

    fun addKategori(name: String) {
        viewModelScope.launch {
            _uiState.value = KategoriUiState.Loading
            try {
                kotlinx.coroutines.delay(300)
                val newId = (_kategoriList.value.mapNotNull { it.idKategori?.toIntOrNull() }.maxOrNull() ?: 0) + 1
                val kategoriBaru = Kategori(idKategori = newId.toString(), name = name)
                _kategoriList.value += kategoriBaru
                fetchKategori()
            } catch (e: Exception) {
                _uiState.value = KategoriUiState.Error("Gagal menambahkan kategori ${e.message}")
            }
        }
    }

    fun updateKategori(id: String, name: String) {
        viewModelScope.launch {
            _uiState.value = KategoriUiState.Loading
            try {
                _kategoriList.value = _kategoriList.value.map { kategori ->
                    if (kategori.idKategori == id) {
                        kategori.copy(name = name)
                    } else {
                        kategori
                    }
                }
                fetchKategori()
            } catch (e: Exception) {
                _uiState.value = KategoriUiState.Error("Gagal memperbarui kategori ${e.message}")
            }
        }
    }

    fun deleteKategori(id: String) {
        viewModelScope.launch {
            _uiState.value = KategoriUiState.Loading
            try {
                _kategoriList.value = _kategoriList.value.filter { it.idKategori != id }
                fetchKategori()
            } catch (e: Exception) {
                _uiState.value = KategoriUiState.Error("Gagal menghapus kategori ${e.message}")
            }
        }
    }

    fun clearUiState() {
        if (_uiState.value is KategoriUiState.Error) {
            _uiState.value = KategoriUiState.Idle
            fetchKategori()
        }
    }

    companion object {
        val defaultCategories = listOf(
            Kategori(idKategori = "1", name = "Makanan"),
            Kategori(idKategori = "2", name = "Minuman"),
            Kategori(idKategori = "3", name = "Aksesoris"),
            Kategori(idKategori = "4", name = "Elektronik"),
            Kategori(idKategori = "5", name = "Jasa")
        )
    }
}