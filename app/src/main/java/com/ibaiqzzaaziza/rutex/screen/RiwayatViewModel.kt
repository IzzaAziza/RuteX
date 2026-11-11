package com.ibaiqzzaaziza.rutex.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibaiqzzaaziza.rutex.database.RencanaRuteDao
import com.ibaiqzzaaziza.rutex.model.RencanaRute
import com.ibaiqzzaaziza.rutex.network.UserDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RiwayatViewModel(
    private val dao: RencanaRuteDao,
    dataStore: UserDataStore
) : ViewModel() {

    // Daftar rencana milik akun yang sedang login, otomatis ter-update saat ada data baru
    @OptIn(ExperimentalCoroutinesApi::class)
    val data: StateFlow<List<RencanaRute>?> = dataStore.userFlow
        .map { it.email }
        .distinctUntilChanged()
        .flatMapLatest { email -> dao.getRencana(email) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            // null berarti data masih dimuat, berbeda dengan list kosong
            initialValue = null
        )

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}
