package com.ibaiqzzaaziza.rutex.database

import androidx.room.TypeConverter
import com.ibaiqzzaaziza.rutex.model.KunjunganDestinasi
import com.ibaiqzzaaziza.rutex.model.TitikItinerary
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Room hanya bisa menyimpan tipe data sederhana (angka, teks, enum).
 * Converter ini mengubah daftar destinasi menjadi teks per baris,
 * dengan format "nama|durasiMenit", dan sebaliknya.
 */
class RuteConverters {

    private val itineraryAdapter = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
        .adapter<List<TitikItinerary>>(
            Types.newParameterizedType(List::class.java, TitikItinerary::class.java)
        )

    @TypeConverter
    fun fromItinerary(list: List<TitikItinerary>): String = itineraryAdapter.toJson(list)

    @TypeConverter
    fun toItinerary(value: String): List<TitikItinerary> =
        if (value.isBlank()) emptyList() else itineraryAdapter.fromJson(value).orEmpty()

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
