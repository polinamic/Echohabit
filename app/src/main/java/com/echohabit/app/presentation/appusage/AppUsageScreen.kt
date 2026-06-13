package com.echohabit.app.presentation.appusage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echohabit.app.domain.model.AppUsage

/**
 * App Usage Detail Screen showing all applications and their usage.
 * Provides detailed breakdown of time spent in each app.
 */
@Composable
fun AppUsageScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Text(
            text = "App Usage Details",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E3A8A)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Mock app usage data
        val mockApps = listOf(
            AppUsage(
                appName = "Instagram",
                packageName = "com.instagram.android",
                durationMs = 2 * 60 * 60 * 1000 + 57 * 60 * 1000,
                sessionCount = 8
            ),
            AppUsage(
                appName = "WhatsApp",
                packageName = "com.whatsapp",
                durationMs = 1 * 60 * 60 * 1000 + 7 * 60 * 1000,
                sessionCount = 15
            ),
            AppUsage(
                appName = "Spotify",
                packageName = "com.spotify.music",
                durationMs = 57 * 60 * 1000,
                sessionCount = 3
            ),
            AppUsage(
                appName = "YouTube",
                packageName = "com.google.android.youtube",
                durationMs = 45 * 60 * 1000,
                sessionCount = 5
            ),
            AppUsage(
                appName = "Chrome",
                packageName = "com.android.chrome",
                durationMs = 30 * 60 * 1000,
                sessionCount = 12
            )
        )

        val totalTime = mockApps.sumOf { it.durationMs }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mockApps) { app ->
                AppDetailItem(app = app, totalTimeMs = totalTime)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Detailed app item with more information
 */
@Composable
fun AppDetailItem(
    app: AppUsage,
    totalTimeMs: Long
) {
    val percentage = app.getPercentage(totalTimeMs)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = app.appName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E3A8A)
                )

                Text(
                    text = app.getFormattedDuration(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3B82F6)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFE0E7FF))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(percentage)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF60A5FA))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${(percentage * 100).toInt()}% of total time",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                Text(
                    text = "${app.sessionCount} sessions",
                    fontSize = 12.sp,
                    color = Color(0xFF93C5FD),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppUsageScreenPreview() {
    AppUsageScreen()
}
