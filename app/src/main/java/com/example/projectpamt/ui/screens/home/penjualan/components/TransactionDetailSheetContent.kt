package com.example.projectpamt.ui.screens.home.penjualan.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectpamt.ui.theme.BorderSlate
import com.example.projectpamt.ui.theme.TextDark
import com.example.projectpamt.ui.theme.TextMuted
import com.example.projectpamt.utils.formatRupiah
import com.example.projectpamt.viewmodel.penjualan.PenjualanWithDetails
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TransactionDetailSheetContent(
    txn: PenjualanWithDetails,
    onClose: () -> Unit
) {
    val dateStr = txn.penjualan.createdAt ?: ""
    val formattedTime = remember(dateStr) {
        try {
            val date =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("id", "ID")).parse(dateStr)
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
                            delay(duration = 2000.milliseconds)
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
                                    clipboardManager.setText(
                                        AnnotatedString(
                                            id
                                        )
                                    )
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
                DetailInfoRow(
                    label = "No. Telepon",
                    value = txn.pelanggan.telepon,
                    enableCopy = true
                )
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