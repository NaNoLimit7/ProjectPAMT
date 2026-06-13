package com.example.projectpamt.ui.screens.home.produk

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projectpamt.data.model.LogInventory
import com.example.projectpamt.ui.screens.home.produk.components.LogDetailSheetContent
import com.example.projectpamt.ui.screens.home.produk.components.LogItem
import com.example.projectpamt.ui.theme.*
import com.example.projectpamt.viewmodel.produk.LogInventoryFilter
import com.example.projectpamt.viewmodel.produk.LogInventoryUiState
import com.example.projectpamt.viewmodel.produk.LogInventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInventoryScreen(
    viewModel: LogInventoryViewModel = viewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var selectedLog by remember { mutableStateOf<LogInventory?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .windowInsetsPadding(WindowInsets.navigationBars)
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
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GreenPrimary)
                }
            }

            is LogInventoryUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message, color = DangerRed, fontSize = 14.sp)
                }
            }

            is LogInventoryUiState.Success -> {
                LogInventoryContent(
                    state = state,
                    onSearchChange = viewModel::setSearchQuery,
                    onFilterSelect = viewModel::setFilter,
                    onLogClick = { selectedLog = it }
                )
            }

            else -> {}
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
fun LogInventoryContent(
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
