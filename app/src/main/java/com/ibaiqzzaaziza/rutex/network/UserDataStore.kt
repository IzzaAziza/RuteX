package com.ibaiqzzaaziza.rutex.network

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ibaiqzzaaziza.rutex.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preference"
)

class UserDataStore(private val context: Context) {

    companion object {
        private val USER_ID = intPreferencesKey("id")
        private val USER_NAME = stringPreferencesKey("name")
        private val USER_EMAIL = stringPreferencesKey("email")
        private val USER_PHOTO = stringPreferencesKey("photoUrl")
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val REMEMBER_ME = booleanPreferencesKey("remember_me")
    }

    val userFlow: Flow<User> = context.dataStore.data.map { preferences ->
        User(
            id = preferences[USER_ID] ?: 0,
            name = preferences[USER_NAME] ?: "",
            email = preferences[USER_EMAIL] ?: "",
            photoUrl = preferences[USER_PHOTO] ?: "",
            accessToken = preferences[ACCESS_TOKEN] ?: "",
            refreshToken = preferences[REFRESH_TOKEN] ?: ""
        )
    }

    // Pengguna langsung masuk ke Beranda saat membuka aplikasi
    // jika sudah login dan memilih "Ingat saya"
    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        !preferences[USER_EMAIL].isNullOrEmpty() && preferences[REMEMBER_ME] == true
    }

    suspend fun saveData(user: User, rememberMe: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = user.id
            preferences[USER_NAME] = user.name
            preferences[USER_EMAIL] = user.email
            preferences[USER_PHOTO] = user.photoUrl
            preferences[ACCESS_TOKEN] = user.accessToken
            preferences[REFRESH_TOKEN] = user.refreshToken
            preferences[REMEMBER_ME] = rememberMe
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
