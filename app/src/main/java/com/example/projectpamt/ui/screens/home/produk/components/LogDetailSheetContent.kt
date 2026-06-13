package com.example.projectpamt.ui.screens.home.produk.components

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
import com.example.projectpamt.data.model.LogInventory
import com.example.projectpamt.ui.theme.BorderSlate
import com.example.projectpamt.ui.theme.DangerRed
import com.example.projectpamt.ui.theme.TextDark
import com.example.projectpamt.ui.theme.TextMuted
import com.example.projectpamt.ui.utils.formatRupiah
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun LogDetailSheetContent(
    log: LogInventory,
    onClose: () -> Unit
) {
    val dateStr = log.updatedAt ?: ""
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
                            delay(2000.milliseconds)
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