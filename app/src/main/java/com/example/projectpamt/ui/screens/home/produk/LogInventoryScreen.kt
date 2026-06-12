package com.example.projectpamt.ui.screens.home.produk

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
import com.example.projectpamt.data.model.LogInventory
import com.example.projectpamt.ui.theme.*
import com.example.projectpamt.ui.utils.DynamicStatusBar
import com.example.projectpamt.ui.utils.formatRupiah
import com.example.projectpamt.viewmodel.produk.LogInventoryFilter
import com.example.projectpamt.viewmodel.produk.LogInventoryUiState
import com.example.projectpamt.viewmodel.produk.LogInventoryViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInventoryScreen(
    modifier: Modifier = Modifier,
    viewModel: LogInventoryViewModel = viewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Set dynamic status bar color to GreenPrimary
    DynamicStatusBar(backgroundColor = GreenPrimary)

    var selectedLog by remember { mutableStateOf<LogInventory?>(null) }
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
                        text = "Log Inventori",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Riwayat perubahan stok dan harga produk",
                        fontSize = 14.sp,
                        color = Color(0xFFDCFCE7)
                    )
                }
            }

            // ── CONTENT BODY ────────────────────────────────────────────────────
            when (val state = uiState) {
                is LogInventoryUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize().weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = GreenPrimary)
                    }
                }
                is LogInventoryUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize().weight(1f).padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = state.message, color = DangerRed, fontSize = 14.sp)
                    }
                }
                is LogInventoryUiState.Success -> {
                    SuccessContent(
                        state = state,
                        onSearchChange = viewModel::setSearchQuery,
                        onFilterSelect = viewModel::setFilter,
                        onLogClick = { selectedLog = it }
                    )
                }
                else -> {}
            }
        }
    }

    // ── DETAILS BOTTOM SHEET ────────────────────────────────────────────────
    if (selectedLog != null) {
        ModalBottomSheet(
            onDismissRequest = { selectedLog = null },
            sheetState = sheetState,
            containerColor = Color.White,
            dragHandle = { BottomSheetDefaults.DragHandle(color = BorderSlate) }
        ) {
            LogDetailSheetContent(
                log = selectedLog!!,
                onClose = { selectedLog = null }
            )
        }
    }
}

@Composable
private fun SuccessContent(
    state: LogInventoryUiState.Success,
    onSearchChange: (String) -> Unit,
    onFilterSelect: (LogInventoryFilter) -> Unit,
    onLogClick: (LogInventory) -> Unit
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
                    Text("Cari nama produk...", color = TextPlaceholder, fontSize = 14.sp)
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

        // Filter chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LogInventoryFilter.entries.forEach { filter ->
                    val isSelected = filter == state.selectedFilter
                    val label = when (filter) {
                        LogInventoryFilter.SEMUA_WAKTU -> "Semua Waktu"
                        LogInventoryFilter.HARI_INI -> "Hari Ini"
                        LogInventoryFilter.MINGGU_INI -> "Minggu Ini"
                        LogInventoryFilter.BULAN_INI -> "Bulan Ini"
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
        if (state.listLogs.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tidak ada log inventori ditemukan",
                        color = TextMuted,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            items(state.listLogs, key = { it.idLogInventory ?: "" }) { log ->
                LogItem(log = log, onClick = { onLogClick(log) })
            }
        }
    }
}

@Composable
private fun LogItem(
    log: LogInventory,
    onClick: () -> Unit
) {
    val dateStr = log.updatedAt ?: ""
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

    val diff = log.stokBaru - log.stokLama

    val (badgeText, badgeColor, badgeBg, actionLabel) = when {
        diff > 0 -> {
            val d = diff.toInt()
            val text = if (diff == d.toDouble()) "+$d" else "+$diff"
            Quadruple(text, Color(0xFF2E7D32), Color(0xFFE8F5E9), "Stok Masuk")
        }
        diff < 0 -> {
            val d = (-diff).toInt()
            val text = if (diff == d.toDouble()) "-$d" else "-$diff"
            Quadruple(text, DangerRed, Color(0xFFFFEBEE), "Stok Keluar")
        }
        else -> {
            Quadruple("Update", TextMuted, BorderSlate, "Update Produk")
        }
    }

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
                // Icon Box
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(GreenMintActive),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.produk),
                        contentDescription = null,
                        tint = GreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Information details
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
                            text = log.namaLama,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        // Status Action Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(badgeBg)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = actionLabel,
                                color = if (diff == 0.0) TextDark else badgeColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "ID Log: #${log.idLogInventory?.take(8)?.uppercase() ?: ""}",
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

            // Right stock delta and price info
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (diff != 0.0) {
                    Text(
                        text = badgeText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = badgeColor
                    )
                }
                Text(
                    text = formatRupiah(log.hargaLama),
                    fontSize = 12.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun LogDetailSheetContent(
    log: LogInventory,
    onClose: () -> Unit
) {
    val dateStr = log.updatedAt ?: ""
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

    val diff = log.stokBaru - log.stokLama

    val (stokChangeDesc, stokColor) = when {
        diff > 0 -> {
            val text = "+${diff.toInt()} pcs (Stok Masuk / Penambahan)"
            Pair(text, Color(0xFF2E7D32))
        }
        diff < 0 -> {
            val text = "${diff.toInt()} pcs (Stok Keluar / Terjual)"
            Pair(text, DangerRed)
        }
        else -> {
            Pair("Tidak ada perubahan stok (Update Info/Harga)", TextMuted)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Sheet Header
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
                    text = "Detail Log Inventori",
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
                        text = "ID Log: ${log.idLogInventory}",
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
                        contentDescription = "Salin ID Log",
                        tint = if (copied) Color(0xFF2E7D32) else TextMuted,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable {
                                log.idLogInventory?.let { id ->
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

        // General Information Table
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            LogInfoRow(label = "Waktu Log", value = formattedTime)
            LogInfoRow(label = "ID Produk", value = log.idProduk, enableCopy = true)
            LogInfoRow(label = "Nama Produk (Snapshot)", value = log.namaLama)
            LogInfoRow(label = "Harga Jual (Snapshot)", value = formatRupiah(log.hargaLama))
            
            HorizontalDivider(color = BorderSlate, modifier = Modifier.padding(vertical = 4.dp))

            Text(
                text = "Perubahan Stok",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            LogInfoRow(label = "Stok Awal", value = "${log.stokLama.toInt()} pcs")
            LogInfoRow(label = "Stok Akhir", value = "${log.stokBaru.toInt()} pcs")
            LogInfoRow(
                label = "Perbedaan",
                value = stokChangeDesc,
                isBoldValue = true,
                valueColor = stokColor
            )
        }
    }
}

@Composable
private fun LogInfoRow(
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

// Helper container class
private data class Quadruple<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)
