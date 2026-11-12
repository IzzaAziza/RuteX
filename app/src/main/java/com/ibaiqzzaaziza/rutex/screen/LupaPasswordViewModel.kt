package com.ibaiqzzaaziza.rutex.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibaiqzzaaziza.rutex.network.ApiStatus
import com.ibaiqzzaaziza.rutex.network.RuteXApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LupaPasswordViewModel : ViewModel() {

    // null = belum ada permintaan yang dikirim
    var status = MutableStateFlow<ApiStatus?>(null)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    /** Server mengirim tautan reset kata sandi ke email jika email terdaftar. */
    fun kirimTautan(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            errorMessage.value = null
            try {
                val response = RuteXApi.service.resetPassword(email.trim())
                if (response.isSuccessful) {
                    status.value = ApiStatus.SUCCESS
                } else {
                    errorMessage.value = RuteXApi.getErrorMessage(response)
                    status.value = ApiStatus.FAILED
                }
            } catch (e: Exception) {
                Log.d("LupaPasswordViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun ulangi() {
        status.value = null
        errorMessage.value = null
    }
}
