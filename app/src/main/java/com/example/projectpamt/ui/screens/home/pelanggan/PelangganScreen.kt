package com.example.projectpamt.ui.screens.home.pelanggan

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
import com.example.projectpamt.data.model.Pelanggan
import com.example.projectpamt.ui.components.CustomerCard
import com.example.projectpamt.ui.navigation.AktivitasPelanggan
import com.example.projectpamt.ui.navigation.EditPelanggan
import com.example.projectpamt.ui.navigation.TambahPelanggan
import com.example.projectpamt.ui.theme.BackgroundSlate
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.TextDark
import com.example.projectpamt.viewmodel.pelanggan.PelangganUiState
import com.example.projectpamt.viewmodel.pelanggan.PelangganViewModel

@Composable
fun PelangganScreen(
    modifier: Modifier = Modifier,
    viewModel: PelangganViewModel = viewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    // Memuat data pelanggan saat pertama kali masuk screen
    LaunchedEffect(Unit) {
        viewModel.fetchPelanggan()
    }

    PelangganContent(
        modifier = modifier,
        uiState = uiState,
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        onAddCustomerClick = { navController.navigate(TambahPelanggan) },
        onActivityClick = { navController.navigate(AktivitasPelanggan(it)) },
        onEditClick = { navController.navigate(EditPelanggan(it)) }
    )
}

@Composable
private fun PelangganContent(
    modifier: Modifier = Modifier,
    uiState: PelangganUiState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onAddCustomerClick: () -> Unit,
    onActivityClick: (Pelanggan) -> Unit,
    onEditClick: (Pelanggan) -> Unit
) {
    // Hitung total pelanggan dan total pelanggan aktif
    val totalPelangganCount = when (uiState) {
        is PelangganUiState.Success -> uiState.data.size
        else -> 0
    }
    val aktifPelangganCount = when (uiState) {
        is PelangganUiState.Success -> uiState.data.count { it.aktif }
        else -> 0
    }

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
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Title Area (Tanpa tombol Kembali)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Pelanggan",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Kelola profil pengguna",
                            fontSize = 14.sp,
                            color = Color(0xFFDBEAFE)
                        )
                    }

                    // Ikon Pelanggan
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .border(BorderStroke(2.dp, Color.White.copy(alpha = 0.3f)), CircleShape)
                            .clickable { /* TODO: Aksi pelanggan */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.pelanggan),
                            contentDescription = "Pelanggan",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Bento Stats Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Card Total Pelanggan
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                            .padding(17.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Total Pelanggan",
                            fontSize = 14.sp,
                            color = Color(0xFFDBEAFE)
                        )
                        Text(
                            text = totalPelangganCount.toString(),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Card Pelanggan Aktif
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                            .padding(17.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Aktif",
                            fontSize = 14.sp,
                            color = Color(0xFFDBEAFE)
                        )
                        Text(
                            text = aktifPelangganCount.toString(),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
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
                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp)),
                    placeholder = {
                        Text("Cari pelanggan...", color = Color(0xFF9CA3AF), fontSize = 16.sp)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.search),
                            contentDescription = null,
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
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
                        .clip(RoundedCornerShape(12.dp))
                        .background(GreenPrimary)
                        .clickable { onAddCustomerClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.add),
                        contentDescription = "Tambah",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // ── 3. CUSTOMER LIST SECTION ─────────────────────────────────────────
        item {
            when (uiState) {
                is PelangganUiState.Loading -> {
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = GreenPrimary)
                    }
                }

                is PelangganUiState.Error -> {
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

                is PelangganUiState.Success -> {
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
                                text = "Pelanggan tidak ditemukan",
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
                            filteredList.forEach { pelanggan ->
                                CustomerCard(
                                    pelanggan = pelanggan,
                                    onActivityClick = { onActivityClick(pelanggan) },
                                    onEditClick = { onEditClick(pelanggan) }
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
