package com.example.projectpamt.ui.screens.home.pelanggan

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projectpamt.data.model.Pelanggan
import com.example.projectpamt.ui.screens.home.pelanggan.components.FilterChipsRow
import com.example.projectpamt.ui.screens.home.pelanggan.components.SummarySection
import com.example.projectpamt.ui.screens.home.pelanggan.components.TransactionListCard
import com.example.projectpamt.ui.theme.BackgroundSlate
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.viewmodel.pelanggan.AktivitasFilter
import com.example.projectpamt.viewmodel.pelanggan.AktivitasPelangganUiState
import com.example.projectpamt.viewmodel.pelanggan.AktivitasPelangganViewModel

import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AktivitasPelangganScreen(
    pelanggan: Pelanggan,
    modifier: Modifier = Modifier,
    viewModel: AktivitasPelangganViewModel = viewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Load data aktivitas setiap kali screen dibuka untuk pelanggan ini
    LaunchedEffect(pelanggan.idPelanggan) {
        pelanggan.idPelanggan?.let { id ->
            viewModel.loadAktivitas(id)
        }
    }

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    AktivitasPelangganContent(
        pelanggan = pelanggan,
        uiState = uiState,
        isRefreshing = viewModel.isRefreshing,
        onRefresh = viewModel::refresh,
        modifier = modifier,
        onBackClick = { navController.popBackStack() },
        onFilterSelected = { filter -> viewModel.setFilter(filter) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AktivitasPelangganContent(
    pelanggan: Pelanggan,
    uiState: AktivitasPelangganUiState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onFilterSelected: (AktivitasFilter) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        // ── 1. HEADER SECTION (Fixed) ────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    spotColor = Color(0x1A000000),
                    ambientColor = Color(0x1A000000)
                )
                .background(
                    color = GreenPrimary,
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Back Button
            Row(
                modifier = Modifier
                    .clickable { onBackClick() }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Kembali",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            // Title Area
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Aktivitas Pelanggan",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = pelanggan.nama,
                    fontSize = 14.sp,
                    color = Color(0xFFDBEAFE),
                    fontWeight = FontWeight.Normal
                )
            }
        }

        // ── 2. STATE HANDLING & DYNAMIC BODY (Scrollable) ───────────────────
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
            when (uiState) {
                is AktivitasPelangganUiState.Loading -> {
                    item {
                        Box(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = GreenPrimary)
                        }
                    }
                }

                is AktivitasPelangganUiState.Error -> {
                    item {
                        Box(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = uiState.message,
                                color = Color(0xFFBA1A1A),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                is AktivitasPelangganUiState.Success -> {
                    // Summary Cards
                    item {
                        SummarySection(summary = uiState.summary)
                    }

                    // Filters Chips Row
                    item {
                        FilterChipsRow(
                            selectedFilter = uiState.selectedFilter,
                            onFilterSelected = onFilterSelected
                        )
                    }

                    // Transaction List Card
                    item {
                        TransactionListCard(
                            listAktivitas = uiState.listAktivitas,
                            modifier = modifier
                        )
                    }
                }

                else -> {}
            }
        }
    }
}
}

