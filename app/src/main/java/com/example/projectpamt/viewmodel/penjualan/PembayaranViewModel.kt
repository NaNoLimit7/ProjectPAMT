package com.example.projectpamt.viewmodel.penjualan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.Kas
import com.example.projectpamt.data.model.DetailPenjualan
import com.example.projectpamt.data.repository.KasRepository
import com.example.projectpamt.data.repository.PenjualanRepository
import com.example.projectpamt.ui.utils.toAppError
import com.example.projectpamt.ui.utils.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

class PembayaranViewModel(
    private val kasRepository: KasRepository = KasRepository(),
    private val penjualanRepository: PenjualanRepository = PenjualanRepository()
) : ViewModel() {
    private val _kasList = MutableStateFlow<List<Kas>>(emptyList())
    val kasList: StateFlow<List<Kas>> = _kasList.asStateFlow()

    private val _selectedKas = MutableStateFlow<Kas?>(null)
    val selectedKas: StateFlow<Kas?> = _selectedKas.asStateFlow()

    private val _penerimaanKas = MutableStateFlow("")
    val penerimaanKas: StateFlow<String> = _penerimaanKas.asStateFlow()

    private val _uiState = MutableStateFlow<PembayaranUiState>(PembayaranUiState.Idle)
    val uiState: StateFlow<PembayaranUiState> = _uiState.asStateFlow()

    var isRefreshing by mutableStateOf(false)
        private set

    init {
        fetchActiveKas()
    }

    fun fetchActiveKas() {
        viewModelScope.launch {
            try {
                val list = kasRepository.getKasAktif()
                _kasList.value = list
                if (_selectedKas.value == null) {
                    _selectedKas.value = list.firstOrNull()
                }
            } catch (e: Exception) {
                // Ignore or handle
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing = true
            try {
                val list = kasRepository.getKasAktif()
                _kasList.value = list
                if (_selectedKas.value == null) {
                    _selectedKas.value = list.firstOrNull()
                }
            } catch (e: Exception) {
                // Ignore
            } finally {
                isRefreshing = false
            }
        }
    }

    fun selectKas(kas: Kas) {
        _selectedKas.value = kas
    }

    fun onPenerimaanKasChange(value: String) {
        // Only allow numeric input (digits)
        val cleanValue = value.replace(Regex("[^0-9]"), "")
        _penerimaanKas.value = cleanValue
    }

    fun prosesPembayaran(
        pelangganId: String,
        cartItems: List<CartItem>,
        totalHarga: Double,
        onSuccess: () -> Unit
    ) {
        val kas = _selectedKas.value
        if (kas == null) {
            _uiState.value = PembayaranUiState.Error("Silakan pilih akun kas terlebih dahulu")
            return
        }

        val penerimaan = _penerimaanKas.value.toDoubleOrNull() ?: 0.0
        if (penerimaan < totalHarga) {
            _uiState.value = PembayaranUiState.Error("Penerimaan kas kurang dari total pembayaran")
            return
        }

        viewModelScope.launch {
            _uiState.value = PembayaranUiState.Loading
            try {
                val items = cartItems.map { item ->
                    DetailPenjualan(
                        idProduk = item.produk.idProduk!!,
                        kuantitas = item.quantity.toDouble(),
                        hargaSatuan = item.produk.harga
                    )
                }

                val detailPenjualanJson = Json.encodeToJsonElement(cartItems)

                val txnId = penjualanRepository.prosesPenjualan(
                    idPelanggan = pelangganId,
                    idKas = kas.idKas!!,
                    jumlahBayar = penerimaan,
                    totalHarga = totalHarga,
                    items = items,
                    detailPenjualan = detailPenjualanJson
                )
                
                _uiState.value = PembayaranUiState.Success(txnId)
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = PembayaranUiState.Error(e.toAppError().toUserMessage())
            }
        }
    }

    fun resetState() {
        _uiState.value = PembayaranUiState.Idle
    }
}
