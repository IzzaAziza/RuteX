package com.ibaiqzzaaziza.rutex.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibaiqzzaaziza.rutex.model.Destinasi
import com.ibaiqzzaaziza.rutex.network.ApiStatus
import com.ibaiqzzaaziza.rutex.network.RuteXApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DetailDestinasiViewModel : ViewModel() {

    var destinasi = mutableStateOf<Destinasi?>(null)
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    fun retrieveData(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                destinasi.value = RuteXApi.service.getLocation(id).data
                status.value = if (destinasi.value != null) ApiStatus.SUCCESS else ApiStatus.FAILED
            } catch (e: Exception) {
                Log.d("DetailDestinasiVM", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }
}
