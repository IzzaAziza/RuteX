package com.ibaiqzzaaziza.rutex.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibaiqzzaaziza.rutex.database.RencanaRuteDao
import com.ibaiqzzaaziza.rutex.model.Destinasi
import com.ibaiqzzaaziza.rutex.model.KategoriDestinasi
import com.ibaiqzzaaziza.rutex.model.Kendaraan
import com.ibaiqzzaaziza.rutex.model.KunjunganDestinasi
import com.ibaiqzzaaziza.rutex.model.ModeRute
import com.ibaiqzzaaziza.rutex.model.RencanaRute
import com.ibaiqzzaaziza.rutex.model.khususCirebon
import com.ibaiqzzaaziza.rutex.network.ApiStatus
import com.ibaiqzzaaziza.rutex.network.PrediksiException
import com.ibaiqzzaaziza.rutex.network.RuteXApi
import com.ibaiqzzaaziza.rutex.network.UserDataStore
import com.ibaiqzzaaziza.rutex.network.buatRutePrediksi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class RuteManualViewModel(
    private val dao: RencanaRuteDao,
    private val dataStore: UserDataStore
) : ViewModel() {

    val kategori = listOf(
        KategoriDestinasi.ALAM,
        KategoriDestinasi.BUDAYA,
        KategoriDestinasi.KULINER,
        KategoriDestinasi.BELANJA
    )

    // Durasi kunjungan dalam menit
    val durasi = listOf(30, 60, 90, 120, 180, 240)

    // Daftar destinasi Cirebon dari server, dipakai untuk titik awal dan tujuan
    var lokasi = mutableStateOf(emptyList<Destinasi>())
        private set

    var statusLokasi = MutableStateFlow(ApiStatus.LOADING)
        private set

    // Status proses "Buat Plan": null = belum ada proses
    var statusSimpan = MutableStateFlow<ApiStatus?>(null)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    var butuhLoginEmail = mutableStateOf(false)
        private set

    // ID rencana yang baru disimpan, dipakai untuk membuka Detail Rute
    var idTersimpan = MutableStateFlow<Long?>(null)
        private set

    init {
        muatLokasi()
    }

    fun muatLokasi() {
        viewModelScope.launch(Dispatchers.IO) {
            statusLokasi.value = ApiStatus.LOADING
            try {
                lokasi.value = RuteXApi.service.getLocations().data.orEmpty()
                    .khususCirebon()
                    .sortedBy { it.name }
                statusLokasi.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("RuteManualViewModel", "Failure: ${e.message}")
                statusLokasi.value = ApiStatus.FAILED
            }
        }
    }

    fun getTitikAwal(): List<String> = lokasi.value.map { it.name }

    fun getDestinasi(kategori: KategoriDestinasi): List<String> =
        lokasi.value.filter { it.kategori == kategori }.map { it.name }

    fun cariKategori(namaDestinasi: String): KategoriDestinasi? =
        lokasi.value.find { it.name == namaDestinasi }?.kategori

    fun buatPlan(
        titikAwal: String,
        tanggal: LocalDate,
        jam: LocalTime,
        kunjungan: List<KunjunganDestinasi>,
        kendaraan: Kendaraan
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            statusSimpan.value = ApiStatus.LOADING
            errorMessage.value = null
            butuhLoginEmail.value = false
            try {
                val user = dataStore.userFlow.first()
                val itinerary = buatRutePrediksi(
                    user = user,
                    titikAwal = titikAwal,
                    destinasi = kunjungan.map { it.nama },
                    durasiMenit = kunjungan.map { it.durasiMenit },
                    tanggal = tanggal,
                    jam = jam,
                    modeKendaraan = kendaraan.apiValue
                )
                val rencana = RencanaRute(
                    mode = ModeRute.MANUAL,
                    titikAwal = titikAwal,
                    tanggal = tanggal.toString(),
                    jamMulai = jam.format(FORMAT_JAM),
                    destinasi = kunjungan,
                    kendaraan = kendaraan,
                    jumlahDestinasi = kunjungan.size,
                    dibuatPada = LocalDateTime.now().format(FORMAT_WAKTU),
                    emailPengguna = user.email,
                    itinerary = itinerary
                )
                idTersimpan.value = dao.insert(rencana)
                statusSimpan.value = ApiStatus.SUCCESS
            } catch (e: PrediksiException) {
                errorMessage.value = e.pesan
                butuhLoginEmail.value = e.butuhLoginEmail
                statusSimpan.value = ApiStatus.FAILED
            } catch (e: Exception) {
                Log.d("RuteManualViewModel", "Failure: ${e.message}")
                statusSimpan.value = ApiStatus.FAILED
            }
        }
    }

    fun clearStatusSimpan() {
        statusSimpan.value = null
        errorMessage.value = null
        butuhLoginEmail.value = false
        idTersimpan.value = null
    }

    companion object {
        const val MAKS_DESTINASI = 5
        val FORMAT_JAM: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val FORMAT_WAKTU: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }
}
