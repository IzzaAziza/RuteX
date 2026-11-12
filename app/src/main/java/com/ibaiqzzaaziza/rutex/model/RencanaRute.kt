package com.ibaiqzzaaziza.rutex.model

import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ibaiqzzaaziza.rutex.R
import java.io.Serializable

// Tiga cara menyusun rute yang bisa dipilih di layar Buat Rute
enum class ModeRute {
    MANUAL,
    DENGAN_CONSTRAINT,
    TANPA_CONSTRAINT
}

enum class Kendaraan(@param:StringRes val label: Int, val apiValue: String, val modeGoogleMaps: String) {
    MOBIL(R.string.kendaraan_mobil, "mobil", "driving"),
    MOTOR(R.string.kendaraan_motor, "motor", "two-wheeler"),
    JALAN_KAKI(R.string.kendaraan_jalan_kaki, "jalan", "walking")
}

/**
 * Satu rencana perjalanan yang dibuat pengguna.
 * Kolom yang tidak dipakai oleh sebuah mode dibiarkan null,
 * misalnya [kendaraan] hanya diisi oleh mode manual.
 */
@Entity(tableName = "rencana_rute")
data class RencanaRute(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val mode: ModeRute,
    val titikAwal: String,
    val tanggal: String,
    val jamMulai: String,
    val destinasi: List<KunjunganDestinasi> = emptyList(),
    val kendaraan: Kendaraan? = null,
    val preferensi: String? = null,
    val jumlahDestinasi: Int,
    val dibuatPada: String,
    // Pemilik rencana, agar riwayat tiap akun terpisah di perangkat yang sama
    val emailPengguna: String = "",
    // Urutan kunjungan hasil susunan server (/predict) beserta jam tiba & berangkat
    val itinerary: List<TitikItinerary> = emptyList()
)

// Serializable agar daftar destinasi di form bisa disimpan dengan rememberSaveable
data class KunjunganDestinasi(
    val nama: String = "",
    val durasiMenit: Int = 60
) : Serializable
