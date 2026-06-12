package com.example.projectpamt.ui.screens.home.produk

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.projectpamt.data.model.Produk
import com.example.projectpamt.ui.components.InventoryProductCard
import com.example.projectpamt.ui.theme.BackgroundSlate
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.TextDark
import com.example.projectpamt.ui.navigation.DetailProduk
import com.example.projectpamt.ui.navigation.EditProduk
import com.example.projectpamt.ui.navigation.TambahProduk
import com.example.projectpamt.viewmodel.produk.ProdukUiState
import com.example.projectpamt.viewmodel.produk.ProdukViewModel

@Composable
fun ProdukScreen(
    modifier: Modifier = Modifier,
    viewModel: ProdukViewModel = viewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    // Memuat data produk aktif saat pertama kali masuk screen
    LaunchedEffect(Unit) {
        viewModel.fetchProdukAktif()
    }

    ProdukContent(
        modifier = modifier,
        uiState = uiState,
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        onAddProductClick = { navController.navigate(TambahProduk) },
        onDetailClick = { produk -> navController.navigate(DetailProduk(produk)) },
        onEditClick = { produk -> navController.navigate(EditProduk(produk)) }
    )
}

@Composable
private fun ProdukContent(
    modifier: Modifier = Modifier,
    uiState: ProdukUiState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onAddProductClick: () -> Unit,
    onDetailClick: (Produk) -> Unit,
    onEditClick: (Produk) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // ── 1. HEADER SECTION ────────────────────────────────────────────────
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = GreenPrimary,
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Title Area
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Produk",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = (-0.75).sp
                        )
                        Text(
                            text = "Kelola inventori dan stok",
                            fontSize = 14.sp,
                            color = Color(0xFFDBEAFE)
                        )
                    }

                    // Ikon Keranjang Belanja/Tas
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(BorderStroke(2.dp, Color.White.copy(alpha = 0.3f)), CircleShape)
                            .clickable { /* TODO: Aksi keranjang/bag */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.produk_bag),
                            contentDescription = "Bag",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // ── 2. SEARCH & ADD SECTION ──────────────────────────────────────────
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search Input
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp)),
                    placeholder = {
                        Text("Cari produk...", color = Color(0xFF9CA3AF), fontSize = 16.sp)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.search),
                            contentDescription = null,
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(15.dp)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = TextDark,
                        unfocusedTextColor = TextDark
                    )
                )

                // Add Button (+)
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(GreenPrimary)
                        .clickable { onAddProductClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.add),
                        contentDescription = "Tambah",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }

        // ── 3. PRODUCT LIST SECTION ──────────────────────────────────────────
        item {
            when (uiState) {
                is ProdukUiState.Loading -> {
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = GreenPrimary)
                    }
                }

                is ProdukUiState.Error -> {
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.message,
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    }
                }

                is ProdukUiState.Success -> {
                    val filteredList = uiState.data.filter {
                        it.nama.contains(searchQuery, ignoreCase = true)
                    }

                    if (filteredList.isEmpty()) {
                        Box(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Produk tidak ditemukan",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            filteredList.forEach { produk ->
                                InventoryProductCard(
                                    produk = produk,
                                    onDetailClick = { onDetailClick(produk) },
                                    onEditClick = { onEditClick(produk) }
                                )
                            }
                        }
                    }
                }

                else -> {}
            }
        }
    }
}
