package com.example.projectpamt.ui.screens.home.produk.components

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.projectpamt.R
import com.example.projectpamt.data.model.Produk
import com.example.projectpamt.ui.theme.GreenMintActive
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.InfoBlueBg
import com.example.projectpamt.ui.theme.InfoBlueText
import com.example.projectpamt.ui.theme.TextDark
import com.example.projectpamt.ui.theme.TextMuted
import com.example.projectpamt.ui.theme.WarningOrangeBg
import com.example.projectpamt.ui.theme.WarningOrangeText
import com.example.projectpamt.utils.formatRupiah

@Composable
fun InventoryProductCard(
    produk: Produk,
    modifier: Modifier = Modifier,
    onDetailClick: () -> Unit = {},
    onEditClick: () -> Unit = {}
) {
    // Tentukan warna badge stok: biru jika stok >= 10, oranye jika sisa sedikit (< 10)
    val isLowStock = produk.stok < 10
    val badgeBg = if (isLowStock) WarningOrangeBg else InfoBlueBg
    val badgeText = if (isLowStock) WarningOrangeText else InfoBlueText
    val badgeLabel = "${produk.stok.toInt()} ${produk.namaSatuan.uppercase()}"

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF3F4F6)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // ── TOP SECTION (IMAGE + DETAILS) ───────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Gambar Thumbnail Produk
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                        .background(GreenMintActive),
                    contentAlignment = Alignment.Center
                ) {
                    SubcomposeAsyncImage(
                        model = produk.imageUrl,
                        contentDescription = produk.nama,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFFF1F5F9))
                            )
                        },
                        error = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFFF1F5F9)),
                                contentAlignment = Alignment.Center
                            ) {
                                // TODO: Gunakan icon fallback gambar jika ada di R.drawable
                                Text(
                                    text = produk.nama.take(1),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenPrimary
                                )
                            }
                        }
                    )
                }

                // Info Produk
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = produk.nama,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 20.sp
                    )

                    Text(
                        text = formatRupiah(produk.harga),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = GreenPrimary,
                        lineHeight = 24.sp
                    )

                    // Row Stok
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Ikon stok dari R.drawable
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.stok),
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(14.dp)
                        )

                        Text(
                            text = "Stok:",
                            fontSize = 12.sp,
                            color = TextMuted
                        )

                        Box(
                            modifier = Modifier
                                .clip(androidx.compose.foundation.shape.RoundedCornerShape(9999.dp))
                                .background(badgeBg)
                                .padding(horizontal = 11.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = badgeLabel,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = badgeText,
                                letterSpacing = 0.25.sp
                            )
                        }
                    }
                }
            }

            // Divider Pemisah
            HorizontalDivider(
                modifier = Modifier.padding(top = 16.dp),
                color = Color(0xFFF9FAFB),
                thickness = 1.dp
            )

            // ── BOTTOM SECTION (BUTTONS) ────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 17.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Tombol Detail
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                        .background(GreenMintActive)
                        .clickable { onDetailClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // TODO: Gunakan icon detail dari R.drawable jika tersedia
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.view),
                            contentDescription = null,
                            tint = GreenPrimary,
                        )
                        Text(
                            text = "Detail",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenPrimary
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
                        // TODO: Gunakan icon edit dari R.drawable jika tersedia
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.edit),
                            contentDescription = null,
                            tint = Color.White,
                        )
                        Text(
                            text = "Edit",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}