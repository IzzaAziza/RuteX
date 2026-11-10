package com.ibaiqzzaaziza.rutex.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibaiqzzaaziza.rutex.model.User
import com.ibaiqzzaaziza.rutex.network.ApiStatus
import com.ibaiqzzaaziza.rutex.network.RuteXApi
import com.ibaiqzzaaziza.rutex.network.UserDataStore
import com.ibaiqzzaaziza.rutex.util.JwtUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class AuthViewModel(private val dataStore: UserDataStore) : ViewModel() {

    // null berarti belum ada proses login/daftar yang berjalan
    var status = MutableStateFlow<ApiStatus?>(null)
        private set

    // Pesan error dari server. Jika null saat status FAILED, berarti gagal terhubung ke server.
    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun login(email: String, password: String, rememberMe: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            errorMessage.value = null
            try {
                masuk(email.trim(), password, nama = null, phone = null, rememberMe = rememberMe)
                status.value = ApiStatus.SUCCESS
            } catch (e: ApiException) {
                // Server menjawab, tetapi menolak permintaan (misalnya password salah)
                errorMessage.value = e.message
                status.value = ApiStatus.FAILED
            } catch (e: Exception) {
                // Tidak ada internet atau server tidak bisa dihubungi
                Log.d("AuthViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    /**
     * Endpoint register tidak menerima nama, jadi setelah akun dibuat
     * aplikasi langsung login lalu menyimpan nama lewat endpoint update user.
     */
    fun register(
        nama: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            errorMessage.value = null
            try {
                val response = RuteXApi.service.register(email.trim(), phone, password, confirmPassword)
                if (!response.isSuccessful) {
                    throw ApiException(RuteXApi.getErrorMessage(response))
                }
                masuk(email.trim(), password, nama = nama.trim(), phone = phone, rememberMe = true)
                status.value = ApiStatus.SUCCESS
            } catch (e: ApiException) {
                // Server menjawab, tetapi menolak permintaan (misalnya password salah)
                errorMessage.value = e.message
                status.value = ApiStatus.FAILED
            } catch (e: Exception) {
                // Tidak ada internet atau server tidak bisa dihubungi
                Log.d("AuthViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    private suspend fun masuk(
        email: String,
        password: String,
        nama: String?,
        phone: String?,
        rememberMe: Boolean
    ) {
        val response = RuteXApi.service.login(email, password)
        val token = response.body()?.data
        if (!response.isSuccessful || token == null) {
            throw ApiException(RuteXApi.getErrorMessage(response))
        }

        val userId = JwtUtil.getUserId(token.access) ?: 0
        val bearer = "Bearer ${token.access}"
        var namaUser = nama.orEmpty()

        if (userId != 0) {
            if (nama != null && phone != null) {
                simpanNama(bearer, userId, email, nama, phone)
            } else {
                namaUser = ambilNama(bearer, userId)
            }
        }

        dataStore.saveData(
            User(
                id = userId,
                name = namaUser,
                email = email,
                accessToken = token.access,
                refreshToken = token.refresh
            ),
            rememberMe = rememberMe
        )
    }

    // Gagal mengambil/menyimpan nama tidak menggagalkan login, karena akun tetap bisa dipakai
    private suspend fun ambilNama(bearer: String, userId: Int): String {
        return try {
            RuteXApi.service.getUser(bearer, userId).body()?.data?.name.orEmpty()
        } catch (e: Exception) {
            Log.d("AuthViewModel", "Gagal mengambil profil: ${e.message}")
            ""
        }
    }

    private suspend fun simpanNama(bearer: String, userId: Int, email: String, nama: String, phone: String) {
        try {
            RuteXApi.service.updateUser(
                bearer,
                userId,
                email.toTextPart(),
                nama.toTextPart(),
                phone.toTextPart()
            )
        } catch (e: Exception) {
            Log.d("AuthViewModel", "Gagal menyimpan nama: ${e.message}")
        }
    }

    private fun String.toTextPart() = toRequestBody("text/plain".toMediaTypeOrNull())

    fun clearStatus() {
        status.value = null
        errorMessage.value = null
    }
}

private class ApiException(pesan: String?) : Exception(pesan)
