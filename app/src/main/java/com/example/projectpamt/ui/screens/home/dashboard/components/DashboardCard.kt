package com.example.projectpamt.ui.screens.home.dashboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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