package com.ibaiqzzaaziza.rutex.screen

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ibaiqzzaaziza.rutex.BuildConfig
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.component.RuteBackTopBar
import com.ibaiqzzaaziza.rutex.navigation.Screen
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBackground
import com.ibaiqzzaaziza.rutex.ui.theme.RuteChip
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDark
import com.ibaiqzzaaziza.rutex.ui.theme.RutePeach
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate
import com.ibaiqzzaaziza.rutex.util.SettingsDataStore
import com.ibaiqzzaaziza.rutex.util.bukaTautan

/** Satu bagian dokumen: judul dan isi paragraf. */
private data class BagianDokumen(@param:StringRes val judul: Int, @param:StringRes val isi: Int)

/** Kerangka bersama layar informasi: top bar dengan tombol kembali dan isi yang bisa digulir. */
@Composable
private fun KerangkaInfo(
    navController: NavHostController,
    title: String,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = { RuteBackTopBar(title = title, onBackClick = { navController.popBackStack() }) },
        containerColor = RuteBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            content()
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun KartuPutih(content: @Composable () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, shape)
            .clip(shape)
            .background(Color.White)
    ) {
        content()
    }
}

// ===================== Bantuan =====================

private val DAFTAR_FAQ = listOf(
    BagianDokumen(R.string.faq_1_tanya, R.string.faq_1_jawab),
    BagianDokumen(R.string.faq_2_tanya, R.string.faq_2_jawab),
    BagianDokumen(R.string.faq_3_tanya, R.string.faq_3_jawab),
    BagianDokumen(R.string.faq_4_tanya, R.string.faq_4_jawab),
    BagianDokumen(R.string.faq_5_tanya, R.string.faq_5_jawab),
    BagianDokumen(R.string.faq_6_tanya, R.string.faq_6_jawab)
)

@Composable
fun BantuanScreen(navController: NavHostController) {
    val context = LocalContext.current
    var terbuka by rememberSaveable { mutableStateOf<Int?>(0) }

    KerangkaInfo(navController = navController, title = stringResource(R.string.bantuan_title)) {
        Text(
            text = stringResource(R.string.bantuan_pengantar),
            fontSize = 14.sp,
            lineHeight = 21.sp,
            color = RuteSlate
        )
        KartuPutih {
            DAFTAR_FAQ.forEachIndexed { index, faq ->
                if (index > 0) HorizontalDivider(color = RuteChip, modifier = Modifier.padding(horizontal = 16.dp))
                ItemFaq(
                    faq = faq,
                    terbuka = terbuka == index,
                    onClick = { terbuka = if (terbuka == index) null else index }
                )
            }
        }
        KartuPutih {
            BarisTautan(
                icon = Icons.Outlined.Language,
                judul = stringResource(R.string.kunjungi_web),
                deskripsi = stringResource(R.string.alamat_web),
                onClick = { context.bukaTautan("https://rutex.id") }
            )
        }
    }
}

@Composable
private fun ItemFaq(faq: BagianDokumen, terbuka: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .animateContentSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(faq.judul),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = RuteNavy,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (terbuka) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                contentDescription = null,
                tint = RuteSlate
            )
        }
        if (terbuka) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(faq.isi),
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = RuteSlate
            )
        }
    }
}

@Composable
private fun BarisTautan(icon: ImageVector, judul: String, deskripsi: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(RuteChip, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = RuteNavy, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = judul, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = RuteNavy)
            Text(text = deskripsi, fontSize = 12.sp, color = RuteSlate)
        }
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = RuteSlate
        )
    }
}

// ===================== Tentang RuteX =====================

@Composable
fun TentangScreen(navController: NavHostController) {
    KerangkaInfo(navController = navController, title = stringResource(R.string.tentang_title)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logo_rutex),
                contentDescription = stringResource(R.string.logo_desc),
                modifier = Modifier.size(88.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = RuteNavy
            )
            Text(
                text = stringResource(R.string.tentang_versi, BuildConfig.VERSION_NAME),
                fontSize = 13.sp,
                color = RuteOrangeDark,
                fontWeight = FontWeight.SemiBold
            )
        }

        Text(
            text = stringResource(R.string.tentang_deskripsi),
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = RuteSlate,
            textAlign = TextAlign.Center
        )

        KartuPutih {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = stringResource(R.string.tentang_fitur_title),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = RuteNavy
                )
                listOf(
                    R.string.tentang_fitur_1,
                    R.string.tentang_fitur_2,
                    R.string.tentang_fitur_3,
                    R.string.tentang_fitur_4
                ).forEach { fitur ->
                    Row(verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier
                                .padding(top = 7.dp)
                                .size(6.dp)
                                .background(RuteOrange, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = stringResource(fitur), fontSize = 14.sp, lineHeight = 20.sp, color = RuteSlate)
                    }
                }
            }
        }

        KartuPutih {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = stringResource(R.string.tentang_sumber_title),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = RuteNavy
                )
                Text(
                    text = stringResource(R.string.tentang_sumber_isi),
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = RuteSlate
                )
            }
        }

        KartuPutih {
            BarisTautan(
                icon = Icons.Outlined.Language,
                judul = stringResource(R.string.syarat_title),
                deskripsi = stringResource(R.string.syarat_desc_singkat),
                onClick = { navController.navigate(Screen.SyaratKetentuan.route) }
            )
            HorizontalDivider(color = RuteChip, modifier = Modifier.padding(horizontal = 16.dp))
            BarisTautan(
                icon = Icons.Outlined.Language,
                judul = stringResource(R.string.privasi_title),
                deskripsi = stringResource(R.string.privasi_desc_singkat),
                onClick = { navController.navigate(Screen.KebijakanPrivasi.route) }
            )
        }

        Text(
            text = stringResource(R.string.tentang_pengembang),
            fontSize = 12.sp,
            color = RuteSlate,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ===================== Syarat & Ketentuan dan Kebijakan Privasi =====================

private val ISI_SYARAT = listOf(
    BagianDokumen(R.string.syarat_1_judul, R.string.syarat_1_isi),
    BagianDokumen(R.string.syarat_2_judul, R.string.syarat_2_isi),
    BagianDokumen(R.string.syarat_3_judul, R.string.syarat_3_isi),
    BagianDokumen(R.string.syarat_4_judul, R.string.syarat_4_isi)
)

private val ISI_PRIVASI = listOf(
    BagianDokumen(R.string.privasi_1_judul, R.string.privasi_1_isi),
    BagianDokumen(R.string.privasi_2_judul, R.string.privasi_2_isi),
    BagianDokumen(R.string.privasi_3_judul, R.string.privasi_3_isi),
    BagianDokumen(R.string.privasi_4_judul, R.string.privasi_4_isi)
)

@Composable
fun SyaratKetentuanScreen(navController: NavHostController) {
    DokumenScreen(navController, stringResource(R.string.syarat_title), ISI_SYARAT)
}

@Composable
fun KebijakanPrivasiScreen(navController: NavHostController) {
    DokumenScreen(navController, stringResource(R.string.privasi_title), ISI_PRIVASI)
}

@Composable
private fun DokumenScreen(navController: NavHostController, title: String, bagian: List<BagianDokumen>) {
    KerangkaInfo(navController = navController, title = title) {
        bagian.forEachIndexed { index, item ->
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "${index + 1}. ${stringResource(item.judul)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = RuteNavy
                )
                Text(
                    text = stringResource(item.isi),
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = RuteSlate
                )
            }
        }
    }
}

// ===================== Notifikasi =====================

@Composable
fun NotifikasiScreen(navController: NavHostController) {
    val context = LocalContext.current
    val settings = remember { SettingsDataStore(context) }
    val aktif by settings.notifikasiFlow.collectAsState(initial = true)

    KerangkaInfo(navController = navController, title = stringResource(R.string.notifikasi_title)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(RutePeach, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (aktif) Icons.Outlined.NotificationsNone else Icons.Outlined.NotificationsOff,
                    contentDescription = null,
                    tint = RuteOrangeDark,
                    modifier = Modifier.size(38.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(if (aktif) R.string.notifikasi_kosong_title else R.string.notifikasi_mati_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = RuteNavy
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(if (aktif) R.string.notifikasi_kosong_desc else R.string.notifikasi_mati_desc),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = RuteSlate,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(onClick = { navController.navigate(Screen.Profil.route) }) {
                Text(text = stringResource(R.string.atur_notifikasi), color = RuteOrangeDark)
            }
        }
    }
}
