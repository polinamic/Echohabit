package com.echohabit.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.echohabit.app.presentation.navigation.AppNavHost
import com.echohabit.app.presentation.navigation.Routes
import com.echohabit.app.presentation.theme.EchoHabitTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val prefs = getSharedPreferences("echohabit_prefs", Context.MODE_PRIVATE)
        val onboardingComplete = prefs.getBoolean("onboarding_complete", false)

        // Tentukan start destination
        val startDestination = when {
            // Sudah login + sudah onboarding → langsung Dashboard
            auth.currentUser != null && onboardingComplete -> Routes.DASHBOARD
            // Sudah login tapi belum onboarding → ke Access
            auth.currentUser != null && !onboardingComplete -> Routes.ACCESS
            // Belum login → sesuai intent dari WelcomeActivity
            else -> intent.getStringExtra("START_DESTINATION") ?: Routes.LOGIN
        }

        setContent {
            EchoHabitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavHost(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}