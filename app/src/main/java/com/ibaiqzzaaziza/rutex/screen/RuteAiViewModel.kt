package com.ibaiqzzaaziza.rutex.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibaiqzzaaziza.rutex.database.RencanaRuteDao
import com.ibaiqzzaaziza.rutex.model.DataWisataCirebon
import com.ibaiqzzaaziza.rutex.model.ModeRute
import com.ibaiqzzaaziza.rutex.model.RencanaRute
import com.ibaiqzzaaziza.rutex.screen.RuteManualViewModel.Companion.FORMAT_JAM
import com.ibaiqzzaaziza.rutex.screen.RuteManualViewModel.Companion.FORMAT_WAKTU
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class RuteAiViewModel(private val dao: RencanaRuteDao) : ViewModel() {

    val titikAwal = DataWisataCirebon.titikAwal

    /**
     * Menyimpan permintaan rute AI.
     * [jumlahDestinasi] bernilai 0 pada mode tanpa constraint,
     * karena jumlahnya nanti ditentukan sendiri oleh AI.
     */
    fun simpan(
        mode: ModeRute,
        preferensi: String,
        titikAwal: String,
        jam: LocalTime,
        jumlahDestinasi: Int
    ) {
        val rencana = RencanaRute(
            mode = mode,
            titikAwal = titikAwal,
            tanggal = LocalDate.now().toString(),
            jamMulai = jam.format(FORMAT_JAM),
            preferensi = preferensi.trim(),
            jumlahDestinasi = jumlahDestinasi,
            dibuatPada = LocalDateTime.now().format(FORMAT_WAKTU)
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(rencana)
        }
    }

    companion object {
        const val MIN_TEMPAT = 1
        const val MAKS_TEMPAT = 8
        const val MIN_PREFERENSI = 10
        const val MAKS_PREFERENSI = 300
    }
}
