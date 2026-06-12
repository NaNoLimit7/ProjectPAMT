package com.example.projectpamt.ui.screens.home.pelanggan

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.example.projectpamt.ui.theme.BackgroundSlate
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.TextDark
import com.example.projectpamt.viewmodel.pelanggan.AktivitasFilter
import com.example.projectpamt.viewmodel.pelanggan.AktivitasPelangganUiState
import com.example.projectpamt.viewmodel.pelanggan.AktivitasPelangganViewModel
import com.example.projectpamt.viewmodel.pelanggan.AktivitasSummary
import com.example.projectpamt.viewmodel.pelanggan.AktivitasType
import com.example.projectpamt.viewmodel.pelanggan.PelangganAktivitas
import com.example.projectpamt.ui.utils.formatRupiah

@Composable
fun AktivitasPelangganScreen(
    pelanggan: Pelanggan,
    modifier: Modifier = Modifier,
    viewModel: AktivitasPelangganViewModel = viewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Load data aktivitas setiap kali screen dibuka untuk pelanggan ini
    LaunchedEffect(pelanggan.idPelanggan) {
        pelanggan.idPelanggan?.let { id ->
            viewModel.loadAktivitas(id)
        }
    }

    AktivitasPelangganContent(
        pelanggan = pelanggan,
        uiState = uiState,
        modifier = modifier,
        onBackClick = { navController.popBackStack() },
        onFilterSelected = { filter -> viewModel.setFilter(filter) }
    )
}

@Composable
private fun AktivitasPelangganContent(
    pelanggan: Pelanggan,
    uiState: AktivitasPelangganUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onFilterSelected: (AktivitasFilter) -> Unit
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
        }

        // ── 2. STATE HANDLING & DYNAMIC BODY ─────────────────────────────────
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

@Composable
private fun SummarySection(
    summary: AktivitasSummary,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // CARD 1: Total Belanja (Full Width)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 20.dp,
                    spotColor = Color(0x0D1E2430),
                    ambientColor = Color(0x0D1E2430)
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0x4DBECABE))
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // Background overlay decoration
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .align(Alignment.TopEnd)
                        .background(
                            color = Color(0xFF007A45).copy(alpha = 0.05f),
                            shape = RoundedCornerShape(bottomStart = 96.dp)
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(17.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.total_belanja),
                            contentDescription = null,
                            tint = Color(0xFF3E4940),
                            modifier = Modifier.size(19.dp)
                        )
                        Text(
                            text = "Total Belanja",
                            fontSize = 12.sp,
                            color = Color(0xFF3E4940),
                            fontWeight = FontWeight.Normal
                        )
                    }

                    Text(
                        text = formatRupiah(summary.totalBelanja),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF181D18)
                    )
                }
            }
        }

        // Row containing Transaksi & Terakhir Aktif
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // CARD 2: Jumlah Transaksi
            Card(
                modifier = Modifier
                    .weight(1f)
                    .shadow(
                        elevation = 20.dp,
                        spotColor = Color(0x0D1E2430),
                        ambientColor = Color(0x0D1E2430)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0x4DBECABE))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(17.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.transaksi_pelanggan),
                            contentDescription = null,
                            tint = Color(0xFF3E4940),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Transaksi",
                            fontSize = 12.sp,
                            color = Color(0xFF3E4940),
                            fontWeight = FontWeight.Normal
                        )
                    }

                    Text(
                        text = summary.totalTransaksi.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF181D18)
                    )
                }
            }

            // CARD 3: Terakhir Aktif
            Card(
                modifier = Modifier
                    .weight(1f)
                    .shadow(
                        elevation = 20.dp,
                        spotColor = Color(0x0D1E2430),
                        ambientColor = Color(0x0D1E2430)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0x4DBECABE))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(17.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.update_time),
                            contentDescription = null,
                            tint = Color(0xFF3E4940),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Terakhir Aktif",
                            fontSize = 12.sp,
                            color = Color(0xFF3E4940),
                            fontWeight = FontWeight.Normal
                        )
                    }

                    Text(
                        text = summary.terakhirAktif,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF181D18)
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterChipsRow(
    selectedFilter: AktivitasFilter,
    onFilterSelected: (AktivitasFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AktivitasFilter.entries.forEach { filter ->
            val isSelected = filter == selectedFilter
            val label = when (filter) {
                AktivitasFilter.SEMUA_WAKTU -> "Semua Waktu"
                AktivitasFilter.BULAN_INI -> "Bulan Ini"
                AktivitasFilter.TIGA_BULAN_TERAKHIR -> "3 Bulan Terakhir"
                AktivitasFilter.TAHUN_INI -> "Tahun Ini"
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .background(if (isSelected) GreenPrimary else Color.White)
                    .then(
                        if (isSelected) Modifier else Modifier.border(
                            1.dp,
                            Color(0xFF6E7A70),
                            RoundedCornerShape(9999.dp)
                        )
                    )
                    .clickable { onFilterSelected(filter) }
                    .padding(horizontal = 16.dp, vertical = 9.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = if (isSelected) Color.White else Color(0xFF3E4940),
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun TransactionListCard(
    listAktivitas: List<PelangganAktivitas>,
    modifier: Modifier = Modifier
) {
    var showAll by remember(listAktivitas) { androidx.compose.runtime.mutableStateOf(false) }
    val displayedList = if (showAll) listAktivitas else listAktivitas.take(4)
    val hasMoreThanFour = listAktivitas.size > 4

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .shadow(
                elevation = 20.dp,
                spotColor = Color(0x0D1E2430),
                ambientColor = Color(0x0D1E2430)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0x4DBECABE))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        ) {
            // Card Title Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        border = BorderStroke(1.dp, Color(0x1ABECABE)),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Riwayat Transaksi",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF181D18),
                )
            }

            // List Items Section
            if (displayedList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada transaksi di periode ini",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                displayedList.forEachIndexed { index, aktivitas ->
                    if (index > 0) {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0x33BECABE),
                            thickness = 1.dp
                        )
                    }

                    TransactionItemRow(aktivitas = aktivitas)
                }
            }

            // Footer Link (Pagination / Show More)
            if (hasMoreThanFour) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0x33BECABE),
                    thickness = 1.dp
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showAll = !showAll }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (showAll) "Sembunyikan Transaksi" else "Lihat Semua Transaksi",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF007A45)
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionItemRow(
    aktivitas: PelangganAktivitas,
    modifier: Modifier = Modifier
) {
    val iconBg = Color(0xFF8AF5B3).copy(alpha = 0.3f)
    val iconRes = R.drawable.riwayat_transaksi
    val iconTint = Color(0xFF007A45)

    val badgeBg = Color(0xFF8AF5B3).copy(alpha = 0.4f)
    val badgeDot = Color(0xFF007A45)
    val badgeText = Color(0xFF007242)
    val badgeLabel = "Selesai"

    val amountColor = Color(0xFF181D18)
    val formattedAmount = formatRupiah(aktivitas.total)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left Circle Icon Container
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(iconRes),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Middle Text Column
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = aktivitas.idAktivitas,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF181D18),
                    letterSpacing = 0.28.sp
                )

                Text(
                    text = aktivitas.tanggal,
                    fontSize = 12.sp,
                    color = Color(0xFF3E4940)
                )

                // Dot Status Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(9999.dp))
                        .background(badgeBg)
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(badgeDot)
                    )
                    Text(
                        text = badgeLabel,
                        fontSize = 10.sp,
                        color = badgeText,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }

        // Right Text Column
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = formattedAmount,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
            Text(
                text = "${aktivitas.jumlahItem} item",
                fontSize = 12.sp,
                color = Color(0xFF3E4940)
            )
        }
    }
}
