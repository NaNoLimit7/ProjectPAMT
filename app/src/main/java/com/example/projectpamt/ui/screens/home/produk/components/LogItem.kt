package com.example.projectpamt.ui.screens.home.produk.components

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
import com.example.projectpamt.data.model.LogInventory
import com.example.projectpamt.ui.theme.BorderSlate
import com.example.projectpamt.ui.theme.DangerRed
import com.example.projectpamt.ui.theme.GreenMintActive
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.TextDark
import com.example.projectpamt.ui.theme.TextMuted
import com.example.projectpamt.ui.theme.TextPlaceholder
import com.example.projectpamt.utils.Quadruple
import com.example.projectpamt.utils.formatRupiah
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun LogItem(
    log: LogInventory,
    onClick: () -> Unit
) {
    val dateStr = log.updatedAt ?: ""
    val formattedTime = remember(dateStr) {
        com.example.projectpamt.ui.utils.DateTimeUtils.formatIso(dateStr, "d MMMM yyyy, HH:mm")
    }

    val diff = log.stokBaru - log.stokLama

    val (badgeText, badgeColor, badgeBg, actionLabel) = when {
        diff > 0 -> {
            val d = diff.toInt()
            val text = if (diff == d.toDouble()) "+$d" else "+$diff"
            Quadruple(text, Color(0xFF2E7D32), Color(0xFFE8F5E9), "Stok Masuk")
        }

        diff < 0 -> {
            val d = (-diff).toInt()
            val text = if (diff == d.toDouble()) "-$d" else "-$diff"
            Quadruple(text, DangerRed, Color(0xFFFFEBEE), "Stok Keluar")
        }

        else -> {
            Quadruple("Update", TextMuted, BorderSlate, "Update Produk")
        }
    }

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
                // Icon Box
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(GreenMintActive),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.produk),
                        contentDescription = null,
                        tint = GreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Information details
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
                            text = log.namaLama,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        // Status Action Badge
                        Box(
                            modifier = Modifier
                                .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                                .background(badgeBg)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = actionLabel,
                                color = if (diff == 0.0) TextDark else badgeColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "ID Log: #${log.idLogInventory?.take(8)?.uppercase() ?: ""}",
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

            // Right stock delta and price info
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (diff != 0.0) {
                    Text(
                        text = badgeText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = badgeColor
                    )
                }
                Text(
                    text = formatRupiah(log.hargaLama),
                    fontSize = 12.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}