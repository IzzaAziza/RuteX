package com.ibaiqzzaaziza.rutex.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ibaiqzzaaziza.rutex.R

enum class KategoriDestinasi(@param:StringRes val label: Int) {
    SEMUA(R.string.chip_all),
    BUDAYA(R.string.chip_culture),
    KULINER(R.string.chip_culinary),
    SEJARAH(R.string.chip_history),
    ALAM(R.string.chip_nature)
}

data class Destinasi(
    val id: Long,
    val nama: String,
    val lokasi: String,
    val label: String,
    val labelHarga: String,
    val harga: String,
    val rating: Double,
    val kategori: KategoriDestinasi,
    @param:DrawableRes val gambar: Int? = null
)
