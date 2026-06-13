package com.example.projectpamt.ui.screens.home.pelanggan.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectpamt.viewmodel.pelanggan.uistate.PelangganAktivitas

@Composable
fun TransactionListCard(
    listAktivitas: List<PelangganAktivitas>,
    modifier: Modifier = Modifier
) {
    var showAll by remember(listAktivitas) { mutableStateOf(false) }
    val displayedList = if (showAll) listAktivitas else listAktivitas.take(4)
    val hasMoreThanFour = listAktivitas.size > 4

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .shadow(
                elevation = 20.dp,
                spotColor = Color(0x0D1E2430),
                ambientColor = Color(0x0D1E2430)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0x4DBECABE))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        ) {
            // Card Title Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        border = BorderStroke(1.dp, Color(0x1ABECABE)),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Riwayat Transaksi",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF181D18),
                )
            }

            // List Items Section
            if (displayedList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada transaksi di periode ini",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                displayedList.forEachIndexed { index, aktivitas ->
                    if (index > 0) {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0x33BECABE),
                            thickness = 1.dp
                        )
                    }

                    TransactionItemRow(aktivitas = aktivitas)
                }
            }

            // Footer Link (Pagination / Show More)
            if (hasMoreThanFour) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0x33BECABE),
                    thickness = 1.dp
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showAll = !showAll }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (showAll) "Sembunyikan Transaksi" else "Lihat Semua Transaksi",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF007A45)
                    )
                }
            }
        }
    }
}