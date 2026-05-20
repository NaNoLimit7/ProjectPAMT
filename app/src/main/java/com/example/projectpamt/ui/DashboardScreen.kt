package com.example.projectpamt.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectpamt.R
import com.example.projectpamt.ui.theme.ProjectPAMTTheme

@Composable
fun DashboardScreen(
    onLogoutClick: () -> Unit,
    fullname: String,
    email: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF00754A),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Dashboard",
                        fontSize = 30.sp,
                        lineHeight = 36.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFFFFFF),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = Color(0xFFDBEAFE),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Senin, 20 Mei 2024",
                            fontSize = 14.sp,
                            color = Color(0xFFDBEAFE),
                        )
                    }
                }

                CircleContainer(
                    size = 48.dp,
                    backgroundColor = Color(0xFF53A285)
                ) {
                    Text(
                        text = "JD",
                        fontSize = 18.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFFFFFFFF),
                    )
                }

            }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    modifier = Modifier.height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DashboardCard(
                        titleIcon = R.drawable.penjualan,
                        title = "Penjualan Bulan ini",
                        value = "Rp500.000",
                        statsLabelIcon = Icons.Default.ArrowUpward,
                        statsLabel = "12.5% vs bulan lalu",
                        statsLabelColor = Color(0xFF86EFAC),
                        modifier = Modifier.weight(1f)
                    )
                    DashboardCard(
                        titleIcon = R.drawable.kas,
                        title = "Saldo Seluruh Kas",
                        value = "Rp7.000.000",
                        statsLabel = "3 Akun",
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DashboardCard(
                        titleIcon = R.drawable.produk,
                        title = "Produk",
                        value = "342",
                        valueFontSize = 24.sp,
                        modifier = Modifier.weight(1f)
                    )
                    DashboardCard(
                        titleIcon = R.drawable.pelanggan,
                        title = "Pelanggan",
                        value = "1.248",
                        valueFontSize = 22.sp,
                        statsLabelIcon = Icons.Default.ArrowUpward,
                        statsLabel = "24 baru",
                        statsLabelColor = Color(0xFF86EFAC),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardCard(
    modifier: Modifier = Modifier,
    titleIcon: Int,
    title: String,
    value: String,
    valueFontSize: TextUnit = 16.sp,
    statsLabelIcon: ImageVector? = null,
    statsLabel: String? = null,
    statsLabelColor: Color? = null
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(
                color = Color(0x1AFFFFFF),
                shape = RoundedCornerShape(size = 16.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0x33FFFFFF),
                shape = RoundedCornerShape(size = 16.dp)
            )
            .padding(16.dp)

    ) {

        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircleContainer(
                    size = 32.dp,
                    backgroundColor = Color(0x33FFFFFF)
                ) {
                    Image(
                        painter = painterResource(id = titleIcon),
                        contentDescription = null,
                    )
                }
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFFDBEAFE),
                )
            }

            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = valueFontSize,
                fontWeight = FontWeight(700),
                color = Color(0xFFFFFFFF),
            )
            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
                    if (statsLabelIcon != null)
                        Icon(
                            imageVector = statsLabelIcon,
                            contentDescription = null,
                            tint = statsLabelColor ?: Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(12.dp)
                        )

                    if (statsLabel != null)
                        Text(
                            text = statsLabel,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight(400),
                            color = statsLabelColor ?: Color.White.copy(alpha = 0.8f),
                        )
                }
            }
        }
    }
}

@Composable
fun CircleContainer(
    size: Dp,
    backgroundColor: Color,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(color = backgroundColor),
        contentAlignment = Alignment.Center,
        content = content
    )
}


@Preview(showSystemUi = true)
@Composable
private fun DashboardScreenPreview() {
    ProjectPAMTTheme {
        Scaffold(Modifier.fillMaxSize()) { innerPadding ->
            DashboardScreen(
                onLogoutClick = {},
                fullname = "John Doe",
                email = "john.doe@example.com",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}