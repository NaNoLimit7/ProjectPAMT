package com.example.projectpamt.ui.screens.home.kas.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectpamt.R
import com.example.projectpamt.data.model.LogKasItem
import com.example.projectpamt.ui.theme.BorderSlate
import com.example.projectpamt.ui.theme.DangerRed
import com.example.projectpamt.ui.theme.GreenPrimary
import com.example.projectpamt.ui.theme.TextDark
import com.example.projectpamt.ui.theme.TextMuted
import com.example.projectpamt.utils.buildAnnotatedLogDescription
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun LogEntryCard(
    logItem: LogKasItem
) {
    val dateStr = logItem.updatedAt
    val formattedDate = remember(dateStr) {
        com.example.projectpamt.ui.utils.DateTimeUtils.formatIso(dateStr, "d MMMM, HH:mm")
    }

    // Determine icon details based on log activity type
    val (icon, iconTint, iconBg) = when (logItem.tipeAktivitas) {
        "Pembuatan Akun" -> Triple(
            ImageVector.vectorResource(R.drawable.add),
            GreenPrimary,
            Color(0xFF94F7B6)
        )

        "Saldo disesuaikan oleh Admin" -> Triple(
            ImageVector.vectorResource(R.drawable.edit),
            GreenPrimary,
            Color(0xFF8AF5B3)
        )

        "Status berubah menjadi Tidak Aktif" -> Triple(
            Icons.Default.Warning,
            DangerRed,
            Color(0xFFFFDAD6)
        )

        "Status berubah menjadi Aktif" -> Triple(
            Icons.Default.Check,
            GreenPrimary,
            Color(0xFFD1E8DB)
        )

        else -> Triple(
            ImageVector.vectorResource(R.drawable.update_time),
            TextMuted,
            Color(0xFFE5E7EB)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(20.dp)),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderSlate)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left Icon Box
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Right Information Box
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Title & Date Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = logItem.tipeAktivitas,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = formattedDate,
                        fontSize = 12.sp,
                        color = Color(0xFF3E4940),
                        fontWeight = FontWeight.Normal
                    )
                }

                // Description Text with highlighted money amounts
                Text(
                    text = buildAnnotatedLogDescription(logItem.detailKeterangan),
                    fontSize = 14.sp,
                    color = Color(0xFF3E4940),
                    lineHeight = 20.sp
                )

                // Author Footer
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.profil),
                        contentDescription = null,
                        tint = Color(0xFF6E7A70),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Oleh: ${logItem.pelaku}",
                        fontSize = 13.sp,
                        color = Color(0xFF6E7A70),
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}