package com.example.projectpamt.ui.screens.home.pengeluaran.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.projectpamt.data.model.Pengeluaran
import com.example.projectpamt.ui.theme.BorderSlate
import com.example.projectpamt.ui.theme.DangerRed
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.TextDark
import com.example.projectpamt.ui.theme.TextMuted
import com.example.projectpamt.ui.utils.formatIsoDate
import com.example.projectpamt.ui.utils.formatRupiah

@Composable
fun ExpenseItem(
    expense: Pengeluaran,
    onClick: () -> Unit
) {
    val categoryName = expense.kategori?.name ?: "Umum"
    val icon = ImageVector.vectorResource(R.drawable.harga_modal)
    val tintColor = GreenPrimary
    val bgIconColor = Color(0xFFE6F4EA)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                spotColor = Color(0x051E2430),
                ambientColor = Color(0x051E2430)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderSlate)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon Box
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        bgIconColor,
                        androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = categoryName,
                    tint = tintColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Description and Cash used
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = expense.deskripsi ?: "Pengeluaran",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                val dateStr = formatIsoDate(expense.createdAt)
                val kasStr = expense.kas?.nama ?: "Kas Utama"
                Text(
                    text = "$dateStr • $kasStr",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            // Amount and Category tag
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "- ${formatRupiah(expense.total)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DangerRed
                )
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFF1F5F9),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(100.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = categoryName,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                }
            }
        }
    }
}