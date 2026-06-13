package com.echohabit.app.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.echohabit.app.presentation.dashboard.DashboardScreen

/**
 * Root navigation graph for EchoHabit.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.LOGIN) {
            PlaceholderRouteScreen(
                title = "Welcome to EchoHabit",
                actionLabel = "Continue",
                onAction = { navController.navigate(Routes.ACCESS) }
            )
        }
        composable(Routes.ACCESS) {
            PlaceholderRouteScreen(
                title = "Enable Access",
                actionLabel = "Open Dashboard",
                onAction = { navController.navigate(Routes.DASHBOARD) }
            )
        }
        composable(Routes.DASHBOARD) {
            DashboardScreen()
        }
    }
}

@Composable
private fun PlaceholderRouteScreen(
    title: String,
    actionLabel: String,
    onAction: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Button(
            modifier = Modifier.padding(top = 24.dp),
            onClick = onAction
        ) {
            Text(actionLabel)
        }
    }
}
