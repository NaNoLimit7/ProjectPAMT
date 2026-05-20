package com.example.projectpamt.ui

import android.widget.GridView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                color = Color(0xFF1E58F6),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                )
            )
            .padding(
                top = 48.dp,
                end = 24.dp,
                start = 24.dp,
                bottom = 24.dp
            )
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
                        style = TextStyle(
                            fontSize = 30.sp,
                            lineHeight = 36.sp,
                            fontWeight = FontWeight(700),
                            color = Color(0xFFFFFFFF),
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = Color(0xFFDBEAFE),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Senin, 20 Mei 2024",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color(0xFFDBEAFE),
                            )
                        )
                    }
                }

                CircleContainer(
                    size = 48.dp,
                    backgroundColor = Color(0x8060A5FA)
                ) {
                    Text(
                        text = "JD",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight(600),
                            color = Color(0xFFFFFFFF),
                        )
                    )
                }

            }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = Color(0x1AFFFFFF),
                                shape = RoundedCornerShape(size = 16.dp)
                            )
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 22.dp)

                    ) {
                        Column {
                            Row {
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = Color(0x1AFFFFFF),
                                shape = RoundedCornerShape(size = 16.dp)
                            )
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 22.dp)

                    ) { }
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