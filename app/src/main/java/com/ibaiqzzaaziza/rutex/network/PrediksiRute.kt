package com.ibaiqzzaaziza.rutex.network

import com.ibaiqzzaaziza.rutex.model.TitikItinerary
import com.ibaiqzzaaziza.rutex.model.User
import com.ibaiqzzaaziza.rutex.model.keItinerary
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/** Gagal membuat rute. [pesan] berisi alasan dari server jika ada. */
class PrediksiException(val pesan: String?, val butuhLoginEmail: Boolean = false) : Exception(pesan)

/**
 * Meminta server menyusun urutan kunjungan tercepat (POST /predict), sama seperti web RuteX.
 * Endpoint ini butuh token login, jadi akun Google (tanpa token server) tidak bisa memakainya.
 */
suspend fun buatRutePrediksi(
    user: User,
    titikAwal: String,
    destinasi: List<String>,
    durasiMenit: List<Int>,
    tanggal: LocalDate,
    jam: LocalTime,
    modeKendaraan: String
): List<TitikItinerary> {
    if (user.accessToken.isEmpty()) {
        throw PrediksiException(pesan = null, butuhLoginEmail = true)
    }

    val response = RuteXApi.service.predict(
        bearerToken = "Bearer ${user.accessToken}",
        origin = titikAwal.toTextPart(),
        // Server menerima daftar destinasi & durasi (detik) yang dipisah koma
        destination = destinasi.joinToString(",").toTextPart(),
        visitDuration = durasiMenit.joinToString(",") { (it * 60).toString() }.toTextPart(),
        departureDate = tanggal.toString().toTextPart(),
        departureTime = jam.format(DateTimeFormatter.ofPattern("HH:mm")).toTextPart(),
        transportMode = modeKendaraan.toTextPart()
    )

    val body = response.body()
    if (!response.isSuccessful || body == null || body.data.isNullOrEmpty()) {
        throw PrediksiException(RuteXApi.getErrorMessage(response))
    }
    return body.keItinerary(jam)
}

private fun String.toTextPart() = toRequestBody("text/plain".toMediaTypeOrNull())
