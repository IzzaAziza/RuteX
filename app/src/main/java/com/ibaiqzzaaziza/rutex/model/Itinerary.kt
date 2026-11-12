package com.ibaiqzzaaziza.rutex.model

import com.squareup.moshi.Json
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/** Satu titik dalam rute yang sudah disusun server: titik awal atau destinasi. */
data class TitikItinerary(
    val nama: String,
    val lat: Double? = null,
    val lng: Double? = null,
    // Jam tiba dan jam meninggalkan lokasi (HH:mm). Titik awal tidak punya jam tiba.
    val tiba: String? = null,
    val berangkat: String? = null,
    // Lama perjalanan dari titik sebelumnya, dalam detik
    val waktuTempuhDetik: Int? = null
)

/**
 * Respons POST /predict. Server mengurutkan destinasi agar rutenya paling efisien:
 * - data: titik awal diikuti destinasi sesuai urutan kunjungan
 * - estimation: lama perjalanan antar titik (detik)
 * - arrival: jam tiba di setiap destinasi
 * - total_time: jam meninggalkan setiap destinasi
 */
data class PrediksiResponse(
    val status: String? = null,
    val message: Any? = null,
    val data: List<TitikPrediksi>? = null,
    val estimation: List<Double>? = null,
    val arrival: List<String>? = null,
    @param:Json(name = "total_time") val totalTime: List<String>? = null
)

data class TitikPrediksi(
    val name: String,
    val lat: Double? = null,
    val lng: Double? = null
)

fun PrediksiResponse.keItinerary(jamBerangkat: LocalTime): List<TitikItinerary> {
    val titik = data.orEmpty()
    val formatJam = DateTimeFormatter.ofPattern("HH:mm")
    return titik.mapIndexed { index, item ->
        if (index == 0) {
            TitikItinerary(
                nama = item.name,
                lat = item.lat,
                lng = item.lng,
                berangkat = jamBerangkat.format(formatJam)
            )
        } else {
            TitikItinerary(
                nama = item.name,
                lat = item.lat,
                lng = item.lng,
                tiba = arrival?.getOrNull(index - 1)?.take(5),
                berangkat = totalTime?.getOrNull(index - 1)?.take(5),
                waktuTempuhDetik = estimation?.getOrNull(index - 1)?.toInt()
            )
        }
    }
}
