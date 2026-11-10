package com.ibaiqzzaaziza.rutex.database

import androidx.room.TypeConverter
import com.ibaiqzzaaziza.rutex.model.KunjunganDestinasi

/**
 * Room hanya bisa menyimpan tipe data sederhana (angka, teks, enum).
 * Converter ini mengubah daftar destinasi menjadi teks per baris,
 * dengan format "nama|durasiMenit", dan sebaliknya.
 */
class RuteConverters {

    @TypeConverter
    fun fromDestinasi(list: List<KunjunganDestinasi>): String {
        return list.joinToString(separator = "\n") { "${it.nama}|${it.durasiMenit}" }
    }

    @TypeConverter
    fun toDestinasi(value: String): List<KunjunganDestinasi> {
        if (value.isBlank()) return emptyList()
        return value.lines().mapNotNull { baris ->
            val nama = baris.substringBeforeLast('|')
            val durasi = baris.substringAfterLast('|').toIntOrNull() ?: return@mapNotNull null
            KunjunganDestinasi(nama, durasi)
        }
    }
}
