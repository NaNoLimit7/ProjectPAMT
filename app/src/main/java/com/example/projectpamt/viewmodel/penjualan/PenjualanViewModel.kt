package com.example.projectpamt.viewmodel.penjualan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpamt.data.model.DetailPenjualan
import com.example.projectpamt.data.model.Pelanggan
import com.example.projectpamt.data.model.Produk
import com.example.projectpamt.data.repository.PelangganRepository
import com.example.projectpamt.data.repository.PenjualanRepository
import com.example.projectpamt.data.repository.ProdukRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement

class PenjualanViewModel(
    private val repository: PenjualanRepository = PenjualanRepository(),
    private val produkRepository: ProdukRepository = ProdukRepository(),
    private val pelangganRepository: PelangganRepository = PelangganRepository()
) : ViewModel() {

    // Action State (Save Transaction)
    private val _uiState = MutableStateFlow<PenjualanUiState>(PenjualanUiState.Idle)
    val uiState: StateFlow<PenjualanUiState> = _uiState.asStateFlow()

    // Data Load State
    private val _dataState = MutableStateFlow<PenjualanDataUiState>(PenjualanDataUiState.Idle)
    val dataState: StateFlow<PenjualanDataUiState> = _dataState.asStateFlow()

    // Interactive States
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _selectedPelanggan = MutableStateFlow<Pelanggan?>(null)
    val selectedPelanggan: StateFlow<Pelanggan?> = _selectedPelanggan.asStateFlow()

    init {
        fetchPenjualanData()
    }

    fun fetchPenjualanData() {
        viewModelScope.launch {
            _dataState.value = PenjualanDataUiState.Loading
            try {
                val totalTransaksi = repository.getTotalTransaksi()
                val pelangganList = pelangganRepository.getAllPelanggan().filter { it.aktif }
                val produkList = Produk.dummyList
                _dataState.value = PenjualanDataUiState.Success(
                    totalTransaksi = totalTransaksi,
                    pelangganList = pelangganList,
                    produkList = produkList
                )
            } catch (e: Exception) {
                _dataState.value = PenjualanDataUiState.Error("Gagal memuat data: ${e.message}")
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun selectPelanggan(pelanggan: Pelanggan?) {
        _selectedPelanggan.value = pelanggan
    }

    fun addToCart(produk: Produk) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItemIndex = currentItems.indexOfFirst { it.produk.idProduk == produk.idProduk }
        if (existingItemIndex != -1) {
            val item = currentItems[existingItemIndex]
            currentItems[existingItemIndex] = item.copy(quantity = item.quantity + 1)
        } else {
            currentItems.add(CartItem(produk, 1))
        }
        _cartItems.value = currentItems
    }

    fun removeFromCart(produk: Produk) {
        val currentItems = _cartItems.value.toMutableList()
        currentItems.removeAll { it.produk.idProduk == produk.idProduk }
        _cartItems.value = currentItems
    }

    fun updateCartQuantity(produk: Produk, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(produk)
            return
        }
        val currentItems = _cartItems.value.toMutableList()
        val existingItemIndex = currentItems.indexOfFirst { it.produk.idProduk == produk.idProduk }
        if (existingItemIndex != -1) {
            val item = currentItems[existingItemIndex]
            currentItems[existingItemIndex] = item.copy(quantity = quantity)
            _cartItems.value = currentItems
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun runProsesPenjualan(
        idPelanggan: String,
        idKas: String,
        jumlahBayar: Double,
        totalHarga: Double,
        items: List<DetailPenjualan>,
        detailPenjualan: JsonElement?) {
        viewModelScope.launch {
            _uiState.value = PenjualanUiState.Loading
            try {
                val idBaru = repository.prosesPenjualan(
                    idPelanggan,
                    idKas,
                    jumlahBayar,
                    totalHarga,
                    items,
                    detailPenjualan
                )
                _uiState.value = PenjualanUiState.Success(idBaru)
                clearCart() // optional: clear cart after success
            } catch (e: Exception) {
                _uiState.value = PenjualanUiState.Error("Gagal memproses penjualan ${e.message}")
            }
        }
    }

    fun clearUiState() {
        _uiState.value = PenjualanUiState.Idle
    }
}