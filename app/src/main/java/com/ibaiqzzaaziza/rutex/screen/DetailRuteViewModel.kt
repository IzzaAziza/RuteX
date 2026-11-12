package com.ibaiqzzaaziza.rutex.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibaiqzzaaziza.rutex.database.RencanaRuteDao
import com.ibaiqzzaaziza.rutex.model.Destinasi
import com.ibaiqzzaaziza.rutex.model.RencanaRute
import com.ibaiqzzaaziza.rutex.network.RuteXApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailRuteViewModel(private val dao: RencanaRuteDao) : ViewModel() {

    // Detail tiap destinasi (foto, jam buka, tiket) dari server, dicocokkan berdasarkan nama
    var detailLokasi = mutableStateOf<Map<String, Destinasi>>(emptyMap())
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                detailLokasi.value = RuteXApi.service.getLocations().data.orEmpty().associateBy { it.name }
            } catch (e: Exception) {
                // Itinerary tetap bisa ditampilkan tanpa foto dan info tambahan
                Log.d("DetailRuteViewModel", "Failure: ${e.message}")
            }
        }
    }

    suspend fun getRencana(id: Long): RencanaRute? = dao.getRencanaById(id)
}
