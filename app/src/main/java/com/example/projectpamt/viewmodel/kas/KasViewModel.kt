package com.example.projectpamt.viewmodel.kas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Kas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class KasViewModel : ViewModel() {
    private val _kasList = MutableStateFlow<List<Kas>>(Kas.dummyList)
    
    private val _uiState = MutableStateFlow<KasUiState>(KasUiState.Idle)
    val uiState: StateFlow<KasUiState> = _uiState.asStateFlow()

    init {
        fetchAllActiveKas()
    }

    fun fetchAllActiveKas() {
        viewModelScope.launch {
            _uiState.value = KasUiState.Loading
            try {
                // Menampilkan seluruh kas (aktif maupun non-aktif) untuk keperluan UI
                _uiState.value = KasUiState.Success(_kasList.value)
            } catch (e: Exception) {
                _uiState.value = KasUiState.Error(e.message ?: "Gagal menampilkan data kas")
            }
        }
    }

    fun addKas(nama: String, saldo: Double) {
        viewModelScope.launch {
            _uiState.value = KasUiState.Loading
            try {
                val newId = (_kasList.value.mapNotNull { it.idKas?.toIntOrNull() }.maxOrNull() ?: 0) + 1
                val kasBaru = Kas(
                    idKas = newId.toString(),
                    nama = nama,
                    saldo = saldo,
                    aktif = true,
                    updatedAtText = "Baru saja ditambahkan"
                )
                _kasList.value = _kasList.value + kasBaru
                fetchAllActiveKas()
            } catch (e: Exception) {
                _uiState.value = KasUiState.Error(e.message ?: "Gagal menambahkan kas baru")
            }
        }
    }

    fun updateNamaKas(id: String, nama: String) {
        viewModelScope.launch {
            _uiState.value = KasUiState.Loading
            try {
                _kasList.value = _kasList.value.map { kas ->
                    if (kas.idKas == id) {
                        kas.copy(nama = nama, updatedAtText = "Baru saja diubah")
                    } else {
                        kas
                    }
                }
                fetchAllActiveKas()
            } catch (e: Exception) {
                _uiState.value = KasUiState.Error(e.message ?: "Gagal mengubah nama kas")
            }
        }
    }

    fun updateSaldo(id: String, saldo: Double, keterangan: String) {
        viewModelScope.launch {
            _uiState.value = KasUiState.Loading
            try {
                _kasList.value = _kasList.value.map { kas ->
                    if (kas.idKas == id) {
                        kas.copy(
                            saldo = kas.saldo + saldo,
                            updatedAtText = "Baru saja diupdate"
                        )
                    } else {
                        kas
                    }
                }
                fetchAllActiveKas()
            } catch (e: Exception) {
                _uiState.value = KasUiState.Error(e.message ?: "Gagal merubah saldo kas")
            }
        }
    }

    fun nonaktifkanKas(id: String) {
        viewModelScope.launch {
            _uiState.value = KasUiState.Loading
            try {
                _kasList.value = _kasList.value.map { kas ->
                    if (kas.idKas == id) {
                        kas.copy(aktif = false, updatedAtText = "Baru saja dinonaktifkan")
                    } else {
                        kas
                    }
                }
                fetchAllActiveKas()
            } catch (e: Exception) {
                _uiState.value = KasUiState.Error(e.message ?: "Gagal menonaktifkan kas")
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