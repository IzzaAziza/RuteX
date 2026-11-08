package com.ibaiqzzaaziza.rutex.screen

import androidx.lifecycle.ViewModel
import com.ibaiqzzaaziza.rutex.model.Destinasi
import com.ibaiqzzaaziza.rutex.model.KategoriDestinasi

class HomeViewModel : ViewModel() {

    // Data dummy sementara, nantinya diambil dari database/API
    val destinasiPopuler = listOf(
        Destinasi(
            id = 1,
            nama = "Keraton Kasepuhan",
            lokasi = "Jalan Kasepuhan, Lemahwungkuk",
            label = "Wisata Budaya",
            labelHarga = "Tiket Masuk",
            harga = "Rp25.000",
            rating = 4.8,
            kategori = KategoriDestinasi.BUDAYA
        ),
        Destinasi(
            id = 2,
            nama = "Nasi Jamblang Ibu Nur",
            lokasi = "Jl. Cangkring 2, Kejaksan",
            label = "Kuliner Legendaris",
            labelHarga = "Mulai Dari",
            harga = "Rp30.000",
            rating = 4.9,
            kategori = KategoriDestinasi.KULINER
        ),
        Destinasi(
            id = 3,
            nama = "Taman Sari Gua Sunyaragi",
            lokasi = "Sunyaragi, Kesambi",
            label = "Cagar Budaya",
            labelHarga = "Tiket Masuk",
            harga = "Rp15.000",
            rating = 4.7,
            kategori = KategoriDestinasi.SEJARAH
        ),
        Destinasi(
            id = 4,
            nama = "Batik Trusmi Village",
            lokasi = "Plered, Cirebon",
            label = "Sentra Kerajinan",
            labelHarga = "Akses Masuk",
            harga = "Gratis",
            rating = 4.8,
            kategori = KategoriDestinasi.BUDAYA
        )
    )
}
