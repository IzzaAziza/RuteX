package com.ibaiqzzaaziza.rutex.model

// Data pilihan sementara yang dipakai beberapa form, nantinya diambil dari database/API
object DataWisataCirebon {

    val titikAwal = listOf(
        "Stasiun Cirebon (Kejaksan)",
        "Stasiun Cirebon Prujakan",
        "Terminal Harjamukti",
        "Pelabuhan Cirebon",
        "Alun-Alun Kejaksan"
    )

    val destinasiPerKategori = mapOf(
        KategoriDestinasi.ALAM to listOf(
            "Batu Lawang Cupang",
            "Telaga Remis",
            "Pantai Kejawanan",
            "Taman Ade Irma Suryani",
            "Situ Sedong"
        ),
        KategoriDestinasi.BUDAYA to listOf(
            "Keraton Kasepuhan",
            "Keraton Kanoman",
            "Taman Sari Gua Sunyaragi",
            "Masjid Agung Sang Cipta Rasa",
            "Batik Trusmi Village"
        ),
        KategoriDestinasi.KULINER to listOf(
            "Nasi Jamblang Ibu Nur",
            "Empal Gentong H. Apud",
            "Nasi Lengko H. Barno",
            "Tahu Gejrot Pagongan",
            "Docang Pasar Kanoman"
        )
    )
}
