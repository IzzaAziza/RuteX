package com.ibaiqzzaaziza.rutex.screen

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.network.GoogleSignInResult
import com.ibaiqzzaaziza.rutex.network.UserDataStore
import com.ibaiqzzaaziza.rutex.network.signInGoogle
import kotlinx.coroutines.launch

class GoogleSignInState(
    val isLoading: Boolean,
    val signIn: () -> Unit
)

/**
 * Dipakai bersama oleh layar Login dan Register.
 * signInGoogle adalah suspend function, jadi dipanggil dari coroutine.
 */
@Composable
fun rememberGoogleSignIn(onSuccess: () -> Unit): GoogleSignInState {
    val context = LocalContext.current
    val dataStore = remember { UserDataStore(context) }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    return GoogleSignInState(isLoading = isLoading) {
        if (isLoading) return@GoogleSignInState
        scope.launch {
            isLoading = true
            val hasil = signInGoogle(context, dataStore)
            isLoading = false

            val pesan = when (hasil) {
                GoogleSignInResult.SUCCESS -> R.string.login_google_success
                GoogleSignInResult.NO_CLIENT_ID -> R.string.error_google_client_id
                GoogleSignInResult.FAILED -> R.string.error_google_login
                // Pengguna menutup pilihan akun, tidak perlu pesan apa pun
                GoogleSignInResult.CANCELLED -> null
            }
            if (pesan != null) {
                Toast.makeText(context, pesan, Toast.LENGTH_SHORT).show()
            }
            if (hasil == GoogleSignInResult.SUCCESS) {
                onSuccess()
            }
        }
    }
}
