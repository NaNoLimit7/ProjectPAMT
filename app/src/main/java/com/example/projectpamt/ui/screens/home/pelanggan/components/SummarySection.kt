package com.example.projectpamt.ui.screens.home.pelanggan.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectpamt.R
import com.example.projectpamt.utils.formatRupiah
import com.example.projectpamt.viewmodel.pelanggan.uistate.AktivitasSummary

@Composable
fun SummarySection(
    summary: AktivitasSummary,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // CARD 1: Total Belanja (Full Width)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 20.dp,
                    spotColor = Color(0x0D1E2430),
                    ambientColor = Color(0x0D1E2430)
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0x4DBECABE))
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // Background overlay decoration
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .align(Alignment.TopEnd)
                        .background(
                            color = Color(0xFF007A45).copy(alpha = 0.05f),
                            shape = RoundedCornerShape(bottomStart = 96.dp)
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(17.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.total_belanja),
                            contentDescription = null,
                            tint = Color(0xFF3E4940),
                            modifier = Modifier.size(19.dp)
                        )
                        Text(
                            text = "Total Belanja",
                            fontSize = 12.sp,
                            color = Color(0xFF3E4940),
                            fontWeight = FontWeight.Normal
                        )
                    }

                    Text(
                        text = formatRupiah(summary.totalBelanja),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF181D18)
                    )
                }
            }
        }

        // Row containing Transaksi & Terakhir Aktif
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // CARD 2: Jumlah Transaksi
            Card(
                modifier = Modifier
                    .weight(1f)
                    .shadow(
                        elevation = 20.dp,
                        spotColor = Color(0x0D1E2430),
                        ambientColor = Color(0x0D1E2430)
                    ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0x4DBECABE))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(17.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.transaksi_pelanggan),
                            contentDescription = null,
                            tint = Color(0xFF3E4940),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Transaksi",
                            fontSize = 12.sp,
                            color = Color(0xFF3E4940),
                            fontWeight = FontWeight.Normal
                        )
                    }

                    Text(
                        text = summary.totalTransaksi.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF181D18)
                    )
                }
            }

            // CARD 3: Terakhir Aktif
            Card(
                modifier = Modifier
                    .weight(1f)
                    .shadow(
                        elevation = 20.dp,
                        spotColor = Color(0x0D1E2430),
                        ambientColor = Color(0x0D1E2430)
                    ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0x4DBECABE))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(17.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.update_time),
                            contentDescription = null,
                            tint = Color(0xFF3E4940),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Terakhir Aktif",
                            fontSize = 12.sp,
                            color = Color(0xFF3E4940),
                            fontWeight = FontWeight.Normal
                        )
                    }

                    Text(
                        text = summary.terakhirAktif,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF181D18)
                    )
                }
            }
        }
    }
}