package com.example.projectpamt.ui.screens.home.pelanggan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.viewmodel.pelanggan.AktivitasFilter

@Composable
fun FilterChipsRow(
    selectedFilter: AktivitasFilter,
    onFilterSelected: (AktivitasFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AktivitasFilter.entries.forEach { filter ->
            val isSelected = filter == selectedFilter
            val label = when (filter) {
                AktivitasFilter.SEMUA_WAKTU -> "Semua Waktu"
                AktivitasFilter.BULAN_INI -> "Bulan Ini"
                AktivitasFilter.TIGA_BULAN_TERAKHIR -> "3 Bulan Terakhir"
                AktivitasFilter.TAHUN_INI -> "Tahun Ini"
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .background(if (isSelected) GreenPrimary else Color.White)
                    .then(
                        if (isSelected) Modifier.Companion else Modifier.border(
                            1.dp,
                            Color(0xFF6E7A70),
                            androidx.compose.foundation.shape.RoundedCornerShape(9999.dp)
                        )
                    )
                    .clickable { onFilterSelected(filter) }
                    .padding(horizontal = 16.dp, vertical = 9.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = if (isSelected) Color.White else Color(0xFF3E4940),
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}