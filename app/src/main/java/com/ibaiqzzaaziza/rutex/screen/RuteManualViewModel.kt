package com.ibaiqzzaaziza.rutex.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibaiqzzaaziza.rutex.database.RencanaRuteDao
import com.ibaiqzzaaziza.rutex.model.DataWisataCirebon
import com.ibaiqzzaaziza.rutex.model.Kendaraan
import com.ibaiqzzaaziza.rutex.model.KategoriDestinasi
import com.ibaiqzzaaziza.rutex.model.KunjunganDestinasi
import com.ibaiqzzaaziza.rutex.model.ModeRute
import com.ibaiqzzaaziza.rutex.model.RencanaRute
import com.ibaiqzzaaziza.rutex.network.UserDataStore
import kotlinx.coroutines.Dispatchers
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

    val titikAwal = DataWisataCirebon.titikAwal

    val kategori = listOf(
        KategoriDestinasi.ALAM,
        KategoriDestinasi.BUDAYA,
        KategoriDestinasi.KULINER
    )

    // Durasi kunjungan dalam menit
    val durasi = listOf(30, 60, 90, 120, 180)

    fun getDestinasi(kategori: KategoriDestinasi): List<String> {
        return DataWisataCirebon.destinasiPerKategori[kategori].orEmpty()
    }

    fun simpan(
        titikAwal: String,
        tanggal: LocalDate,
        jam: LocalTime,
        kunjungan: List<KunjunganDestinasi>,
        kendaraan: Kendaraan
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val rencana = RencanaRute(
                mode = ModeRute.MANUAL,
                titikAwal = titikAwal,
                tanggal = tanggal.toString(),
                jamMulai = jam.format(FORMAT_JAM),
                destinasi = kunjungan,
                kendaraan = kendaraan,
                jumlahDestinasi = kunjungan.size,
                dibuatPada = LocalDateTime.now().format(FORMAT_WAKTU),
                emailPengguna = dataStore.userFlow.first().email
            )
            dao.insert(rencana)
        }
    }

    companion object {
        const val MAKS_DESTINASI = 5
        val FORMAT_JAM: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val FORMAT_WAKTU: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }
}
