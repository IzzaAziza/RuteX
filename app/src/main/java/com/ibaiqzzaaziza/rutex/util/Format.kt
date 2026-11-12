package com.ibaiqzzaaziza.rutex.util

import java.text.NumberFormat
import java.util.Locale

private val FORMAT_ANGKA = NumberFormat.getIntegerInstance(Locale.forLanguageTag("id-ID"))

/** 25000 -> "Rp25.000" */
fun formatRupiah(nilai: Int): String = "Rp" + FORMAT_ANGKA.format(nilai)
