package com.example.projectpamt.ui.screens.home.produk.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectpamt.R

@Composable
fun DetailDescriptionSection(
    mainParagraphs: List<String>,
    specBullets: List<String>,
    createdAt: String?,
    descText: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color(0xFFE5E7EB)), RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.deskripsi),
                    contentDescription = null,
                    tint = Color(0xFF181D18),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Deskripsi Produk",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF181D18)
                )
            }

            HorizontalDivider(color = Color(0xFFDFE4DD), thickness = 1.dp)

            if (mainParagraphs.isNotEmpty()) {
                Text(
                    text = mainParagraphs.joinToString("\n"),
                    fontSize = 14.sp,
                    color = Color(0xFF3E4940),
                    lineHeight = 20.sp
                )
            } else if (descText.isBlank()) {
                Text(
                    text = "Tidak ada deskripsi untuk produk ini.",
                    fontSize = 14.sp,
                    color = Color(0xFF6E7A70),
                    lineHeight = 20.sp
                )
            }

            if (specBullets.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    specBullets.forEach { spec ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "•",
                                color = Color(0xFF3E4940),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = spec,
                                color = Color(0xFF3E4940),
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            createdAt?.let {
                HorizontalDivider(color = Color(0x1ABECABE), thickness = 1.dp)
                Text(
                    text = "Terakhir diperbarui: " + it.take(10) + " " + it.drop(11).take(5),
                    fontSize = 12.sp,
                    color = Color(0xFF6E7A70),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
