package com.example.projectpamt.ui.screens.home.pelanggan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectpamt.R
import com.example.projectpamt.utils.formatRupiah
import com.example.projectpamt.viewmodel.pelanggan.uistate.PelangganAktivitas

@Composable
fun TransactionItemRow(
    aktivitas: PelangganAktivitas,
    modifier: Modifier = Modifier
) {
    val iconBg = Color(0xFF8AF5B3).copy(alpha = 0.3f)
    val iconRes = R.drawable.riwayat_transaksi
    val iconTint = Color(0xFF007A45)

    val badgeBg = Color(0xFF8AF5B3).copy(alpha = 0.4f)
    val badgeDot = Color(0xFF007A45)
    val badgeText = Color(0xFF007242)
    val badgeLabel = "Selesai"

    val amountColor = Color(0xFF181D18)
    val formattedAmount = formatRupiah(aktivitas.total)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left Circle Icon Container
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(iconRes),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Middle Text Column
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = aktivitas.idAktivitas,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF181D18),
                    letterSpacing = 0.28.sp
                )

                Text(
                    text = aktivitas.tanggal,
                    fontSize = 12.sp,
                    color = Color(0xFF3E4940)
                )

                // Dot Status Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(9999.dp))
                        .background(badgeBg)
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(badgeDot)
                    )
                    Text(
                        text = badgeLabel,
                        fontSize = 10.sp,
                        color = badgeText,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }

        // Right Text Column
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = formattedAmount,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
            Text(
                text = "${aktivitas.jumlahItem} item",
                fontSize = 12.sp,
                color = Color(0xFF3E4940)
            )
        }
    }
}