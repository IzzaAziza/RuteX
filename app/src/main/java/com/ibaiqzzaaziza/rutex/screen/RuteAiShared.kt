package com.ibaiqzzaaziza.rutex.screen

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.navigation.Screen
import com.ibaiqzzaaziza.rutex.network.ApiStatus

/**
 * Dipakai kedua form AI: menampilkan pesan hasil generate rute,
 * lalu membuka Detail Rute jika rencana berhasil disusun dan disimpan.
 */
@Composable
fun PantauHasilGenerate(viewModel: RuteAiViewModel, navController: NavHostController) {
    val context = LocalContext.current
    val status by viewModel.statusGenerate.collectAsState()
    val idTersimpan by viewModel.idTersimpan.collectAsState()

    val pesanGagalKoneksi = stringResource(R.string.error_koneksi)
    val pesanButuhLogin = stringResource(R.string.error_butuh_login_email)

    LaunchedEffect(status) {
        when (status) {
            ApiStatus.SUCCESS -> {
                Toast.makeText(context, R.string.rencana_ai_tersimpan, Toast.LENGTH_SHORT).show()
                val id = idTersimpan
                viewModel.clearStatus()
                if (id != null) {
                    navController.navigate(Screen.DetailRute.withId(id)) {
                        popUpTo(Screen.Home.route)
                    }
                }
            }
            ApiStatus.FAILED -> {
                val pesan = when {
                    viewModel.butuhLoginEmail.value -> pesanButuhLogin
                    else -> viewModel.errorMessage.value ?: pesanGagalKoneksi
                }
                Toast.makeText(context, pesan, Toast.LENGTH_LONG).show()
                viewModel.clearStatus()
            }
            else -> {}
        }
    }
}
