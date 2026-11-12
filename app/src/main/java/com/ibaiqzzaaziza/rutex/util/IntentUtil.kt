package com.ibaiqzzaaziza.rutex.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.ibaiqzzaaziza.rutex.R

/**
 * Navigasi ke aplikasi lain dengan implicit intent (Modul 05).
 * Jika tidak ada aplikasi yang bisa membuka intent, pengguna diberi tahu lewat Toast.
 */
private fun Context.bukaIntent(intent: Intent) {
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, R.string.error_tidak_ada_aplikasi, Toast.LENGTH_SHORT).show()
    }
}

/** Membuka lokasi di aplikasi peta (Google Maps atau aplikasi peta lain). */
fun Context.bukaPeta(lat: Double?, lng: Double?, nama: String) {
    val uri = if (lat != null && lng != null) {
        Uri.parse("geo:$lat,$lng?q=$lat,$lng(${Uri.encode(nama)})")
    } else {
        Uri.parse("geo:0,0?q=${Uri.encode("$nama Cirebon")}")
    }
    bukaIntent(Intent(Intent.ACTION_VIEW, uri))
}

/**
 * Membuka petunjuk arah di Google Maps dari titik pertama ke titik terakhir,
 * dengan titik di tengah sebagai persinggahan (waypoints).
 */
fun Context.bukaNavigasi(titik: List<Pair<Double, Double>>, modeKendaraan: String) {
    if (titik.size < 2) return
    val format = { p: Pair<Double, Double> -> "${p.first},${p.second}" }
    val waypoints = titik.subList(1, titik.lastIndex).joinToString("|") { format(it) }
    val uri = Uri.Builder()
        .scheme("https")
        .authority("www.google.com")
        .path("maps/dir/")
        .appendQueryParameter("api", "1")
        .appendQueryParameter("origin", format(titik.first()))
        .appendQueryParameter("destination", format(titik.last()))
        .apply { if (waypoints.isNotEmpty()) appendQueryParameter("waypoints", waypoints) }
        .appendQueryParameter("travelmode", modeKendaraan)
        .build()
    bukaIntent(Intent(Intent.ACTION_VIEW, uri))
}

/** Membagikan teks ke aplikasi lain (WhatsApp, Telegram, dll). */
fun Context.bagikanTeks(teks: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, teks)
    }
    bukaIntent(Intent.createChooser(intent, getString(R.string.bagikan_lewat)))
}
