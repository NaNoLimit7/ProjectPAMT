package com.example.projectpamt.viewmodel.kas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Kas
import com.example.projectpamt.data.repository.KasRepository
import com.example.projectpamt.ui.utils.toAppError
import com.example.projectpamt.ui.utils.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class KasViewModel : ViewModel() {
    private val repository = KasRepository()
    private val _kasList = MutableStateFlow<List<Kas>>(emptyList())
    
    private val _uiState = MutableStateFlow<KasUiState>(KasUiState.Idle)
    val uiState: StateFlow<KasUiState> = _uiState.asStateFlow()

    var isRefreshing by mutableStateOf(false)
        private set

    init {
        fetchAllActiveKas()
    }

    fun fetchAllActiveKas() {
        viewModelScope.launch {
            _uiState.value = KasUiState.Loading
            try {
                _kasList.value = repository.getAllKas()
                _uiState.value = KasUiState.Success(_kasList.value)
            } catch (e: Exception) {
                _uiState.value = KasUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing = true
            try {
                _kasList.value = repository.getAllKas()
                _uiState.value = KasUiState.Success(_kasList.value)
            } catch (e: Exception) {
                _uiState.value = KasUiState.Error(e.toAppError().toUserMessage())
            } finally {
                isRefreshing = false
            }
        }
    }

    fun addKas(nama: String, saldo: Double, keterangan: String, aktif: Boolean, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = KasUiState.Loading
            try {
                repository.tambahKas(nama, saldo, aktif, keterangan)
                fetchAllActiveKas()
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = KasUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun updateNamaKas(id: String, nama: String) {
        viewModelScope.launch {
            _uiState.value = KasUiState.Loading
            try {
                val existing = _kasList.value.find { it.idKas == id }
                if (existing != null) {
                    repository.updateKasDanCatatLog(id, nama, existing.aktif, "Mengubah nama kas menjadi $nama")
                    fetchAllActiveKas()
                } else {
                    _uiState.value = KasUiState.Error("Kas tidak ditemukan")
                }
            } catch (e: Exception) {
                _uiState.value = KasUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun updateSaldo(id: String, saldo: Double, keterangan: String) {
        viewModelScope.launch {
            _uiState.value = KasUiState.Loading
            try {
                repository.updateSaldo(id, saldo, keterangan)
                fetchAllActiveKas()
            } catch (e: Exception) {
                _uiState.value = KasUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun nonaktifkanKas(id: String) {
        viewModelScope.launch {
            _uiState.value = KasUiState.Loading
            try {
                repository.nonaktifkanKas(id)
                fetchAllActiveKas()
            } catch (e: Exception) {
                _uiState.value = KasUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun updateKas(id: String, nama: String, aktif: Boolean, keteranganUser: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = KasUiState.Loading
            try {
                repository.updateKasDanCatatLog(id, nama, aktif, keteranganUser)
                fetchAllActiveKas()
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = KasUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun softDeleteKas(id: String, keteranganUser: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = KasUiState.Loading
            try {
                val existing = _kasList.value.find { it.idKas == id }
                if (existing != null) {
                    repository.updateKasDanCatatLog(id, existing.nama, false, keteranganUser)
                    fetchAllActiveKas()
                    onSuccess()
                } else {
                    _uiState.value = KasUiState.Error("Kas tidak ditemukan")
                }
            } catch (e: Exception) {
                _uiState.value = KasUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun clearUiState() {
        if (_uiState.value is KasUiState.Error) {
            _uiState.value = KasUiState.Idle
            fetchAllActiveKas()
        }
    }
}