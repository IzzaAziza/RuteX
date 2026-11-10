package com.ibaiqzzaaziza.rutex.network

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.ibaiqzzaaziza.rutex.BuildConfig
import com.ibaiqzzaaziza.rutex.model.User

enum class GoogleSignInResult { SUCCESS, CANCELLED, NO_CLIENT_ID, FAILED }

/**
 * Login dengan akun Google menggunakan Credential Manager (Modul 14).
 * Nama, email, dan foto akun Google disimpan ke DataStore agar pengguna cukup login 1x.
 */
suspend fun signInGoogle(context: Context, dataStore: UserDataStore): GoogleSignInResult {
    // Web client ID belum diisi di local.properties
    if (BuildConfig.API_KEY.isBlank()) return GoogleSignInResult.NO_CLIENT_ID

    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.API_KEY)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    return try {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)
        handleSignIn(result, dataStore)
    } catch (e: GetCredentialCancellationException) {
        GoogleSignInResult.CANCELLED
    } catch (e: GetCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
        GoogleSignInResult.FAILED
    }
}

private suspend fun handleSignIn(
    result: GetCredentialResponse,
    dataStore: UserDataStore
): GoogleSignInResult {
    val credential = result.credential
    if (credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
    ) {
        return try {
            val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data)
            val nama = googleIdToken.displayName ?: ""
            val email = googleIdToken.id
            val photoUrl = googleIdToken.profilePictureUri?.toString() ?: ""
            dataStore.saveData(User(name = nama, email = email, photoUrl = photoUrl), rememberMe = true)
            GoogleSignInResult.SUCCESS
        } catch (e: GoogleIdTokenParsingException) {
            Log.e("SIGN-IN", "Error: ${e.message}")
            GoogleSignInResult.FAILED
        }
    } else {
        Log.e("SIGN-IN", "Error: unrecognized custom credential type.")
        return GoogleSignInResult.FAILED
    }
}

/** Menghapus credential Google dan data pengguna yang tersimpan saat logout. */
suspend fun signOut(context: Context, dataStore: UserDataStore) {
    try {
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    } catch (e: ClearCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
    dataStore.clear()
}
