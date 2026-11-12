package com.ibaiqzzaaziza.rutex.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibaiqzzaaziza.rutex.database.RencanaRuteDao
import com.ibaiqzzaaziza.rutex.model.DurasiPerjalanan
import com.ibaiqzzaaziza.rutex.model.GayaPerjalanan
import com.ibaiqzzaaziza.rutex.model.KategoriDestinasi
import com.ibaiqzzaaziza.rutex.model.PreferensiPerjalanan
import com.ibaiqzzaaziza.rutex.model.User
import com.ibaiqzzaaziza.rutex.network.UserDataStore
import com.ibaiqzzaaziza.rutex.util.SettingsDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfilViewModel(
    dao: RencanaRuteDao,
    dataStore: UserDataStore,
    private val settings: SettingsDataStore
) : ViewModel() {

    val preferensi: StateFlow<PreferensiPerjalanan> = settings.preferensiFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PreferensiPerjalanan()
    )

    val user: StateFlow<User> = dataStore.userFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = User()
    )

    // Jumlah rencana milik akun yang sedang login, ditampilkan di kartu profil
    @OptIn(ExperimentalCoroutinesApi::class)
    val jumlahRute: StateFlow<Int> = dataStore.userFlow
        .map { it.email }
        .distinctUntilChanged()
        .flatMapLatest { email -> dao.countRencana(email) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0
        )

    val notifikasi: StateFlow<Boolean> = settings.notifikasiFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = true
    )

    fun simpanNotifikasi(aktif: Boolean) {
        viewModelScope.launch(Dispatchers.IO) { settings.saveNotifikasi(aktif) }
    }

    fun simpanJenisWisata(jenis: Set<KategoriDestinasi>) {
        viewModelScope.launch(Dispatchers.IO) { settings.saveJenisWisata(jenis) }
    }

    fun simpanDurasi(durasi: DurasiPerjalanan) {
        viewModelScope.launch(Dispatchers.IO) { settings.saveDurasi(durasi) }
    }

    fun simpanGaya(gaya: GayaPerjalanan) {
        viewModelScope.launch(Dispatchers.IO) { settings.saveGaya(gaya) }
    }
}
