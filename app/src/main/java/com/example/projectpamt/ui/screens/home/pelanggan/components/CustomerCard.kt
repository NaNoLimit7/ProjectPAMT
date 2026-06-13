package com.example.projectpamt.ui.screens.home.pelanggan.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.example.projectpamt.data.model.Pelanggan
import com.example.projectpamt.ui.theme.GreenMintActive
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.InfoBlueBg
import com.example.projectpamt.ui.theme.InfoBlueText
import com.example.projectpamt.ui.theme.TextMuted
import com.example.projectpamt.utils.formatRupiah

@Composable
fun CustomerCard(
    pelanggan: Pelanggan,
    onActivityClick: () -> Unit = {},
    onEditClick: () -> Unit = {}
) {
    // Tentukan inisial nama
    val initial = pelanggan.nama.take(1).uppercase()

    // Tentukan warna status badge
    val badgeBg = if (pelanggan.aktif) InfoBlueBg else Color(0xFFF3F4F6)
    val badgeText = if (pelanggan.aktif) InfoBlueText else Color(0xFF4B5563)
    val badgeLabel = if (pelanggan.aktif) "Aktif" else "Non-Aktif"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF3F4F6)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(21.dp)
        ) {
            // ── TOP SECTION (AVATAR + DETAILS) ──────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Avatar Lingkaran
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(GreenMintActive),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initial,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    )
                }

                // Info Pelanggan
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Nama & Status Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = pelanggan.nama,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )

                        Box(
                            modifier = Modifier
                                .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                                .background(badgeBg)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = badgeLabel,
                                fontSize = 12.sp,
                                color = badgeText,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }

                    // Nomor Telepon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // TODO: Gunakan icon telepon dari R.drawable jika tersedia
                        Text(
                            text = pelanggan.telepon,
                            fontSize = 14.sp,
                            color = TextMuted
                        )
                    }
                }
            }

            // Divider Pemisah
            HorizontalDivider(
                modifier = Modifier.padding(top = 16.dp),
                color = Color(0xFFF3F4F6),
                thickness = 1.dp
            )

            // ── BOTTOM SECTION (BUTTONS) ────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Tombol Aktivitas
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                        .background(GreenMintActive)
                        .clickable { onActivityClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.history),
                            contentDescription = "Aktivitas",
                            tint = GreenPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Aktivitas",
                            fontSize = 14.sp,
                            color = GreenPrimary,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                // Tombol Edit
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                        .background(GreenPrimary)
                        .clickable { onEditClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.edit),
                            contentDescription = "Aktivitas",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )

                        Text(
                            text = "Edit",
                            fontSize = 14.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}