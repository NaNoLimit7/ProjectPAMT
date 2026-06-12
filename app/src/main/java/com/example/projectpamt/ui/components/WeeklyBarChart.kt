package com.example.projectpamt.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.TextPlaceholder

/**
 * Histogram batang mingguan untuk menampilkan data penjualan per hari (Min–Sab).
 *
 * @param data          Map dari nama hari pendek ke nilai (misal: "M" to 4500.0).
 * @param maxValue      Nilai maksimum Y-axis untuk menormalisasi tinggi batang.
 * @param onViewAllClick Callback saat "View All" ditekan.
 */
@Composable
fun WeeklyBarChart(
    data: List<Pair<String, Double>>,
    maxValue: Double,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val yLabels = listOf("8000", "6000", "4000", "2000", "0")
    val chartMaxHeight = 160.dp
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = modifier.fillMaxWidth()) {
        // Header: judul + "View All"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Penjualan Minggu Ini",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
            Text(
                text = "View All",
                fontSize = 10.sp,
                color = GreenPrimary,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onViewAllClick
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Chart body: Y-labels + Bars
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartMaxHeight + 24.dp), // +24 for x-axis labels
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Y-axis labels
            Column(
                modifier = Modifier
                    .width(32.dp)
                    .height(chartMaxHeight),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                yLabels.forEach { label ->
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        color = TextPlaceholder,
                        lineHeight = 14.sp
                    )
                }
            }

            // Grid + bars
            Box(modifier = Modifier.weight(1f).height(chartMaxHeight + 24.dp)) {
                // Dashed grid lines
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(chartMaxHeight),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(4) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFFE2E8F0))
                        )
                    }
                    // Solid baseline
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFCBD5E1))
                    )
                }

                // Bars + X-axis labels
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(chartMaxHeight + 24.dp)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    data.forEach { (day, value) ->
                        val fraction = if (maxValue > 0) (value / maxValue).coerceIn(0.0, 1.0) else 0.0
                        val barHeight = (chartMaxHeight.value * fraction).dp

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.height(chartMaxHeight + 24.dp)
                        ) {
                            // Spacer to push bar to the bottom of the chart area
                            Spacer(modifier = Modifier.weight(1f))
                            Box(
                                modifier = Modifier
                                    .width(28.dp)
                                    .height(barHeight.coerceAtLeast(4.dp))
                                    .background(
                                        color = GreenPrimary,
                                        shape = RoundedCornerShape(
                                            topStart = 6.dp,
                                            topEnd = 6.dp,
                                            bottomStart = 0.dp,
                                            bottomEnd = 0.dp
                                        )
                                    )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = day,
                                fontSize = 10.sp,
                                color = TextPlaceholder,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
