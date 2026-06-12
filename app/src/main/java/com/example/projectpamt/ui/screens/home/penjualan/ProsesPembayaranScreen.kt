package com.example.projectpamt.ui.screens.home.penjualan

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.projectpamt.data.model.Pelanggan
import com.example.projectpamt.ui.theme.*
import com.example.projectpamt.ui.utils.DynamicStatusBar
import com.example.projectpamt.ui.utils.ValidationUtils
import com.example.projectpamt.ui.utils.formatRupiah
import com.example.projectpamt.viewmodel.penjualan.CartItem
import com.example.projectpamt.viewmodel.penjualan.PembayaranUiState
import com.example.projectpamt.viewmodel.penjualan.PembayaranViewModel
import kotlinx.serialization.json.Json
import com.example.projectpamt.ui.navigation.ProsesPembayaran
import com.example.projectpamt.ui.navigation.InfoPembayaranBerhasil
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProsesPembayaranScreen(
    modifier: Modifier = Modifier,
    pelanggan: Pelanggan,
    cartItemsJson: String,
    totalHarga: Double,
    viewModel: PembayaranViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val kasList by viewModel.kasList.collectAsStateWithLifecycle()
    val selectedKas by viewModel.selectedKas.collectAsStateWithLifecycle()
    val penerimaanKas by viewModel.penerimaanKas.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val cartItems = remember(cartItemsJson) {
        try {
            Json.decodeFromString<List<CartItem>>(cartItemsJson)
        } catch (e: Exception) {
            emptyList()
        }
    }

    val cashReceivedAmount = remember(penerimaanKas) {
        penerimaanKas.toDoubleOrNull() ?: 0.0
    }

    val formattedPenerimaan = remember(penerimaanKas) {
        ValidationUtils.formatThousandSeparator(penerimaanKas)
    }

    // Set dynamic status bar color to GreenPrimary
    DynamicStatusBar(backgroundColor = GreenPrimary)

    // Handle states
    LaunchedEffect(uiState) {
        if (uiState is PembayaranUiState.Success) {
            val transactionId = (uiState as PembayaranUiState.Success).transactionId
            val formattedDate = SimpleDateFormat("d MMMM yyyy HH:mm", Locale("id", "ID")).format(Date())
            val change = cashReceivedAmount - totalHarga
            
            // Set parameter to clear the cart in PenjualanScreen
            navController.previousBackStackEntry?.savedStateHandle?.set("clear_cart", true)
            
            // Navigate to InfoPembayaranBerhasil and pop ProsesPembayaran Screen
            navController.navigate(
                InfoPembayaranBerhasil(
                    idTransaksi = transactionId,
                    totalPembayaran = totalHarga,
                    kembalian = change,
                    tanggalWaktu = formattedDate
                )
            ) {
                popUpTo<ProsesPembayaran> { inclusive = true }
            }
            viewModel.resetState()
        } else if (uiState is PembayaranUiState.Error) {
            Toast.makeText(context, (uiState as PembayaranUiState.Error).message, Toast.LENGTH_SHORT).show()
            viewModel.resetState()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundSlate)
            .imePadding()
    ) {
        // Scrollable content area that adjusts with keyboard
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // ── HEADER SECTION ────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = GreenPrimary,
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Back button
                Row(
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
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

                // Title and Icon Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // IDR Badge Icon
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "IDR",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column {
                        Text(
                            text = "Pembayaran",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Selesaikan transaksi Anda",
                            fontSize = 14.sp,
                            color = Color(0xFFDCFCE7)
                        )
                    }
                }

                // Total Payment Card (Glassmorphic style)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.1f))
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Total Pembayaran",
                            fontSize = 14.sp,
                            color = Color(0xFFDCFCE7)
                        )
                        Text(
                            text = formatRupiah(totalHarga),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = (-1).sp
                        )
                    }
                }
            }

            // ── ORDER SUMMARY CARD ────────────────────────────────────────────
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderSlate)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Ringkasan pembelian",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    // Cart items list
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        cartItems.forEach { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${item.produk.nama} (${item.quantity}x)",
                                    fontSize = 14.sp,
                                    color = TextDark,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = formatRupiah(item.totalHarga),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextDark
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = BorderSlate, modifier = Modifier.padding(vertical = 4.dp))

                    // Customer info line
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Pelanggan: ",
                            fontSize = 14.sp,
                            color = TextMuted
                        )
                        Text(
                            text = pelanggan.nama,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    }
                }
            }

            // ── PAYMENT DETAILS SECTION ───────────────────────────────────────
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp), // extra padding to clear keyboard space nicely
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderSlate)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // 1. Dropdown Pilih Akun Kas
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Pilih Akun Kas",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        var dropdownExpanded by remember { mutableStateOf(false) }

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .clickable { dropdownExpanded = true }
                                    .padding(horizontal = 16.dp, vertical = 15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = selectedKas?.nama ?: "Pilih Akun Kas...",
                                    fontSize = 16.sp,
                                    color = if (selectedKas != null) TextDark else TextPlaceholder
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = TextMuted
                                )
                            }

                            DropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false },
                                modifier = Modifier.fillMaxWidth(0.85f)
                            ) {
                                kasList.forEach { kas ->
                                    DropdownMenuItem(
                                        text = { Text(kas.nama) },
                                        onClick = {
                                            viewModel.selectKas(kas)
                                            dropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // 2. Input Penerimaan Kas
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Penerimaan Kas",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        OutlinedTextField(
                            value = formattedPenerimaan,
                            onValueChange = { viewModel.onPenerimaanKasChange(it) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    text = "0",
                                    color = TextPlaceholder,
                                    fontSize = 20.sp
                                )
                            },
                            leadingIcon = {
                                Text(
                                    text = "Rp",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark,
                                    modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextDark,
                                unfocusedTextColor = TextDark,
                                cursorColor = GreenPrimary,
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedBorderColor = BorderSlate,
                                focusedBorderColor = GreenPrimary
                            )
                        )

                        // Live change feedback
                        if (penerimaanKas.isNotEmpty()) {
                            if (cashReceivedAmount >= totalHarga) {
                                val change = cashReceivedAmount - totalHarga
                                Text(
                                    text = "Kembalian: ${formatRupiah(change)}",
                                    color = Color(0xFF22C55E),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                                )
                            } else {
                                val shortAmount = totalHarga - cashReceivedAmount
                                Text(
                                    text = "Kurang: ${formatRupiah(shortAmount)}",
                                    color = DangerRed,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Fixed Action Buttons at the bottom
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Button Batal (Left)
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceSlate,
                        contentColor = TextDark
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text(
                        text = "Batal",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Button Proses Pembayaran (Right)
                val isEnabled = uiState !is PembayaranUiState.Loading
                Button(
                    onClick = {
                        viewModel.prosesPembayaran(
                            pelangganId = pelanggan.idPelanggan ?: "",
                            cartItems = cartItems,
                            totalHarga = totalHarga,
                            onSuccess = {}
                        )
                    },
                    enabled = isEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenPrimary,
                        contentColor = Color.White,
                        disabledContainerColor = GreenPrimary.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(2.2f)
                        .height(56.dp)
                ) {
                    if (uiState is PembayaranUiState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Proses Pembayaran",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
