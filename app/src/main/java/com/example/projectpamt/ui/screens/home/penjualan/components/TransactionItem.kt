package com.example.projectpamt.ui.screens.home.penjualan.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectpamt.R
import com.example.projectpamt.ui.theme.BorderSlate
import com.example.projectpamt.ui.theme.GreenMintActive
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.TextDark
import com.example.projectpamt.ui.theme.TextMuted
import com.example.projectpamt.ui.theme.TextPlaceholder
import com.example.projectpamt.utils.formatRupiah
import com.example.projectpamt.viewmodel.penjualan.uistate.PenjualanWithDetails
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TransactionItem(
    txn: PenjualanWithDetails,
    onClick: () -> Unit
) {
    val dateStr = txn.penjualan.createdAt ?: ""
    val formattedTime = remember(dateStr) {
        try {
            val date =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("id", "ID")).parse(dateStr)
            if (date != null) {
                SimpleDateFormat("d MMM yyyy, HH:mm", Locale("id", "ID")).format(date)
            } else {
                dateStr
            }
        } catch (_: Exception) {
            dateStr
        }
    }

    val customerName = txn.pelanggan?.nama ?: "Umum (Cash)"
    val itemCount = txn.items.sumOf { it.quantity }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
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
                // Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(GreenMintActive),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.riwayat_transaksi),
                        contentDescription = null,
                        tint = GreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Info details
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
                            text = customerName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        // Status Badge
                        Box(
                            modifier = Modifier
                                .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Selesai",
                                color = Color(0xFF2E7D32),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "ID: #${txn.penjualan.idPenjualan?.take(8)?.uppercase() ?: ""}",
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

            // Right amount & count details
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = formatRupiah(txn.penjualan.totalHarga),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = GreenPrimary
                )
                Text(
                    text = "$itemCount item",
                    fontSize = 12.sp,
                    color = TextMuted
                )
                txn.kas?.let {
                    Text(
                        text = it.nama,
                        fontSize = 10.sp,
                        color = TextPlaceholder,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}