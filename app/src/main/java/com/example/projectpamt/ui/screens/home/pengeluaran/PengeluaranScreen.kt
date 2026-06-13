package com.example.projectpamt.ui.screens.home.pengeluaran

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.projectpamt.data.model.Pengeluaran
import com.example.projectpamt.ui.components.AppTextField
import com.example.projectpamt.ui.navigation.EditPengeluaran
import com.example.projectpamt.ui.navigation.TambahPengeluaran
import com.example.projectpamt.ui.screens.home.pengeluaran.components.ExpenseItem
import com.example.projectpamt.ui.theme.BackgroundSlate
import com.example.projectpamt.ui.theme.BorderSlate
import com.example.projectpamt.ui.theme.DangerRed
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.TextMuted
import com.example.projectpamt.utils.formatRupiah
import com.example.projectpamt.viewmodel.pengeluaran.PengeluaranUiState
import com.example.projectpamt.viewmodel.pengeluaran.PengeluaranViewModel
import java.text.SimpleDateFormat
import java.time.Instant.parse
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun PengeluaranScreen(
    modifier: Modifier = Modifier,
    viewModel: PengeluaranViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchPengeluaran()
    }

    PengeluaranContent(
        modifier = modifier,
        uiState = uiState,
        onBackClick = { navController.popBackStack() },
        onAddClick = { navController.navigate(TambahPengeluaran) },
        onItemClick = { pengeluaran ->
            navController.navigate(EditPengeluaran(pengeluaran))
        }
    )
}

@Composable
private fun PengeluaranContent(
    modifier: Modifier = Modifier,
    uiState: PengeluaranUiState,
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    onItemClick: (Pengeluaran) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ── FIXED HEADER ──────────────────────────────────────────────
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                        text = "Pengeluaran",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Kelola pengeluaran Anda",
                        fontSize = 14.sp,
                        color = Color(0xFFDCFCE7),
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            // ── SCROLLABLE LIST AREA ────────────────────────────────────────
            when (uiState) {
                is PengeluaranUiState.Loading -> {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = GreenPrimary)
                    }
                }
                is PengeluaranUiState.Error -> {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(text = uiState.message, color = DangerRed, fontSize = 16.sp)
                    }
                }
                is PengeluaranUiState.Success -> {
                    val rawList = uiState.data
                    
                    // Filter search query
                    val filteredList = remember(rawList, searchQuery) {
                        if (searchQuery.isBlank()) {
                            rawList
                        } else {
                            rawList.filter {
                                it.deskripsi?.contains(searchQuery, ignoreCase = true) == true ||
                                        it.kategori?.name?.contains(searchQuery, ignoreCase = true) == true
                            }
                        }
                    }

                    // Compute dynamic totals based on current date
                    val now = Calendar.getInstance()
                    val currentMonth = now.get(Calendar.MONTH)
                    val currentYear = now.get(Calendar.YEAR)
                    
                    val lastMonthCal = Calendar.getInstance().apply {
                        add(Calendar.MONTH, -1)
                    }
                    val lastMonth = lastMonthCal.get(Calendar.MONTH)
                    val lastMonthYear = lastMonthCal.get(Calendar.YEAR)

                    val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("id", "ID"))

                    val expensesThisMonthList = remember(rawList) {
                        rawList.filter { exp ->
                            val dateStr = exp.createdAt
                            if (dateStr == null) {
                                false
                            } else {
                                val date = try {
                                    parse(dateStr)?.let { Date.from(it) }
                                } catch (e: Exception) {
                                    try { df.parse(dateStr) } catch(ex: Exception) { null }
                                }
                                if (date == null) {
                                    false
                                } else {
                                    val cal = Calendar.getInstance().apply { time = date }
                                    cal.get(Calendar.MONTH) == currentMonth && cal.get(Calendar.YEAR) == currentYear
                                }
                            }
                        }
                    }

                    val totalExpenses = remember(expensesThisMonthList) {
                        expensesThisMonthList.sumOf { it.total }
                    }

                    val totalExpensesLastMonth = remember(rawList) {
                        rawList.filter { exp ->
                            val dateStr = exp.createdAt
                            if (dateStr == null) {
                                false
                            } else {
                                val date = try {
                                    java.time.Instant.parse(dateStr)?.let { Date.from(it) }
                                } catch (e: Exception) {
                                    try { df.parse(dateStr) } catch(ex: Exception) { null }
                                }
                                if (date == null) {
                                    false
                                } else {
                                    val cal = Calendar.getInstance().apply { time = date }
                                    cal.get(Calendar.MONTH) == lastMonth && cal.get(Calendar.YEAR) == lastMonthYear
                                }
                            }
                        }
                    }.sumOf { it.total }

                    // Compute comparison text & colors
                    val trendText = remember(totalExpenses, totalExpensesLastMonth) {
                        if (totalExpensesLastMonth == 0.0) {
                            if (totalExpenses == 0.0) {
                                "Stabil vs bulan lalu"
                            } else {
                                "Baru bulan ini"
                            }
                        } else {
                            val percent = ((totalExpenses - totalExpensesLastMonth) / totalExpensesLastMonth) * 100
                            val rounded = kotlin.math.round(percent * 10) / 10
                            if (rounded > 0) {
                                "↑ $rounded% vs bulan lalu"
                            } else if (rounded < 0) {
                                "↓ ${-rounded}% vs bulan lalu"
                            } else {
                                "Stabil vs bulan lalu"
                            }
                        }
                    }

                    val trendBgColor = remember(totalExpenses, totalExpensesLastMonth) {
                        if (totalExpenses >= totalExpensesLastMonth) {
                            Color(0xFFFEE2E2) // light red
                        } else {
                            Color(0xFFDCFCE7) // light green
                        }
                    }

                    val trendTextColor = remember(totalExpenses, totalExpensesLastMonth) {
                        if (totalExpenses >= totalExpensesLastMonth) {
                            DangerRed
                        } else {
                            GreenPrimary
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Card: Total Pengeluaran
                        item {
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
                                border = BorderStroke(1.dp, BorderSlate)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "Total Pengeluaran Bulan Ini",
                                        fontSize = 14.sp,
                                        color = TextMuted,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = formatRupiah(totalExpenses),
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DangerRed
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = trendBgColor,
                                                    shape = RoundedCornerShape(100.dp)
                                                )
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = trendText,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = trendTextColor
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Search Bar
                        item {
                            AppTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = "Cari pengeluaran...",
                                leadingIcon = Icons.Default.Search,
                                singleLine = true
                            )
                        }

                        // Section Title
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "TRANSAKSI TERBARU",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMuted,
                                    letterSpacing = 0.6.sp
                                )
                                Text(
                                    text = SimpleDateFormat("MMMM yyyy", Locale("id", "ID")).format(Date()),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenPrimary
                                )
                            }
                        }

                        // List Items
                        if (filteredList.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Tidak ada transaksi pengeluaran",
                                        color = TextMuted,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        } else {
                            items(filteredList, key = { it.idPengeluaran ?: "" }) { expense ->
                                ExpenseItem(
                                    expense = expense,
                                    onClick = { onItemClick(expense) }
                                )
                            }
                        }
                    }
                }
                else -> {}
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = onAddClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            shape = CircleShape,
            containerColor = GreenPrimary,
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Tambah Pengeluaran",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}