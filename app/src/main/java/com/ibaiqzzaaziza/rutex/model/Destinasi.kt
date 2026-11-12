package com.ibaiqzzaaziza.rutex.model

import androidx.annotation.StringRes
import com.ibaiqzzaaziza.rutex.R
import com.squareup.moshi.Json

/**
 * Kategori destinasi. [apiValue] adalah nilai "category" yang dipakai server RuteX.
 * SEMUA hanya dipakai untuk filter di aplikasi, jadi tidak punya nilai API.
 */
enum class KategoriDestinasi(@param:StringRes val label: Int, val apiValue: String?) {
    SEMUA(R.string.chip_all, null),
    BUDAYA(R.string.chip_culture, "budaya"),
    KULINER(R.string.chip_culinary, "kuliner"),
    ALAM(R.string.chip_nature, "alam"),
    BELANJA(R.string.chip_shopping, "berbelanja");

    companion object {
        fun dariApi(value: String?): KategoriDestinasi =
            entries.find { it.apiValue == value } ?: SEMUA
    }
}

/** Data destinasi dari endpoint /locations milik server RuteX. */
data class Destinasi(
    val id: Int,
    val name: String,
    val address: String = "",
    val description: String = "",
    @param:Json(name = "open_time") val openTime: String? = null,
    @param:Json(name = "close_time") val closeTime: String? = null,
    @param:Json(name = "is_priority") val isPriority: Boolean = false,
    @param:Json(name = "ticket_price") val ticketPrice: Int? = null,
    val facilities: List<String> = emptyList(),
    val category: String? = null,
    val latitude: String? = null,
    val longitude: String? = null,
    val images: List<GambarDestinasi> = emptyList()
) {
    val kategori: KategoriDestinasi get() = KategoriDestinasi.dariApi(category)

    // Jam dari server berformat HH:mm:ss, cukup ditampilkan HH:mm
    val jamBuka: String get() = openTime.orEmpty().take(5)
    val jamTutup: String get() = closeTime.orEmpty().take(5)

    val lat: Double? get() = latitude?.toDoubleOrNull()
    val lng: Double? get() = longitude?.toDoubleOrNull()
}

data class GambarDestinasi(
    val id: Int,
    val image: String
)

/**
 * RuteX adalah aplikasi khusus Cirebon, sedangkan server juga menyimpan destinasi kota lain
 * dan data uji coba. Hanya destinasi Cirebon yang ditampilkan di aplikasi.
 */
fun List<Destinasi>.khususCirebon(): List<Destinasi> = filter {
    val teks = "${it.name} ${it.address}"
    teks.contains("Cirebon", ignoreCase = true) && !it.name.equals("Testing", ignoreCase = true)
}
