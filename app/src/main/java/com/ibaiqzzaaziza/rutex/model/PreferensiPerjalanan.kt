package com.ibaiqzzaaziza.rutex.model

import androidx.annotation.StringRes
import com.ibaiqzzaaziza.rutex.R

enum class DurasiPerjalanan(@param:StringRes val label: Int) {
    BEBERAPA_JAM(R.string.durasi_beberapa_jam),
    SATU_HARI(R.string.durasi_satu_hari),
    DUA_HARI(R.string.durasi_dua_hari),
    TIGA_HARI_LEBIH(R.string.durasi_tiga_hari)
}

enum class GayaPerjalanan(
    @param:StringRes val label: Int,
    @param:StringRes val deskripsi: Int
) {
    SANTAI(R.string.gaya_santai, R.string.gaya_santai_desc),
    FLEKSIBEL(R.string.gaya_fleksibel, R.string.gaya_fleksibel_desc),
    PADAT(R.string.gaya_padat, R.string.gaya_padat_desc)
}

/** Preferensi pengguna yang dipakai RuteX untuk menyesuaikan rekomendasi rute. */
data class PreferensiPerjalanan(
    val jenisWisata: Set<KategoriDestinasi> = setOf(KategoriDestinasi.BUDAYA, KategoriDestinasi.KULINER),
    val durasi: DurasiPerjalanan = DurasiPerjalanan.SATU_HARI,
    val gaya: GayaPerjalanan = GayaPerjalanan.FLEKSIBEL
)
