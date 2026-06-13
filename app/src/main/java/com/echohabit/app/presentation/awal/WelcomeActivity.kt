package com.echohabit.app.presentation.awal

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.echohabit.app.MainActivity
import com.echohabit.app.presentation.navigation.Routes

/**
 * Compatibility entry point used by older settings and auth flows.
 */
class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("START_DESTINATION", Routes.LOGIN)
        }
        startActivity(intent)
        finish()
    }
}
