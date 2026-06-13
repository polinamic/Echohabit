package com.echohabit.app.presentation.settings

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.echohabit.app.core.utils.firebaseAuthOrNull
import com.echohabit.app.presentation.awal.WelcomeActivity
import com.echohabit.app.presentation.onboarding.InstalledApp

val NavyBlueS = Color(0xFF1A237E)
val LightBlueS = Color(0xFF4A90D9)

// ── SharedPreferences helpers ─────────────────────────────────────────────────
fun saveExcludedApps(context: Context, excluded: Set<String>) {
    context.getSharedPreferences("echohabit_prefs", Context.MODE_PRIVATE)
        .edit().putStringSet("excluded_apps", excluded).apply()
}

fun loadExcludedApps(context: Context): Set<String> {
    return context.getSharedPreferences("echohabit_prefs", Context.MODE_PRIVATE)
        .getStringSet("excluded_apps", emptySet()) ?: emptySet()
}

// ── Ambil installed apps (sama persis dengan onboarding) ─────────────────────
fun getInstalledAppsForSettings(context: Context): List<InstalledApp> {
    val pm = context.packageManager
    return try {
        pm.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { app ->
                val isSystemApp = app.flags and ApplicationInfo.FLAG_SYSTEM != 0
                val isUpdatedSystemApp = app.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0
                val hasLaunchIntent = pm.getLaunchIntentForPackage(app.packageName) != null
                // Exclude app EcoHabit sendiri dari list
                val isNotSelf = app.packageName != context.packageName
                (!isSystemApp || isUpdatedSystemApp) && hasLaunchIntent && isNotSelf
            }
            .map { app ->
                InstalledApp(
                    packageName = app.packageName,
                    appName = pm.getApplicationLabel(app).toString(),
                    icon = try { pm.getApplicationIcon(app.packageName) } catch (e: Exception) { null }
                )
            }
            .sortedBy { it.appName }
    } catch (e: Exception) { emptyList() }
}

// ── Root ──────────────────────────────────────────────────────────────────────
@Composable
fun SettingsScreen() {
    var currentPage by remember { mutableStateOf("main") }

    when (currentPage) {
        "main"           -> SettingsMainScreen(
            onExcludeClick        = { currentPage = "exclude" },
            onChangePasswordClick = { currentPage = "changePassword" }
        )
        "exclude"        -> SettingsExcludeScreen(onBack = { currentPage = "main" })
        "changePassword" -> ChangePasswordScreen(onBack = { currentPage = "main" })
    }
}

// ── Settings Main ─────────────────────────────────────────────────────────────
@Composable
fun SettingsMainScreen(
    onExcludeClick: () -> Unit,
    onChangePasswordClick: () -> Unit
) {
    val context = LocalContext.current
    val auth = firebaseAuthOrNull()
    val user = auth?.currentUser
    var showLogoutDialog by remember { mutableStateOf(false) }

    val displayName = user?.displayName?.trim()?.ifBlank { null }
        ?: user?.email?.substringBefore("@")?.replaceFirstChar { it.uppercase() }
        ?: "User"
    val email = user?.email ?: ""

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            title = {
                Text(
                    text = "Are you sure you want to log out?",
                    fontWeight = FontWeight.Bold,
                    color = NavyBlueS,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    auth?.signOut()
                    val intent = Intent(context, WelcomeActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)
                }) {
                    Text("Yes", color = NavyBlueS, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("No", color = Color(0xFF8A94A6))
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4FF))
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = displayName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = NavyBlueS
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("💧", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("5", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = LightBlueS)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8EAF6)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = NavyBlueS,
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = displayName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NavyBlueS)
        Text(text = email, fontSize = 13.sp, color = Color(0xFF8A94A6))

        Spacer(modifier = Modifier.height(32.dp))

        SettingsMenuItem(label = "Exclude Application", onClick = onExcludeClick)
        Spacer(modifier = Modifier.height(12.dp))
        SettingsMenuItem(label = "Change Password", onClick = onChangePasswordClick)
        Spacer(modifier = Modifier.height(12.dp))
        SettingsMenuItem(
            label = "Logout",
            onClick = { showLogoutDialog = true },
            textColor = Color(0xFFE53935)
        )
    }
}

@Composable
fun SettingsMenuItem(label: String, onClick: () -> Unit, textColor: Color = Color(0xFF1A1A2E)) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(text = label, fontSize = 15.sp, color = textColor, fontWeight = FontWeight.Medium)
        }
    }
}

// ── Settings Exclude — SAMA PERSIS dengan onboarding ExcludeAppScreen ─────────
@Composable
fun SettingsExcludeScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val installedApps = remember { getInstalledAppsForSettings(context) }
    var excludedApps by remember { mutableStateOf(loadExcludedApps(context)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4FF))
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Choose apps to exclude",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = NavyBlueS,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "You can change this anytime in Settings",
            fontSize = 13.sp,
            color = Color(0xFF8A94A6)
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (installedApps.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No apps found.\nMake sure Usage Access is enabled.",
                    fontSize = 14.sp,
                    color = Color(0xFF8A94A6),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(installedApps) { app ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Icon app dari HP
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF0F4FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                app.icon?.let { drawable ->
                                    androidx.compose.foundation.Image(
                                        bitmap = drawable.toBitmap(40, 40).asImageBitmap(),
                                        contentDescription = app.appName,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = app.appName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1A1A2E),
                                modifier = Modifier.weight(1f)
                            )

                            // Toggle — ON = dikecualikan dari analisis
                            Switch(
                                checked = excludedApps.contains(app.packageName),
                                onCheckedChange = { isExcluded ->
                                    excludedApps = if (isExcluded) {
                                        excludedApps + app.packageName
                                    } else {
                                        excludedApps - app.packageName
                                    }
                                    // Langsung simpan setiap perubahan
                                    saveExcludedApps(context, excludedApps)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor   = Color.White,
                                    checkedTrackColor   = LightBlueS,
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = Color(0xFFCDD5E0)
                                )
                            )
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
        }

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NavyBlueS,
                contentColor = Color.White
            )
        ) {
            Text("Done", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

// ── Change Password ───────────────────────────────────────────────────────────
@Composable
fun ChangePasswordScreen(onBack: () -> Unit) {
    val auth = firebaseAuthOrNull()
    val user = auth?.currentUser
    val email = user?.email ?: ""
    var isSent by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4FF))
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Change\nPassword",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = NavyBlueS,
                lineHeight = 36.sp
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("💧", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("5", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = LightBlueS)
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!isSent) {
                    Text(
                        text = "Reset password link will be\nsent to:",
                        fontSize = 14.sp,
                        color = Color(0xFF555555),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = email,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyBlueS,
                        textAlign = TextAlign.Center
                    )

                    if (errorMsg.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = errorMsg, color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            isLoading = true
                            errorMsg = ""
                            auth?.sendPasswordResetEmail(email)
                                ?.addOnSuccessListener { isLoading = false; isSent = true }
                                ?.addOnFailureListener { e ->
                                    isLoading = false
                                    errorMsg = e.message ?: "Failed to send email"
                                } ?: run {
                                isLoading = false
                                errorMsg = "Firebase is not configured for this build"
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NavyBlueS,
                            contentColor = Color.White
                        ),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Send Email", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Text("✅", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Email Sent!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyBlueS
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Check your inbox at $email and follow the instructions to reset your password.",
                        fontSize = 13.sp,
                        color = Color(0xFF555555),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NavyBlueS,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Back to Settings", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
