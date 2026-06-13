package com.echohabit.app.presentation.awal

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.echohabit.app.MainActivity

/**
 * Lightweight launcher that forwards users into the Compose app shell.
 */
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
