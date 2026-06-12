package com.example.projectpamt.ui.screens.home.penjualan

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projectpamt.R
import com.example.projectpamt.ui.navigation.Dashboard
import com.example.projectpamt.ui.navigation.PenjualanList
import com.example.projectpamt.ui.theme.*
import com.example.projectpamt.ui.utils.DynamicStatusBar
import com.example.projectpamt.ui.utils.formatRupiah

@Composable
fun PembayaranBerhasilScreen(
    modifier: Modifier = Modifier,
    idTransaksi: String,
    totalPembayaran: Double,
    kembalian: Double,
    tanggalWaktu: String,
    navController: NavController
) {
    // Intercept back button to navigate to Penjualan Screen and clear cart
    BackHandler(enabled = true) {
        navController.navigate(PenjualanList) {
            popUpTo(PenjualanList) { inclusive = true }
        }
    }

    // Status bar matches the background color of success page
    DynamicStatusBar(backgroundColor = BackgroundSlate)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // ── SUCCESS HEADER ────────────────────────────────────────────────
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Success Badge Checkmark
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00C853)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Pembayaran Berhasil!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00C853),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Transaksi berhasil diselesaikan",
                    fontSize = 14.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            }
        }

        // ── TRANSACTION RECEIPT CARD ──────────────────────────────────────
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSlate)
        ) {
            Column(
                modifier = Modifier.padding(21.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // ID Transaksi
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFDCFCE7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.riwayat_transaksi),
                            contentDescription = null,
                            tint = Color(0xFF00754A),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ID Transaksi",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = idTransaksi,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark,
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
                                contentDescription = "Salin ID Transaksi",
                                tint = if (copied) Color(0xFF2E7D32) else TextMuted,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable {
                                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(idTransaksi))
                                        copied = true
                                    }
                            )
                        }
                    }
                }

                HorizontalDivider(color = BorderSlate)

                // Receipt Details
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Total Pembayaran
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Pembayaran",
                            fontSize = 14.sp,
                            color = TextMuted
                        )
                        Text(
                            text = formatRupiah(totalPembayaran),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    }

                    // Kembalian
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Kembalian",
                            fontSize = 14.sp,
                            color = TextMuted
                        )
                        Text(
                            text = formatRupiah(kembalian),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00C853)
                        )
                    }

                    // Tanggal & Waktu
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "Tanggal & Waktu",
                            fontSize = 14.sp,
                            color = TextMuted
                        )
                        Text(
                            text = tanggalWaktu,
                            fontSize = 14.sp,
                            color = TextDark,
                            textAlign = TextAlign.End
                        )
                    }
                }

                // Success Message Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF0FDF4))
                        .border(BorderStroke(1.dp, Color(0xFFDCFCE7)), RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF166534),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Pembayaran telah diterima dan dicatat ke akun kas",
                            fontSize = 13.sp,
                            color = Color(0xFF166534),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // ── ACTION BUTTONS AREA (Scrolls with screen content) ─────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Button Transaksi Baru (Solid green, white text)
            Button(
                onClick = {
                    navController.navigate(PenjualanList) {
                        popUpTo(PenjualanList) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00754A),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.penjualan_graph),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Transaksi Baru",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Button Kembali ke Dashboard (Text button style)
            TextButton(
                onClick = {
                    navController.navigate(Dashboard) {
                        popUpTo(Dashboard) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.dashboard),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color(0xFF374151)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Kembali ke Dashboard",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF374151)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
