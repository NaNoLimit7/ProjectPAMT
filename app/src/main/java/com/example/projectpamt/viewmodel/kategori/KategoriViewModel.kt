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
                val newId = java.util.UUID.randomUUID().toString()
                val kategoriBaru = Kategori(idKategori = newId, name = name)
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
            Kategori(idKategori = "4bfa0525-455b-419b-8d16-6512eb2d4ee7", name = "Makanan"),
            Kategori(idKategori = "9d3d3ef1-5a5c-4d51-a982-f5492d3b24f5", name = "Minuman"),
            Kategori(idKategori = "8c68ebfa-ec5c-4a37-b4d2-b6ab0c99f9de", name = "Aksesoris"),
            Kategori(idKategori = "3b08e5e8-132d-45df-bb2f-6825ec3a2417", name = "Elektronik"),
            Kategori(idKategori = "5602b255-f3d1-4339-86ff-3da58c0437be", name = "Jasa")
        )
    }
}