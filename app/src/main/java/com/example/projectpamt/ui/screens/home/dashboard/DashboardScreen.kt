package com.example.projectpamt.ui.screens.home.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.projectpamt.R
import com.example.projectpamt.ui.components.CircleContainer
import com.example.projectpamt.ui.components.DashboardCard
import com.example.projectpamt.ui.components.QuickActionButton
import com.example.projectpamt.ui.components.WeeklyBarChart
import com.example.projectpamt.ui.theme.ActionBlue
import com.example.projectpamt.ui.theme.ActionGreen
import com.example.projectpamt.ui.theme.ActionOrange
import com.example.projectpamt.ui.theme.ActionPurple
import com.example.projectpamt.ui.theme.BackgroundSlate
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.GreenSecondary
import com.example.projectpamt.ui.theme.ProjectPAMTTheme
import com.example.projectpamt.ui.utils.formatNumber
import com.example.projectpamt.ui.utils.formatRupiah
import com.example.projectpamt.ui.utils.getInitials
import com.example.projectpamt.ui.utils.toIndonesianFormattedDate
import com.example.projectpamt.viewmodel.auth.AuthViewModel
import java.util.Date

// ─── Data model untuk state Dashboard ───────────────────────────────────────

data class DashboardState(
    val penjualanBulanIni: Double = 0.0,
    val totalProduk: Int = 0,
    val saldoKas: Double = 0.0,
    val jumlahKasAktif: Int = 0,
    val totalPelanggan: Int = 0,
    val pelangganAktif: Int = 0,
    val penjualanMingguIni: List<Pair<String, Double>> = emptyList(),
    val namaPengguna: String = ""
)

// ─── Screen (stateful) ──────────────────────────────────────────────────────

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val fullname by authViewModel.fullname.collectAsStateWithLifecycle()

    // Dummy state — akan diganti dengan data real seiring integrasi bertahap
    val dummyState = DashboardState(
        penjualanBulanIni = 500_000.0,
        totalProduk = 342,
        saldoKas = 7_000_000.0,
        jumlahKasAktif = 3,
        totalPelanggan = 1248,
        pelangganAktif = 156,
        penjualanMingguIni = listOf(
            "Min" to 3600.0,
            "Sen" to 3200.0,
            "Sel" to 4400.0,
            "Rab" to 4000.0,
            "Kam" to 5200.0,
            "Jum" to 6800.0,
            "Sab" to 4800.0,
        ),
        namaPengguna = fullname
    )

    DashboardContent(
        modifier = modifier,
        state = dummyState,
        onNavigatePenjualan = {},
        onNavigateTambahPenjualan = {},
        onNavigateTambahProduk = {},
        onNavigateTambahPelanggan = {},
        onNavigateTambahPengeluaran = {},
        onViewAllPenjualan = {}
    )
}

// ─── Content (stateless, testable) ──────────────────────────────────────────

@Composable
fun DashboardContent(
    modifier: Modifier = Modifier,
    state: DashboardState,
    onNavigatePenjualan: () -> Unit,
    onNavigateTambahPenjualan: () -> Unit,
    onNavigateTambahProduk: () -> Unit,
    onNavigateTambahPelanggan: () -> Unit,
    onNavigateTambahPengeluaran: () -> Unit,
    onViewAllPenjualan: () -> Unit,
) {
    val formattedDate = Date().toIndonesianFormattedDate()
    val initials = state.namaPengguna.getInitials()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .verticalScroll(rememberScrollState())
    ) {
        // ── HEADER SECTION (green background) ───────────────────────────
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
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Title row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Dashboard",
                        fontSize = 30.sp,
                        lineHeight = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = Color(0xFFDBEAFE),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = formattedDate,
                            fontSize = 14.sp,
                            color = Color(0xFFDBEAFE)
                        )
                    }
                }
                // Avatar / Foto Profil
                CircleContainer(
                    size = 48.dp,
                    backgroundColor = GreenSecondary
                ) {
                    Text(
                        text = initials,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            // ── SUMMARY CARDS GRID ───────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DashboardCard(
                        titleIcon = R.drawable.penjualan_graph,
                        title = "Penjualan Bulan Ini",
                        value = formatRupiah(state.penjualanBulanIni),
                        statsLabelIcon = Icons.Default.ArrowUpward,
                        statsLabel = "12.5%",
                        statsLabelColor = Color(0xFF86EFAC),
                        modifier = Modifier.weight(1f)
                    )
                    DashboardCard(
                        titleIcon = R.drawable.kas,
                        title = "Saldo Kas",
                        value = formatRupiah(state.saldoKas),
                        statsLabel = "${state.jumlahKasAktif} akun",
                        statsLabelColor = Color(0xFFBFDBFE),
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DashboardCard(
                        titleIcon = R.drawable.produk,
                        title = "Produk",
                        value = state.totalProduk.toString(),
                        valueFontSize = 24.sp,
                        modifier = Modifier.weight(1f)
                    )
                    DashboardCard(
                        titleIcon = R.drawable.pelanggan,
                        title = "Pelanggan",
                        value = formatNumber(state.totalPelanggan),
                        valueFontSize = 24.sp,
                        statsLabel = "${state.pelangganAktif} aktif",
                        statsLabelColor = Color(0xFF86EFAC),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // ── MAIN CONTENT AREA ────────────────────────────────────────────
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // ── QUICK ACTIONS ─────────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Aksi Cepat",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    QuickActionButton(
                        icon = ImageVector.vectorResource(R.drawable.penjualan),
                        label = "Penjualan\nBaru",
                        accentColor = ActionBlue,
                        onClick = onNavigateTambahPenjualan,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        icon = ImageVector.vectorResource(R.drawable.add),
                        label = "Tambah\nProduk",
                        accentColor = ActionGreen,
                        onClick = onNavigateTambahProduk,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        icon = ImageVector.vectorResource(R.drawable.tambah_pelanggan),
                        label = "Tambah\nPelanggan",
                        accentColor = ActionPurple,
                        onClick = onNavigateTambahPelanggan,
                        modifier = Modifier.weight(1f),
                        iconSize = 20.dp
                    )
                    QuickActionButton(
                        icon = ImageVector.vectorResource(R.drawable.kas),
                        label = "Tambah\nPengeluaran",
                        accentColor = ActionOrange,
                        onClick = onNavigateTambahPengeluaran,
                        modifier = Modifier.weight(1f),
                        iconSize = 18.dp
                    )
                }
            }

            // ── CHART SECTION ─────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .padding(21.dp)
            ) {
                WeeklyBarChart(
                    data = state.penjualanMingguIni,
                    maxValue = state.penjualanMingguIni.maxOfOrNull { it.second } ?: 8000.0,
                    onViewAllClick = onViewAllPenjualan
                )
            }
        }
    }
}

// ─── Preview ──────────────────────────────────────────────────────────────
@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun DashboardContentPreview() {
    ProjectPAMTTheme(dynamicColor = false) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            DashboardContent(
                modifier = Modifier.padding(innerPadding),
                state = DashboardState(
                    penjualanBulanIni = 500_000.0,
                    totalProduk = 342,
                    saldoKas = 7_000_000.0,
                    jumlahKasAktif = 3,
                    totalPelanggan = 1248,
                    pelangganAktif = 156,
                    penjualanMingguIni = listOf(
                        "Min" to 3600.0,
                        "Sen" to 3200.0,
                        "Sel" to 4400.0,
                        "Rab" to 4000.0,
                        "Kam" to 5200.0,
                        "Jum" to 6800.0,
                        "Sab" to 4800.0,
                    ),
                    namaPengguna = "Dika Setiawan"
                ),
                onNavigatePenjualan = {},
                onNavigateTambahPenjualan = {},
                onNavigateTambahProduk = {},
                onNavigateTambahPelanggan = {},
                onNavigateTambahPengeluaran = {},
                onViewAllPenjualan = {}
            )
        }
    }
}