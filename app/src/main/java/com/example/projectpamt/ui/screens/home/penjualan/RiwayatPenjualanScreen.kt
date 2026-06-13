package com.example.projectpamt.ui.screens.home.penjualan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projectpamt.ui.screens.home.penjualan.components.TransactionDetailSheetContent
import com.example.projectpamt.ui.screens.home.penjualan.components.TransactionItem
import com.example.projectpamt.ui.theme.*
import com.example.projectpamt.utils.formatRupiah
import com.example.projectpamt.viewmodel.penjualan.uistate.RiwayatFilter
import com.example.projectpamt.viewmodel.penjualan.uistate.RiwayatPenjualanUiState
import com.example.projectpamt.viewmodel.penjualan.RiwayatPenjualanViewModel
import com.example.projectpamt.viewmodel.penjualan.uistate.PenjualanWithDetails

import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.SnackbarHostState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatPenjualanScreen(
    viewModel: RiwayatPenjualanViewModel = viewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var selectedTxn by remember { mutableStateOf<PenjualanWithDetails?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    PullToRefreshBox(
        isRefreshing = viewModel.isRefreshing,
        onRefresh = viewModel::refresh,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundSlate)
        ) {
            // Header etc.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = GreenPrimary,
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Row(
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Kembali",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }


                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Riwayat Penjualan",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Daftar transaksi penjualan toko Anda",
                        fontSize = 14.sp,
                        color = Color(0xFFDCFCE7)
                    )
                }
            }


            when (val state = uiState) {
                is RiwayatPenjualanUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .windowInsetsPadding(WindowInsets.navigationBars),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = GreenPrimary)
                    }
                }

                is RiwayatPenjualanUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = state.message ?: "Terjadi kesalahan", color = DangerRed, fontSize = 14.sp)
                    }
                }

                is RiwayatPenjualanUiState.Success -> {
                    RiwayatPenjualanContent(
                        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                        state = state,
                        onSearchChange = viewModel::setSearchQuery,
                        onFilterSelect = viewModel::setFilter,
                        onTxnClick = { selectedTxn = it }
                    )
                }

                else -> {}
            }
        }
    }



    if (selectedTxn != null) {
        ModalBottomSheet(
            onDismissRequest = { selectedTxn = null },
            sheetState = sheetState,
            containerColor = Color.White,
            dragHandle = { BottomSheetDefaults.DragHandle(color = BorderSlate) }
        ) {
            TransactionDetailSheetContent(
                txn = selectedTxn!!,
                onClose = { selectedTxn = null }
            )
        }
    }
}

@Composable
private fun RiwayatPenjualanContent(
    modifier: Modifier = Modifier,
    state: RiwayatPenjualanUiState.Success,
    onSearchChange: (String) -> Unit,
    onFilterSelect: (RiwayatFilter) -> Unit,
    onTxnClick: (PenjualanWithDetails) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            TextField(
                value = state.searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                placeholder = {
                    Text(
                        "Cari ID Transaksi / Pelanggan...",
                        color = TextPlaceholder,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = TextPlaceholder,
                        modifier = Modifier.size(18.dp)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = GreenMintBg,
                    unfocusedContainerColor = GreenMintBg,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = TextDark,
                    unfocusedTextColor = TextDark
                )
            )
        }


        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Card(
                    modifier = Modifier
                        .weight(1.2f)
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderSlate)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Total Pendapatan", fontSize = 12.sp, color = TextMuted)
                        Text(
                            text = formatRupiah(state.totalPendapatan),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenPrimary
                        )
                    }
                }


                Card(
                    modifier = Modifier
                        .weight(0.8f)
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderSlate)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Total Transaksi", fontSize = 12.sp, color = TextMuted)
                        Text(
                            text = "${state.totalTransaksi}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    }
                }
            }
        }


        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RiwayatFilter.entries.forEach { filter ->
                    val isSelected = filter == state.selectedFilter
                    val label = when (filter) {
                        RiwayatFilter.SEMUA_WAKTU -> "Semua Waktu"
                        RiwayatFilter.HARI_INI -> "Hari Ini"
                        RiwayatFilter.MINGGU_INI -> "Minggu Ini"
                        RiwayatFilter.BULAN_INI -> "Bulan Ini"
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                            .background(if (isSelected) GreenPrimary else Color.White)
                            .then(
                                if (isSelected) Modifier else Modifier.border(
                                    1.dp,
                                    BorderSlate,
                                    RoundedCornerShape(9999.dp)
                                )
                            )
                            .clickable { onFilterSelect(filter) }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            color = if (isSelected) Color.White else TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }


        if (state.listPenjualan.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tidak ada riwayat penjualan ditemukan",
                        color = TextMuted,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            items(state.listPenjualan, key = { it.penjualan.idPenjualan ?: "" }) { txn ->
                TransactionItem(txn = txn, onClick = { onTxnClick(txn) })
            }
        }
    }
}

