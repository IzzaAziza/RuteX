package com.ibaiqzzaaziza.rutex.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibaiqzzaaziza.rutex.database.RencanaRuteDao
import com.ibaiqzzaaziza.rutex.model.Destinasi
import com.ibaiqzzaaziza.rutex.model.RencanaRute
import com.ibaiqzzaaziza.rutex.model.khususCirebon
import com.ibaiqzzaaziza.rutex.network.ApiStatus
import com.ibaiqzzaaziza.rutex.network.RuteXApi
import com.ibaiqzzaaziza.rutex.network.UserDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    dao: RencanaRuteDao,
    dataStore: UserDataStore
) : ViewModel() {

    // Destinasi prioritas dari server, ditampilkan di bagian Destinasi Populer
    var destinasiPopuler = mutableStateOf(emptyList<Destinasi>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    // Rencana terbaru milik akun yang sedang login, untuk bagian Rute Terakhir
    @OptIn(ExperimentalCoroutinesApi::class)
    val ruteTerakhir: StateFlow<RencanaRute?> = dataStore.userFlow
        .map { it.email }
        .distinctUntilChanged()
        .flatMapLatest { email -> dao.getRencana(email) }
        .map { it.firstOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    init {
        retrieveData()
    }

    fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                destinasiPopuler.value = RuteXApi.service.getPriorityLocations().data
                    .orEmpty()
                    .khususCirebon()
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("HomeViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }
}
