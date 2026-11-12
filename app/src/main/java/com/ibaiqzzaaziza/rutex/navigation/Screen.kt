package com.ibaiqzzaaziza.rutex.navigation

import android.net.Uri

const val KEY_ID_DESTINASI = "idDestinasi"
const val KEY_NAMA_DESTINASI = "destinasi"
const val KEY_ID_RENCANA = "idRencana"

sealed class Screen(val route: String) {
    data object Splash : Screen("splashScreen")
    data object Login : Screen("loginScreen")
    data object Register : Screen("registerScreen")
    data object Home : Screen("homeScreen")
    data object BuatRute : Screen("buatRuteScreen")
    // Argumen destinasi bersifat opsional, diisi saat dibuka dari Detail Destinasi
    data object RuteManual : Screen("ruteManualScreen?$KEY_NAMA_DESTINASI={$KEY_NAMA_DESTINASI}") {
        fun withDestinasi(nama: String?) =
            if (nama == null) "ruteManualScreen" else "ruteManualScreen?$KEY_NAMA_DESTINASI=${Uri.encode(nama)}"
    }
    data object RuteAiConstraint : Screen("ruteAiConstraintScreen")
    data object RuteAiTanpaConstraint : Screen("ruteAiTanpaConstraintScreen")
    data object Riwayat : Screen("riwayatScreen")
    data object Profil : Screen("profilScreen")
    data object DetailRute : Screen("detailRuteScreen/{$KEY_ID_RENCANA}") {
        fun withId(id: Long) = "detailRuteScreen/$id"
    }
    data object DetailDestinasi : Screen("detailDestinasiScreen/{$KEY_ID_DESTINASI}") {
        fun withId(id: Int) = "detailDestinasiScreen/$id"
    }
}
