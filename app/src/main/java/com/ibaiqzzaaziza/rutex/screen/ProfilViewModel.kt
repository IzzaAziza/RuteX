package com.ibaiqzzaaziza.rutex.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibaiqzzaaziza.rutex.database.RencanaRuteDao
import com.ibaiqzzaaziza.rutex.model.User
import com.ibaiqzzaaziza.rutex.network.UserDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ProfilViewModel(
    dao: RencanaRuteDao,
    dataStore: UserDataStore
) : ViewModel() {

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
}
