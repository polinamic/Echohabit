package com.echohabit.app.presentation.dashboard

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echohabit.app.R
import com.echohabit.app.presentation.awal.WelcomeActivity
import com.echohabit.app.presentation.appusage.AppUsageScreen
import com.echohabit.app.presentation.settings.SettingsScreen
import com.google.firebase.auth.FirebaseAuth

sealed class BottomNavItem(val label: String, val icon: Int) {
    object Main     : BottomNavItem("Main",     R.drawable.ic_nav_main)
    object App      : BottomNavItem("App",      R.drawable.ic_nav_app)
    object Settings : BottomNavItem("Settings", R.drawable.ic_nav_settings)
}

@Composable
fun DashboardScreen() {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf(
        BottomNavItem.Main,
        BottomNavItem.App,
        BottomNavItem.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
            ) {
                tabs.forEachIndexed { index, item ->
                    val isSelected = selectedTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.label,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor   = Color(0xFF4A90D9),
                            selectedTextColor   = Color(0xFF4A90D9),
                            unselectedIconColor = Color(0xFFADB5BD),
                            unselectedTextColor = Color(0xFFADB5BD),
                            indicatorColor      = Color(0xFFEAF2FF)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> MainScreen()
                1 -> AppScreen()
                2 -> SettingsScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    // TODO: ganti dengan UI dashboard utama
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text("Main Screen", fontSize = 18.sp, color = Color(0xFF8A94A6))
    }
}

@Composable
fun AppScreen() {
    AppUsageScreen()
}
