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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import com.example.projectpamt.R
import com.example.projectpamt.data.model.Pelanggan
import com.example.projectpamt.data.model.Produk
import com.example.projectpamt.ui.components.AppNavigationBar
import com.example.projectpamt.ui.components.ProductCard
import com.example.projectpamt.ui.navigation.ProsesPembayaran
import com.example.projectpamt.ui.navigation.TambahPelanggan
import com.example.projectpamt.ui.navigation.RiwayatPenjualan
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.ProjectPAMTTheme
import com.example.projectpamt.ui.utils.formatRupiah
import com.example.projectpamt.viewmodel.penjualan.CartItem
import com.example.projectpamt.viewmodel.penjualan.PenjualanDataUiState
import com.example.projectpamt.viewmodel.penjualan.PenjualanViewModel

import androidx.compose.runtime.LaunchedEffect
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// ─── Screen (stateful) ──────────────────────────────────────────────────────

@Composable
fun PenjualanScreen(
    modifier: Modifier = Modifier,
    viewModel: PenjualanViewModel = viewModel(),
    navController: NavController,
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

    LaunchedEffect(clearCart) {
        if (clearCart == true) {
            viewModel.clearCart()
            navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>("clear_cart")
        }
    }

    PenjualanContent(
        modifier = modifier,
        dataState = uiState,
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
                Toast.makeText(context, "Silakan pilih pelanggan terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        },
        onHistoryClick = { navController.navigate(RiwayatPenjualan) }
    )
}

// ─── Content (stateless) ────────────────────────────────────────────────────

@Composable
private fun PenjualanContent(
    modifier: Modifier = Modifier,
    dataState: PenjualanDataUiState,
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
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = if (cartItems.isNotEmpty()) 88.dp else 24.dp)
        ) {
            // ── HEADER ────────────────────────────────────────────────────
            item {
                PenjualanHeader(
                    totalTransaksi = cartItems.sumOf { it.totalHarga }.toInt(),
                    cartItemCount = cartItems.sumOf { it.quantity },
                    onHistoryClick = onHistoryClick
                )
            }

            // ── CUSTOMER + CART SECTION ───────────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Pilih Pelanggan label
                    Text(
                        text = "Pilih Pelanggan",
                        fontSize = 14.sp,
                        color = Color(0xFF334155)
                    )

                    // Dropdown Pelanggan
                    if (dataState is PenjualanDataUiState.Success) {
                        CustomerDropdown(
                            pelangganList = dataState.pelangganList,
                            selectedPelanggan = selectedPelanggan,
                            onSelect = onSelectPelanggan
                        )
                    }

                    // Tambah pelanggan cepat
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

            // ── KERANJANG SECTION ─────────────────────────────────────────
            if (cartItems.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(top = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Header keranjang
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

                        // Cart items
                        cartItems.forEach { item ->
                            CartItemRow(
                                item = item,
                                onDelete = { onRemoveFromCart(item.produk) },
                                onDecrement = { onUpdateQuantity(item.produk, item.quantity - 1) },
                                onIncrement = { onUpdateQuantity(item.produk, item.quantity + 1) }
                            )
                        }
                    }
                }
            }

            // ── PRODUK SECTION HEADER + SEARCH ────────────────────────────
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

                    // Search bar
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

            // ── PRODUCT GRID ──────────────────────────────────────────────
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

                        // LazyVerticalGrid tidak bisa di dalam LazyColumn,
                        // jadi kita render grid secara manual dalam rows
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
                                    // Jika row ganjil, isi dengan spacer
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

        // ── FIXED BOTTOM CART BAR ─────────────────────────────────────────
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

// ─── Header ─────────────────────────────────────────────────────────────────

@Composable
private fun PenjualanHeader(
    totalTransaksi: Int,
    cartItemCount: Int,
    onHistoryClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = GreenPrimary,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Title row
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

        // Total balance card
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
                    text = formatRupiah(totalTransaksi.toDouble()),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )
            }
            // Item count badge
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

// ─── Customer Dropdown ───────────────────────────────────────────────────────

@Composable
private fun CustomerDropdown(
    pelangganList: List<Pelanggan>,
    selectedPelanggan: Pelanggan?,
    onSelect: (Pelanggan?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFEDF6F0))
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedPelanggan?.nama ?: "Cari atau pilih pelanggan...",
                color = if (selectedPelanggan != null) Color(0xFF1E293B) else Color(0xFF94A3B8),
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color(0xFF64748B)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Tanpa Pelanggan", color = Color.Gray) },
                onClick = { onSelect(null); expanded = false }
            )
            pelangganList.forEach { pelanggan ->
                DropdownMenuItem(
                    text = { Text(pelanggan.nama) },
                    onClick = { onSelect(pelanggan); expanded = false }
                )
            }
        }
    }
}

// ─── Cart Item Row ────────────────────────────────────────────────────────────

@Composable
private fun CartItemRow(
    item: CartItem,
    onDelete: () -> Unit,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Product image thumbnail
            Box(
                modifier = Modifier
                    .size(width = 56.dp, height = 61.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(
                    model = item.produk.imageUrl,
                    contentDescription = item.produk.nama,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            // Name + price
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.produk.nama,
                    fontSize = 14.sp,
                    color = Color(0xFF1E293B),
                    maxLines = 2
                )
                Text(
                    text = formatRupiah(item.totalHarga),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreenPrimary
                )

                // Quantity stepper
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEDF6F0))
                        .padding(horizontal = 2.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onDecrement() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "−",
                            fontSize = 14.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "${item.quantity}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onIncrement() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "+",
                            fontSize = 14.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Delete icon
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.delete),
                    contentDescription = "Hapus",
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// ─── Bottom Cart Bar ──────────────────────────────────────────────────────────

@Composable
private fun CartBottomBar(
    cartItems: List<CartItem>,
    onProcessClick: () -> Unit
) {
    val totalHarga = cartItems.sumOf { it.totalHarga }
    val totalItems = cartItems.sumOf { it.quantity }

    Snackbar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onProcessClick() },
        shape = RoundedCornerShape(12.dp),
        containerColor = GreenPrimary,
        contentColor = Color.White,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bagian Kiri (Ikon Keranjang dan Total)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Keranjang",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = "$totalItems Item",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = formatRupiah(totalHarga),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Bagian Kanan (Tombol Proses)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Proses",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PenjualanScreenPreview() {
    // Sample data for preview
    val sampleProduk = Produk.dummyList
    val samplePelanggan = listOf(
        Pelanggan(idPelanggan = "p1", nama = "Budi", telepon = "08123456789"),
        Pelanggan(idPelanggan = "p2", nama = "Siti", telepon = "08234567890")
    )

    val sampleCart = listOf(
        CartItem(sampleProduk[0], quantity = 2),
        CartItem(sampleProduk[2], quantity = 1)
    )

    val total = sampleCart.sumOf { it.totalHarga }.toInt()
    val dataStatePreview = PenjualanDataUiState.Success(
        totalTransaksi = total,
        pelangganList = samplePelanggan,
        produkList = sampleProduk
    )

    ProjectPAMTTheme(dynamicColor = false) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0), // ← tambahkan ini
            bottomBar = {
                AppNavigationBar(
                    navController = rememberNavController(),
                    currentDestination = null
                )
            }
        ) { innerPadding ->
            PenjualanContent(
                modifier = Modifier.padding(innerPadding),
                dataState = dataStatePreview,
                searchQuery = "",
                cartItems = sampleCart,
                selectedPelanggan = samplePelanggan.firstOrNull(),
                onSearchQueryChange = {},
                onSelectPelanggan = {},
                onAddToCart = {},
                onRemoveFromCart = {},
                onUpdateQuantity = { _, _ -> },
                onClearCart = {},
                onAddPelangganClick = {},
                onProcessPaymentClick = {},
                onHistoryClick = {}
            )
        }
    }
}