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
import com.ibaiqzzaaziza.rutex.screen.RuteManualViewModel.Companion.FORMAT_JAM
import com.ibaiqzzaaziza.rutex.screen.RuteManualViewModel.Companion.FORMAT_WAKTU
import com.ibaiqzzaaziza.rutex.util.SettingsDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class RuteAiViewModel(
    private val dao: RencanaRuteDao,
    private val dataStore: UserDataStore,
    private val settings: SettingsDataStore
) : ViewModel() {

    // Destinasi Cirebon dari server, dipakai sebagai pilihan lokasi awal dan kandidat rekomendasi
    var lokasi = mutableStateOf(emptyList<Destinasi>())
        private set

    var statusLokasi = MutableStateFlow(ApiStatus.LOADING)
        private set

    var statusGenerate = MutableStateFlow<ApiStatus?>(null)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    var butuhLoginEmail = mutableStateOf(false)
        private set

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
                Log.d("RuteAiViewModel", "Failure: ${e.message}")
                statusLokasi.value = ApiStatus.FAILED
            }
        }
    }

    /**
     * Menyusun rute dari preferensi pengguna.
     * 1. Destinasi dipilih dengan mencocokkan kata kunci preferensi & minat di Profil.
     * 2. Urutan kunjungan dan jadwalnya disusun server lewat /predict.
     * [jumlahDestinasi] null berarti jumlahnya ditentukan otomatis (mode tanpa constraint).
     */
    fun generate(
        mode: ModeRute,
        preferensi: String,
        titikAwal: String,
        jam: LocalTime,
        jumlahDestinasi: Int?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            statusGenerate.value = ApiStatus.LOADING
            errorMessage.value = null
            butuhLoginEmail.value = false
            try {
                val user = dataStore.userFlow.first()
                val minat = settings.preferensiFlow.first().jenisWisata
                val jumlah = jumlahDestinasi ?: JUMLAH_OTOMATIS
                val terpilih = pilihDestinasi(preferensi, minat, titikAwal, jumlah)
                val kunjungan = terpilih.map {
                    KunjunganDestinasi(nama = it.name, durasiMenit = durasiKunjungan(it.kategori))
                }

                val itinerary = buatRutePrediksi(
                    user = user,
                    titikAwal = titikAwal,
                    destinasi = kunjungan.map { it.nama },
                    durasiMenit = kunjungan.map { it.durasiMenit },
                    tanggal = LocalDate.now(),
                    jam = jam,
                    modeKendaraan = Kendaraan.MOBIL.apiValue
                )
                val rencana = RencanaRute(
                    mode = mode,
                    titikAwal = titikAwal,
                    tanggal = LocalDate.now().toString(),
                    jamMulai = jam.format(FORMAT_JAM),
                    destinasi = kunjungan,
                    kendaraan = Kendaraan.MOBIL,
                    preferensi = preferensi.trim(),
                    jumlahDestinasi = kunjungan.size,
                    dibuatPada = LocalDateTime.now().format(FORMAT_WAKTU),
                    emailPengguna = user.email,
                    itinerary = itinerary
                )
                idTersimpan.value = dao.insert(rencana)
                statusGenerate.value = ApiStatus.SUCCESS
            } catch (e: PrediksiException) {
                errorMessage.value = e.pesan
                butuhLoginEmail.value = e.butuhLoginEmail
                statusGenerate.value = ApiStatus.FAILED
            } catch (e: Exception) {
                Log.d("RuteAiViewModel", "Failure: ${e.message}")
                statusGenerate.value = ApiStatus.FAILED
            }
        }
    }

    /**
     * Memberi skor setiap destinasi:
     * - nama destinasi disebut di preferensi: +5
     * - kategori cocok dengan kata kunci di preferensi: +3
     * - kategori termasuk minat di Profil: +1
     * - destinasi prioritas (unggulan) dari server: +1
     */
    private fun pilihDestinasi(
        preferensi: String,
        minat: Set<KategoriDestinasi>,
        titikAwal: String,
        jumlah: Int
    ): List<Destinasi> {
        val teks = preferensi.lowercase()
        val kategoriDisebut = KATA_KUNCI.filterValues { kata -> kata.any { teks.contains(it) } }.keys

        return lokasi.value
            .filter { it.name != titikAwal }
            .map { destinasi ->
                var skor = 0
                if (destinasi.name.lowercase().split(" ").any { it.length > 3 && teks.contains(it) }) skor += 5
                if (destinasi.kategori in kategoriDisebut) skor += 3
                if (destinasi.kategori in minat) skor += 1
                if (destinasi.isPriority) skor += 1
                destinasi to skor
            }
            .sortedWith(compareByDescending<Pair<Destinasi, Int>> { it.second }.thenBy { it.first.name })
            .take(jumlah)
            .map { it.first }
    }

    private fun durasiKunjungan(kategori: KategoriDestinasi): Int = when (kategori) {
        KategoriDestinasi.KULINER -> 60
        KategoriDestinasi.ALAM -> 120
        else -> 90
    }

    fun clearStatus() {
        statusGenerate.value = null
        errorMessage.value = null
        butuhLoginEmail.value = false
        idTersimpan.value = null
    }

    companion object {
        const val MIN_TEMPAT = 1
        const val MAKS_TEMPAT = 8
        const val MIN_PREFERENSI = 10
        const val MAKS_PREFERENSI = 300
        private const val JUMLAH_OTOMATIS = 4

        private val KATA_KUNCI = mapOf(
            KategoriDestinasi.BUDAYA to listOf("budaya", "keraton", "kraton", "sejarah", "masjid", "makam", "vihara", "batik", "tradisi", "religi"),
            KategoriDestinasi.KULINER to listOf("kuliner", "makan", "empal", "jamblang", "nasi", "docang", "lengko", "tahu", "sate", "jajan"),
            KategoriDestinasi.ALAM to listOf("alam", "pantai", "bukit", "curug", "danau", "hutan", "telaga", "air", "sejuk", "pemandangan", "keluarga"),
            KategoriDestinasi.BELANJA to listOf("belanja", "mall", "oleh", "shopping")
        )
    }
}
