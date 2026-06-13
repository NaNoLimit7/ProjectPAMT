package com.example.projectpamt.ui.screens.home.penjualan

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projectpamt.R
import com.example.projectpamt.data.model.Pelanggan
import com.example.projectpamt.data.model.Produk
import com.example.projectpamt.ui.navigation.ProsesPembayaran
import com.example.projectpamt.ui.navigation.RiwayatPenjualan
import com.example.projectpamt.ui.navigation.TambahPelanggan
import com.example.projectpamt.ui.screens.home.penjualan.components.CartBottomBar
import com.example.projectpamt.ui.screens.home.penjualan.components.CartItemRow
import com.example.projectpamt.ui.screens.home.penjualan.components.CustomerDropdown
import com.example.projectpamt.ui.screens.home.penjualan.components.ProductCard
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.utils.formatRupiah
import com.example.projectpamt.viewmodel.penjualan.uistate.CartItem
import com.example.projectpamt.viewmodel.penjualan.uistate.PenjualanDataUiState
import com.example.projectpamt.viewmodel.penjualan.PenjualanViewModel
import kotlinx.serialization.json.Json


import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PenjualanScreen(
    modifier: Modifier = Modifier,
    viewModel: PenjualanViewModel = viewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val uiState by viewModel.dataState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val selectedPelanggan by viewModel.selectedPelanggan.collectAsStateWithLifecycle()

    val context = androidx.compose.ui.platform.LocalContext.current

    val clearCart by navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<Boolean?>("clear_cart", null)
        ?.collectAsStateWithLifecycle() ?: remember { mutableStateOf(null) }

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    // Auto-refresh when returning from add/edit screens
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val needRefresh by savedStateHandle?.getStateFlow("need_refresh", false)?.collectAsStateWithLifecycle() ?: remember { mutableStateOf(false) }

    LaunchedEffect(needRefresh) {
        if (needRefresh) {
            viewModel.refresh()
            savedStateHandle?.set("need_refresh", false)
        }
    }

    LaunchedEffect(clearCart) {
        if (clearCart == true) {
            viewModel.clearCart()
            navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>("clear_cart")
        }
    }

    PenjualanContent(
        modifier = modifier,
        dataState = uiState,
        isRefreshing = viewModel.isRefreshing,
        onRefresh = viewModel::refresh,
        searchQuery = searchQuery,
        cartItems = cartItems,
        selectedPelanggan = selectedPelanggan,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onSelectPelanggan = viewModel::selectPelanggan,
        onAddToCart = viewModel::addToCart,
        onRemoveFromCart = viewModel::removeFromCart,
        onUpdateQuantity = viewModel::updateCartQuantity,
        onClearCart = viewModel::clearCart,
        onAddPelangganClick = { navController.navigate(TambahPelanggan) },
        onProcessPaymentClick = {
            if (selectedPelanggan != null) {
                val cartItemsJson = Json.encodeToString(cartItems)
                val totalHarga = cartItems.sumOf { it.totalHarga }
                navController.navigate(
                    ProsesPembayaran(
                        pelanggan = selectedPelanggan!!,
                        cartItemsJson = cartItemsJson,
                        totalHarga = totalHarga
                    )
                )
            } else {
                Toast.makeText(
                    context,
                    "Silakan pilih pelanggan terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        onHistoryClick = { navController.navigate(RiwayatPenjualan) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PenjualanContent(
    modifier: Modifier = Modifier,
    dataState: PenjualanDataUiState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    searchQuery: String,
    cartItems: List<CartItem>,
    selectedPelanggan: Pelanggan?,
    onSearchQueryChange: (String) -> Unit,
    onSelectPelanggan: (Pelanggan?) -> Unit,
    onAddToCart: (Produk) -> Unit,
    onRemoveFromCart: (Produk) -> Unit,
    onUpdateQuantity: (Produk, Int) -> Unit,
    onClearCart: () -> Unit,
    onAddPelangganClick: () -> Unit,
    onProcessPaymentClick: () -> Unit,
    onHistoryClick: () -> Unit,
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = GreenPrimary,)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Penjualan",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Sistem Transaksi Cepat",
                            fontSize = 14.sp,
                            color = Color(0xFFBFDBFE)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0x8A7CE2BD))
                            .clickable { onHistoryClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.history),
                            contentDescription = "Riwayat Penjualan",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }


            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(bottom = if (cartItems.isNotEmpty()) 88.dp else 24.dp)
            ) {

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = GreenPrimary,
                                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                            )
                            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.1f))
                                .padding(20.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Transaksi Saat Ini",
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = formatRupiah(cartItems.sumOf { it.totalHarga }),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    letterSpacing = (-0.5).sp
                                )
                            }

                            val cartItemCount = cartItems.sumOf { it.quantity }
                            if (cartItemCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .clip(RoundedCornerShape(50))
                                        .background(Color.White.copy(alpha = 0.5f))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "$cartItemCount item",
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }


                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(top = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Pilih Pelanggan",
                            fontSize = 14.sp,
                            color = Color(0xFF334155)
                        )

                        if (dataState is PenjualanDataUiState.Success) {
                            CustomerDropdown(
                                pelangganList = dataState.pelangganList,
                                selectedPelanggan = selectedPelanggan,
                                onSelect = onSelectPelanggan
                            )
                        }

                        Row(
                            modifier = Modifier
                                .align(Alignment.End)
                                .clickable { onAddPelangganClick() },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.add_with_circle),
                                contentDescription = null,
                                tint = GreenPrimary,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "Tambah pelanggan cepat",
                                fontSize = 12.sp,
                                color = GreenPrimary
                            )
                        }
                    }
                }


                if (cartItems.isNotEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .padding(top = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Keranjang",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                                Text(
                                    text = "Hapus Semua",
                                    fontSize = 14.sp,
                                    color = Color(0xFFEF4444),
                                    modifier = Modifier.clickable { onClearCart() }
                                )
                            }

                            cartItems.forEach { item ->
                                CartItemRow(
                                    item = item,
                                    onDelete = { onRemoveFromCart(item.produk) },
                                    onDecrement = {
                                        onUpdateQuantity(
                                            item.produk,
                                            item.quantity - 1
                                        )
                                    },
                                    onIncrement = {
                                        onUpdateQuantity(
                                            item.produk,
                                            item.quantity + 1
                                        )
                                    }
                                )
                            }
                        }
                    }
                }


                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(top = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Produk",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )

                        TextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text("Cari Produk", color = Color(0xFF6B7280), fontSize = 14.sp)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = Color(0xFF6B7280),
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFEDF6F0),
                                unfocusedContainerColor = Color(0xFFEDF6F0),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = Color(0xFF1E293B),
                                unfocusedTextColor = Color(0xFF1E293B)
                            )
                        )
                    }
                }


                item {
                    when (dataState) {
                        is PenjualanDataUiState.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Memuat produk...", color = Color.Gray)
                            }
                        }

                        is PenjualanDataUiState.Error -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(dataState.message, color = Color.Red, fontSize = 14.sp)
                            }
                        }

                        is PenjualanDataUiState.Success -> {
                            val filtered = dataState.produkList.filter {
                                it.nama.contains(searchQuery, ignoreCase = true)
                            }

                            val rows = filtered.chunked(2)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                rows.forEach { rowItems ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        rowItems.forEach { produk ->
                                            ProductCard(
                                                produk = produk,
                                                modifier = Modifier.weight(1f),
                                                onAddClick = { onAddToCart(produk) }
                                            )
                                        }
                                        if (rowItems.size == 1) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        else -> {}
                    }
                }
            }
        }


        AnimatedVisibility(
            visible = cartItems.isNotEmpty(),
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            CartBottomBar(
                cartItems = cartItems,
                onProcessClick = onProcessPaymentClick
            )
        }
    }
}
}