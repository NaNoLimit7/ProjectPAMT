package com.example.projectpamt.ui.screens.home.produk.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectpamt.data.model.Produk
import com.example.projectpamt.ui.utils.ValidationUtils

@Composable
fun DetailHargaStokSection(
    produk: Produk,
    hargaModalDouble: Double,
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
            Text(
                text = "Harga & Stok",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF181D18)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Harga Modal", fontSize = 12.sp, color = Color(0xFF3E4940), modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .border(BorderStroke(1.dp, Color(0xFFBECABE)), RoundedCornerShape(12.dp))
                                .background(Color(0xFFF9FAFB))
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(text = "Rp " + ValidationUtils.formatThousandSeparator(hargaModalDouble.toLong().toString()), fontSize = 14.sp, color = Color(0xFF3E4940))
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Harga Jual", fontSize = 12.sp, color = Color(0xFF3E4940), modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .border(BorderStroke(1.dp, Color(0xFFBECABE)), RoundedCornerShape(12.dp))
                                .background(Color(0xFFF9FAFB))
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(text = "Rp " + ValidationUtils.formatThousandSeparator(produk.harga.toLong().toString()), fontSize = 14.sp, color = Color(0xFF3E4940))
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Stok Awal", fontSize = 12.sp, color = Color(0xFF3E4940), modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .border(BorderStroke(1.dp, Color(0xFFBECABE)), RoundedCornerShape(12.dp))
                                .background(Color(0xFFF9FAFB))
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(text = produk.stok.toInt().toString(), fontSize = 14.sp, color = Color(0xFF3E4940))
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Satuan", fontSize = 12.sp, color = Color(0xFF3E4940), modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .border(BorderStroke(1.dp, Color(0xFFBECABE)), RoundedCornerShape(12.dp))
                                .background(Color(0xFFF9FAFB))
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(text = produk.namaSatuan, fontSize = 14.sp, color = Color(0xFF3E4940))
                        }
                    }
                }
            }
        }
    }
}
