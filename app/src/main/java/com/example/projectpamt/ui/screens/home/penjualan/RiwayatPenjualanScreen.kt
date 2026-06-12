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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalClipboardManager
import kotlinx.coroutines.delay
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projectpamt.R
import com.example.projectpamt.ui.theme.*
import com.example.projectpamt.ui.utils.DynamicStatusBar
import com.example.projectpamt.ui.utils.formatRupiah
import com.example.projectpamt.viewmodel.penjualan.RiwayatFilter
import com.example.projectpamt.viewmodel.penjualan.RiwayatPenjualanUiState
import com.example.projectpamt.viewmodel.penjualan.RiwayatPenjualanViewModel
import com.example.projectpamt.viewmodel.penjualan.PenjualanWithDetails
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatPenjualanScreen(
    modifier: Modifier = Modifier,
    viewModel: RiwayatPenjualanViewModel = viewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // Set dynamic status bar color to GreenPrimary
    DynamicStatusBar(backgroundColor = GreenPrimary)

    var selectedTxn by remember { mutableStateOf<PenjualanWithDetails?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundSlate)
                .padding(innerPadding)
        ) {
            // ── HEADER ──────────────────────────────────────────────────────────
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
                // Back Button
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

                // Title Area
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

            // ── CONTENT BODY ────────────────────────────────────────────────────
            when (val state = uiState) {
                is RiwayatPenjualanUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize().weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = GreenPrimary)
                    }
                }
                is RiwayatPenjualanUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize().weight(1f).padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = state.message, color = DangerRed, fontSize = 14.sp)
                    }
                }
                is RiwayatPenjualanUiState.Success -> {
                    SuccessContent(
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

    // ── DETAILS BOTTOM SHEET ────────────────────────────────────────────────
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
private fun SuccessContent(
    state: RiwayatPenjualanUiState.Success,
    onSearchChange: (String) -> Unit,
    onFilterSelect: (RiwayatFilter) -> Unit,
    onTxnClick: (PenjualanWithDetails) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Search bar
        item {
            TextField(
                value = state.searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                placeholder = {
                    Text("Cari ID Transaksi / Pelanggan...", color = TextPlaceholder, fontSize = 14.sp)
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

        // Summary cards section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Total Pendapatan Card
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

                // Total Transaksi Card
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

        // Filter chips
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

        // List
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

@Composable
private fun TransactionItem(
    txn: PenjualanWithDetails,
    onClick: () -> Unit
) {
    val dateStr = txn.penjualan.createdAt ?: ""
    val formattedTime = remember(dateStr) {
        try {
            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("id", "ID")).parse(dateStr)
            if (date != null) {
                SimpleDateFormat("d MMM yyyy, HH:mm", Locale("id", "ID")).format(date)
            } else {
                dateStr
            }
        } catch (e: Exception) {
            dateStr
        }
    }

    val customerName = txn.pelanggan?.nama ?: "Umum (Cash)"
    val itemCount = txn.items.sumOf { it.quantity }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderSlate)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(GreenMintActive),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.riwayat_transaksi),
                        contentDescription = null,
                        tint = GreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Info details
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = customerName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        // Status Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Selesai",
                                color = Color(0xFF2E7D32),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "ID: #${txn.penjualan.idPenjualan?.take(8)?.uppercase() ?: ""}",
                        fontSize = 12.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = formattedTime,
                        fontSize = 11.sp,
                        color = TextPlaceholder
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Right amount & count details
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = formatRupiah(txn.penjualan.totalHarga),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = GreenPrimary
                )
                Text(
                    text = "$itemCount item",
                    fontSize = 12.sp,
                    color = TextMuted
                )
                txn.kas?.let {
                    Text(
                        text = it.nama,
                        fontSize = 10.sp,
                        color = TextPlaceholder,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionDetailSheetContent(
    txn: PenjualanWithDetails,
    onClose: () -> Unit
) {
    val dateStr = txn.penjualan.createdAt ?: ""
    val formattedTime = remember(dateStr) {
        try {
            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("id", "ID")).parse(dateStr)
            if (date != null) {
                SimpleDateFormat("d MMMM yyyy HH:mm", Locale("id", "ID")).format(date)
            } else {
                dateStr
            }
        } catch (e: Exception) {
            dateStr
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Receipt Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Detail Transaksi",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "ID: ${txn.penjualan.idPenjualan}",
                        fontSize = 13.sp,
                        color = TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    
                    val clipboardManager = LocalClipboardManager.current
                    var copied by remember { mutableStateOf(false) }
                    
                    LaunchedEffect(copied) {
                        if (copied) {
                            delay(2000)
                            copied = false
                        }
                    }

                    Icon(
                        imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                        contentDescription = "Salin ID",
                        tint = if (copied) Color(0xFF2E7D32) else TextMuted,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable {
                                txn.penjualan.idPenjualan?.let { id ->
                                    clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(id))
                                    copied = true
                                }
                            }
                    )
                }
            }
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Tutup",
                    tint = TextMuted
                )
            }
        }

        HorizontalDivider(color = BorderSlate)

        // General Information
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            DetailInfoRow(label = "Waktu Transaksi", value = formattedTime)
            DetailInfoRow(label = "Metode Pembayaran", value = txn.kas?.nama ?: "-")
            DetailInfoRow(label = "Pelanggan", value = txn.pelanggan?.nama ?: "Umum (Cash)")
            if (txn.pelanggan != null) {
                DetailInfoRow(label = "No. Telepon", value = txn.pelanggan.telepon, enableCopy = true)
            }
        }

        HorizontalDivider(color = BorderSlate)

        // Items list
        Text(
            text = "Daftar Item",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            txn.items.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.produk.nama,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextDark
                        )
                        Text(
                            text = "${formatRupiah(item.produk.harga)} x ${item.quantity} ${item.produk.namaSatuan}",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                    Text(
                        text = formatRupiah(item.totalHarga),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }
            }
        }

        HorizontalDivider(color = BorderSlate)

        // Total payments summary
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            DetailInfoRow(
                label = "Total Harga",
                value = formatRupiah(txn.penjualan.totalHarga),
                isBoldValue = true
            )
            DetailInfoRow(
                label = "Jumlah Bayar",
                value = formatRupiah(txn.penjualan.jumlahBayar)
            )
            
            val change = txn.penjualan.jumlahBayar - txn.penjualan.totalHarga
            DetailInfoRow(
                label = "Kembalian",
                value = formatRupiah(change),
                valueColor = if (change > 0) Color(0xFF2E7D32) else TextDark,
                isBoldValue = true
            )
        }
    }
}

@Composable
private fun DetailInfoRow(
    label: String,
    value: String,
    isBoldValue: Boolean = false,
    valueColor: Color = TextDark,
    enableCopy: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = TextMuted,
            modifier = Modifier.weight(0.8f)
        )
        Row(
            modifier = Modifier.weight(1.2f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = if (isBoldValue) FontWeight.Bold else FontWeight.Normal,
                color = valueColor,
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
            if (enableCopy && value.isNotBlank() && value != "-") {
                val clipboardManager = LocalClipboardManager.current
                var copied by remember { mutableStateOf(false) }
                
                LaunchedEffect(copied) {
                    if (copied) {
                        delay(2000)
                        copied = false
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                    contentDescription = "Salin",
                    tint = if (copied) Color(0xFF2E7D32) else TextMuted,
                    modifier = Modifier
                        .size(14.dp)
                        .clickable {
                            clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(value))
                            copied = true
                        }
                )
            }
        }
    }
}
