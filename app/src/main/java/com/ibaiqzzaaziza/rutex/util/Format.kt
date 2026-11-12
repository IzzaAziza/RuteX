package com.ibaiqzzaaziza.rutex.util

import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val FORMAT_ANGKA = NumberFormat.getIntegerInstance(Locale.forLanguageTag("id-ID"))

/** 25000 -> "Rp25.000" */
fun formatRupiah(nilai: Int): String = "Rp" + FORMAT_ANGKA.format(nilai)

private val FORMAT_TANGGAL: DateTimeFormatter =
    DateTimeFormatter.ofPattern("d MMM yyyy", Locale.forLanguageTag("id-ID"))

/** Tanggal disimpan dengan format yyyy-MM-dd, ditampilkan menjadi "24 Okt 2024" */
fun formatTanggalRencana(tanggal: String): String {
    return try {
        LocalDate.parse(tanggal).format(FORMAT_TANGGAL)
    } catch (e: Exception) {
        tanggal
    }
}
