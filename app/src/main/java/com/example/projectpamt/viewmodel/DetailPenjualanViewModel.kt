package com.example.projectpamt.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.DetailPenjualan
import com.example.projectpamt.data.repository.DetailPenjualanRepository
import kotlinx.coroutines.launch

class DetailPenjualanViewModel : ViewModel() {
    private val repository = DetailPenjualanRepository()

    var detailPenjualanList by mutableStateOf<List<DetailPenjualan>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchDetailPenjualan()
    }

    fun fetchDetailPenjualan() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                detailPenjualanList = repository.getAllDetailPenjualan()
            } catch (e: Exception) {
                errorMessage = "Gagal menampilkan data detail penjualan ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun addDetailPenjualan(idPenjualan: String, idProduk: String, kuantitas: Double, hargaSatuan: Double){
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val detailPenjualanBaru = DetailPenjualan(
                    idPenjualan = idPenjualan,
                    idProduk = idProduk,
                    kuantitas = kuantitas,
                    hargaSatuan = hargaSatuan
                )
                repository.insertDetailPenjualan(detailPenjualanBaru)

                fetchDetailPenjualan()
            } catch (e: Exception) {
                errorMessage = "Gagal menambahkan data detail penjualan ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }
}