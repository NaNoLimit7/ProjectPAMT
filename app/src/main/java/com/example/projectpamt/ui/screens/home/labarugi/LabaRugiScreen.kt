package com.example.projectpamt.ui.screens.home.labarugi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projectpamt.ui.theme.*
import com.example.projectpamt.utils.formatNumber
import com.example.projectpamt.utils.formatRupiah
import com.example.projectpamt.viewmodel.labarugi.LabaRugiPeriode
import com.example.projectpamt.viewmodel.labarugi.LabaRugiState
import com.example.projectpamt.viewmodel.labarugi.LabaRugiViewModel
import com.example.projectpamt.viewmodel.labarugi.uistate.LabaRugiUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabaRugiScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: LabaRugiViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundSlate),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = GreenPrimary,
                        shape = RoundedCornerShape(
                            bottomStart = 24.dp,
                            bottomEnd = 24.dp
                        )
                    )
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // Back Button
                Row(
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Kembali",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Laporan Laba/Rugi",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    ) { innerPadding ->
        when (uiState) {
            is LabaRugiUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GreenPrimary)
                }
            }
            is LabaRugiUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = (uiState as LabaRugiUiState.Error).message,
                            color = Color.Red,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Button(
                            onClick = { viewModel.loadData() },
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                        ) {
                            Text("Coba Lagi", color = Color.White)
                        }
                    }
                }
            }
            is LabaRugiUiState.Success -> {
                val successState = (uiState as LabaRugiUiState.Success).state
                val period = (uiState as LabaRugiUiState.Success).selectedPeriod

                PullToRefreshBox(
                    isRefreshing = viewModel.isRefreshing,
                    onRefresh = { viewModel.refresh() },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    LabaRugiContent(
                        state = successState,
                        selectedPeriod = period,
                        onPeriodSelect = { viewModel.setPeriod(it) }
                    )
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun LabaRugiContent(
    state: LabaRugiState,
    selectedPeriod: LabaRugiPeriode,
    onPeriodSelect: (LabaRugiPeriode) -> Unit
) {
    var showDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1. Filter Period Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color(0xFFBECABE)), RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = Color(0xFF3E4940),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Periode Laporan",
                            fontSize = 11.sp,
                            color = Color(0xFF3E4940)
                        )
                        Text(
                            text = selectedPeriod.getDisplayName(),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF181D18)
                        )
                    }
                }

                Box {
                    Button(
                        onClick = { showDropdown = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x268AF5B3)),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Ubah",
                                color = Color(0xFF007242),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color(0xFF007242),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false }
                    ) {
                        LabaRugiPeriode.entries.forEach { period ->
                            DropdownMenuItem(
                                text = { Text(period.getDisplayName()) },
                                onClick = {
                                    onPeriodSelect(period)
                                    showDropdown = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // 2. Net Profit/Loss Card
        val isProfit = state.netProfit >= 0
        val cardBgColor = if (isProfit) Color(0xFF007A45) else Color(0xFFFFDAD6)
        val textPrimaryColor = if (isProfit) Color(0xFFA2FFC0) else Color(0xFF93000A)
        val labelText = if (isProfit) "LABA BERSIH" else "RUGI BERSIH"

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = cardBgColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = labelText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimaryColor,
                            letterSpacing = 0.6.sp
                        )
                        Text(
                            text = formatRupiah(state.netProfit),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimaryColor,
                            lineHeight = 38.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = if (isProfit) Color(0x33FFFFFF) else Color(0x33BA1A1A),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isProfit) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown,
                            contentDescription = null,
                            tint = textPrimaryColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                HorizontalDivider(
                    color = if (isProfit) Color(0x1AFFFFFF) else Color(0x1ABA1A1A),
                    thickness = 1.dp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Total Penjualan",
                            fontSize = 12.sp,
                            color = textPrimaryColor.copy(alpha = 0.8f)
                        )
                        Text(
                            text = formatRupiah(state.totalPenjualan),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimaryColor
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "Total Pengeluaran",
                            fontSize = 12.sp,
                            color = textPrimaryColor.copy(alpha = 0.8f)
                        )
                        Text(
                            text = formatRupiah(state.totalPengeluaran),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimaryColor
                        )
                    }
                }
            }
        }

//        // 3. Pendapatan Breakdown Card
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .border(BorderStroke(1.dp, Color(0x4DBECABE)), RoundedCornerShape(20.dp)),
//            colors = CardDefaults.cardColors(containerColor = Color.White),
//            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(24.dp),
//                verticalArrangement = Arrangement.spacedBy(20.dp)
//            ) {
//                // Header
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(40.dp)
//                            .background(color = Color(0x268AF5B3), shape = RoundedCornerShape(12.dp)),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
//                            contentDescription = null,
//                            tint = Color(0xFF005F34),
//                            modifier = Modifier.size(22.dp)
//                        )
//                    }
//                    Text(
//                        text = "Pendapatan",
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFF005F34)
//                    )
//                }
//
//                // Items list
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    BreakdownItem(
//                        title = "Penjualan Produk",
//                        subtitle = "${formatNumber(state.countPenjualanProduk)} Transaksi",
//                        value = formatRupiah(state.totalPenjualanProduk),
//                        isExpense = false
//                    )
//                    BreakdownItem(
//                        title = "Jasa & Layanan",
//                        subtitle = "${formatNumber(state.countJasaLayanan)} Order",
//                        value = formatRupiah(state.totalJasaLayanan),
//                        isExpense = false
//                    )
//                }
//
//                HorizontalDivider(color = Color(0xFFBECABE), thickness = 1.dp)
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "TOTAL INCOME",
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFF3E4940)
//                    )
//                    Text(
//                        text = formatRupiah(state.totalPenjualan),
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFF005F34)
//                    )
//                }
//            }
//        }
//
//        // 4. Pengeluaran Breakdown Card
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .border(BorderStroke(1.dp, Color(0x4DBECABE)), RoundedCornerShape(20.dp)),
//            colors = CardDefaults.cardColors(containerColor = Color.White),
//            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(24.dp),
//                verticalArrangement = Arrangement.spacedBy(20.dp)
//            ) {
//                // Header
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(40.dp)
//                            .background(color = Color(0xFFFFDAD6), shape = RoundedCornerShape(12.dp)),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.TrendingDown,
//                            contentDescription = null,
//                            tint = Color(0xFFBA1A1A),
//                            modifier = Modifier.size(20.dp)
//                        )
//                    }
//                    Text(
//                        text = "Pengeluaran",
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFFBA1A1A)
//                    )
//                }
//
//                // Dynamic grouped expenses list
//                if (state.groupedExpenses.isEmpty()) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 16.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "Tidak ada pengeluaran",
//                            fontSize = 14.sp,
//                            color = Color(0xFF64748B)
//                        )
//                    }
//                } else {
//                    Column(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalArrangement = Arrangement.spacedBy(16.dp)
//                    ) {
//                        state.groupedExpenses.forEach { group ->
//                            val subLabel = when (group.categoryName.lowercase()) {
//                                "operasional" -> "Sewa & Gaji"
//                                "persediaan" -> "Stok Barang"
//                                "utilitas" -> "Listrik, Air, Wifi"
//                                else -> "Biaya pengeluaran ${group.categoryName.lowercase()}"
//                            }
//
//                            BreakdownItem(
//                                title = group.categoryName,
//                                subtitle = subLabel,
//                                value = formatRupiah(group.total),
//                                isExpense = true
//                            )
//                        }
//                    }
//                }
//
//                HorizontalDivider(color = Color(0xFFBECABE), thickness = 1.dp)
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "TOTAL EXPENSE",
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFF3E4940)
//                    )
//                    Text(
//                        text = formatRupiah(state.totalPengeluaran),
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFFBA1A1A)
//                    )
//                }
//            }
//        }
    }
}

@Composable
private fun BreakdownItem(
    title: String,
    subtitle: String,
    value: String,
    isExpense: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF181D18)
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color(0xFF3E4940)
            )
        }
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = if (isExpense) Color(0xFFBA1A1A) else Color(0xFF007242)
        )
    }
}
