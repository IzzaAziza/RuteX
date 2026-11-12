package com.ibaiqzzaaziza.rutex.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibaiqzzaaziza.rutex.database.RencanaRuteDao
import com.ibaiqzzaaziza.rutex.model.DurasiPerjalanan
import com.ibaiqzzaaziza.rutex.model.GayaPerjalanan
import com.ibaiqzzaaziza.rutex.model.KategoriDestinasi
import com.ibaiqzzaaziza.rutex.model.PreferensiPerjalanan
import com.ibaiqzzaaziza.rutex.model.User
import com.ibaiqzzaaziza.rutex.network.ApiStatus
import com.ibaiqzzaaziza.rutex.network.RuteXApi
import com.ibaiqzzaaziza.rutex.network.UserDataStore
import com.ibaiqzzaaziza.rutex.util.SettingsDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class ProfilViewModel(
    dao: RencanaRuteDao,
    private val dataStore: UserDataStore,
    private val settings: SettingsDataStore
) : ViewModel() {

    // Status proses edit profil: null = tidak ada proses
    var editStatus = MutableStateFlow<ApiStatus?>(null)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    /**
     * Mengubah nama pengguna.
     * Akun email/password juga diperbarui di server lewat PUT /user/{id}.
     * Server meminta email dan nomor telepon ikut dikirim, jadi data lama diambil dulu.
     * Akun Google hanya tersimpan di perangkat, jadi cukup disimpan ke DataStore.
     */
    fun editNama(nama: String) {
        viewModelScope.launch(Dispatchers.IO) {
            editStatus.value = ApiStatus.LOADING
            errorMessage.value = null
            try {
                val akun = dataStore.userFlow.first()
                if (akun.accessToken.isNotEmpty() && akun.id != 0) {
                    val bearer = "Bearer ${akun.accessToken}"
                    val profil = RuteXApi.service.getUser(bearer, akun.id).body()?.data
                    val response = RuteXApi.service.updateUser(
                        bearer,
                        akun.id,
                        (profil?.email ?: akun.email).toTextPart(),
                        nama.toTextPart(),
                        profil?.phone.orEmpty().toTextPart()
                    )
                    if (!response.isSuccessful) {
                        errorMessage.value = RuteXApi.getErrorMessage(response)
                        editStatus.value = ApiStatus.FAILED
                        return@launch
                    }
                }
                dataStore.saveNama(nama)
                editStatus.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("ProfilViewModel", "Failure: ${e.message}")
                editStatus.value = ApiStatus.FAILED
            }
        }
    }

    fun clearEditStatus() {
        editStatus.value = null
        errorMessage.value = null
    }

    private fun String.toTextPart() = toRequestBody("text/plain".toMediaTypeOrNull())

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
