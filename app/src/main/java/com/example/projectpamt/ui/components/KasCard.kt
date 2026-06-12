package com.example.projectpamt.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.projectpamt.data.model.Kas
import com.example.projectpamt.ui.theme.GreenMintActive
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.TextDark
import com.example.projectpamt.ui.theme.TextMuted
import com.example.projectpamt.ui.utils.formatRupiah

@Composable
fun KasCard(
    kas: Kas,
    modifier: Modifier = Modifier,
    onTransaksiClick: () -> Unit = {},
    onLihatLogClick: () -> Unit = {},
    onEditClick: () -> Unit = {}
) {
    // Status Badge colors
    val badgeBg = if (kas.aktif) GreenMintActive else Color(0xFFE5E7EB)
    val badgeText = if (kas.aktif) Color(0xFF16A34A) else Color(0xFF4B5563)
    val badgeLabel = if (kas.aktif) "Aktif" else "Tidak Aktif"

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF3F4F6)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // ── TOP SECTION (NAME, BADGE, EDIT ICON) ─────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Name and Active Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(13.dp)
                ) {
                    Text(
                        text = kas.nama,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                            .background(badgeBg)
                            .padding(horizontal = 10.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badgeLabel,
                            fontSize = 11.sp,
                            color = badgeText,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Edit Button (Pencil Icon)
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.edit),
                    contentDescription = "Ubah Nama Kas",
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onEditClick() }
                )
            }

            // ── BALANCE SECTION ──────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = formatRupiah(kas.saldo),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreenPrimary,
                    letterSpacing = (-0.7).sp
                )
            }

            // ── UPDATE TIME SECTION ──────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.update_time),
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = kas.updatedAtText ?: "Diperbarui baru-baru ini",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            // ── ACTIONS SECTION (ONLY FOR ACTIVE ACCOUNTS) ───────────────────
            if (kas.aktif) {
                // Divider
                HorizontalDivider(
                    modifier = Modifier.padding(top = 16.dp),
                    color = Color(0xFFF3F4F6),
                    thickness = 1.dp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    // Tombol Transaksi
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(41.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(GreenPrimary)
                            .clickable { onTransaksiClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.transaksi),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Transaksi",
                                fontSize = 13.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Tombol Lihat Log
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(41.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE5E7EB)) // border outline color
                            .padding(1.dp), // border width
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(11.dp))
                                .background(Color(0xFFF9FAFB))
                                .clickable { onLihatLogClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.view),
                                    contentDescription = null,
                                    tint = TextDark,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Lihat Log",
                                    fontSize = 13.sp,
                                    color = TextDark,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
