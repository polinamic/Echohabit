package com.echohabit.app.core.utils

import com.google.firebase.auth.FirebaseAuth

/**
 * Returns FirebaseAuth when Firebase has been configured for this build.
 */
fun firebaseAuthOrNull(): FirebaseAuth? {
    return try {
        FirebaseAuth.getInstance()
    } catch (_: IllegalStateException) {
        null
    }
}
