package com.example.projectpamt.ui.screens.home.kas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projectpamt.R
import com.example.projectpamt.data.model.Kas
import com.example.projectpamt.data.model.LogKasItem
import com.example.projectpamt.ui.theme.*
import com.example.projectpamt.utils.buildAnnotatedLogDescription
import com.example.projectpamt.utils.formatRupiah
import com.example.projectpamt.viewmodel.kas.uistate.TransaksiKasUiState
import com.example.projectpamt.viewmodel.kas.TransaksiKasViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs

@Composable
fun TransaksiKasScreen(
    kas: Kas,
    viewModel: TransaksiKasViewModel = viewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Fetch transactions on startup
    LaunchedEffect(kas.idKas) {
        kas.idKas?.let { id ->
            viewModel.fetchTransaksiKas(id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        // ── HEADER SECTION ──────────────────────────────────────────────────
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
                    text = "Transaksi ${kas.nama}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Riwayat transaksi uang masuk & keluar",
                    fontSize = 14.sp,
                    color = Color(0xFFDCFCE7)
                )
            }
        }

        // ── CONTENT BODY ────────────────────────────────────────────────────
        when (val state = uiState) {
            is TransaksiKasUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GreenPrimary)
                }
            }

            is TransaksiKasUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        color = DangerRed,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            is TransaksiKasUiState.Success -> {
                TransaksiKasContent(kas = kas, transactions = state.transactions)
            }

            else -> {}
        }
    }
}

@Composable
private fun TransaksiKasContent(
    kas: Kas,
    transactions: List<LogKasItem>
) {
    // Calculate total net mutasi
    val totalMutasi = remember(transactions) {
        transactions.sumOf { it.saldoAkhir - it.saldoAwal }
    }

    // Group transactions by date
    val groupedTransactions = remember(transactions) {
        transactions.groupBy { logItem ->
            try {
                val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("id", "ID")).parse(logItem.updatedAt)
                if (date != null) {
                    SimpleDateFormat("d MMMM yyyy", Locale("id", "ID")).format(date)
                } else {
                    "Lainnya"
                }
            } catch (_: Exception) {
                "Lainnya"
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Bento Summary Cards (Saldo Saat Ini & Total Mutasi)
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Card 1: Saldo Saat Ini
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderSlate)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Saldo Saat Ini",
                                fontSize = 12.sp,
                                color = TextMuted,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = formatRupiah(kas.saldo),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF005F34) // Dark Green
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(GreenMintBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.kas),
                                contentDescription = null,
                                tint = GreenPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Card 2: Total Mutasi
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderSlate)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Total Mutasi",
                                fontSize = 12.sp,
                                color = TextMuted,
                                fontWeight = FontWeight.Medium
                            )

                            val trendColor = if (totalMutasi >= 0) Color(0xFF006D3F) else DangerRed
                            val trendSign = if (totalMutasi >= 0) "+" else ""

                            Text(
                                text = "$trendSign${formatRupiah(totalMutasi)}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = trendColor
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (totalMutasi >= 0) GreenMintBg else Color(0xFFFFEBEE)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (totalMutasi >= 0) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                                contentDescription = null,
                                tint = if (totalMutasi >= 0) GreenPrimary else DangerRed,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Section Title: Riwayat Transaksi
        item {
            Text(
                text = "Riwayat Transaksi",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
        }

        // List of Grouped Daily Transactions
        groupedTransactions.forEach { (date, dayLogs) ->
            // Daily Date Header Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = date,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted
                    )

                    // Calculate net change of the day
                    val dailyNet = dayLogs.sumOf { it.saldoAkhir - it.saldoAwal }
                    val dailyNetColor = when {
                        dailyNet > 0 -> Color(0xFF006D3F)
                        dailyNet < 0 -> DangerRed
                        else -> TextMuted
                    }
                    val dailyNetSign = when {
                        dailyNet > 0 -> "+"
                        else -> ""
                    }
                    Text(
                        text = "$dailyNetSign${formatRupiah(dailyNet)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = dailyNetColor
                    )
                }
            }

            // Transaction Items for that day
            items(dayLogs, key = { it.idLogKas ?: it.updatedAt }) { logItem ->
                TransactionRowCard(logItem = logItem)
            }
        }

        // End of History Illustration
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEAEFE8)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.riwayat_transaksi),
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = "Akhir riwayat yang tersedia",
                    fontSize = 14.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun TransactionRowCard(
    logItem: LogKasItem
) {
    val dateStr = logItem.updatedAt
    val formattedTime = remember(dateStr) {
        try {
            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("id", "ID")).parse(dateStr)
            if (date != null) {
                SimpleDateFormat("HH:mm", Locale("id", "ID")).format(date)
            } else {
                ""
            }
        } catch (_: Exception) {
            ""
        }
    }

    val changeAmount = logItem.saldoAkhir - logItem.saldoAwal
    val isDebit = changeAmount >= 0

    val (icon, iconTint, iconBg) = if (isDebit) {
        Triple(
            Icons.Default.ArrowUpward,
            Color(0xFF137333),
            Color(0xFFE6F4EA)
        )
    } else {
        Triple(
            Icons.Default.ArrowDownward,
            Color(0xFFC5221F),
            Color(0xFFFFEBEE)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderSlate)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left Icon Box
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Right Information Box
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Description Text with highlighted money amounts
                Text(
                    text = buildAnnotatedLogDescription(logItem.detailKeterangan),
                    fontSize = 14.sp,
                    color = Color(0xFF3E4940),
                    lineHeight = 20.sp
                )

                // Footer: Performer and Time in a single line with bullet separator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.profil),
                        contentDescription = null,
                        tint = Color(0xFF6E7A70),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Oleh: ${logItem.pelaku} • $formattedTime",
                        fontSize = 13.sp,
                        color = Color(0xFF6E7A70),
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            // Right Side: Amount
            Text(
                text = "${if (isDebit) "+" else "-"}${formatRupiah(abs(changeAmount))}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = iconTint
            )
        }
    }
}
