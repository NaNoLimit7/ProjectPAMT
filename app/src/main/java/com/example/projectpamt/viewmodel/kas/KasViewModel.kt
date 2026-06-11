package com.example.projectpamt.viewmodel.kas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Kas
import com.example.projectpamt.data.repository.KasRepository
import kotlinx.coroutines.launch

class KasViewModel : ViewModel() {
    private val repository = KasRepository()

    var kasList by mutableStateOf<List<Kas>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchAllActiveKas()
    }

    fun fetchAllActiveKas() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                kasList = repository.getKasAktif()
            } catch (e: Exception) {
                errorMessage = "Gagal menampilkan kas aktif ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun addKas(nama: String, saldo: Double) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val kasBaru = Kas(nama = nama, saldo = saldo)
                repository.tambahKas(kasBaru)

                fetchAllActiveKas()
            } catch (e: Exception) {
                errorMessage = "Gagal menambahkan kas baru ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun updateNamaKas(id: String, nama: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                repository.updateNamaKas(id, nama)

                fetchAllActiveKas()
            } catch (e: Exception) {
                errorMessage = "Gagal mengubah nama kas ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun updateSaldo(id: String, saldo: Double, keterangan: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                repository.updateSaldo(
                    idKas = id,
                    perubahanSaldo = saldo,
                    keterangan = keterangan
                )

                fetchAllActiveKas()
            } catch (e: Exception) {
                errorMessage = "Gagal merubah saldo kas ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun nonaktifkanKas(id: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                repository.nonaktifkanKas(idKas = id)

                fetchAllActiveKas()
            } catch (e: Exception) {
                errorMessage = "Gagal menonaktifkan kas ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }
}