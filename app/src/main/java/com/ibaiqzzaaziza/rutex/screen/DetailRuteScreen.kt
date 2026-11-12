package com.ibaiqzzaaziza.rutex.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.component.PetaRute
import com.ibaiqzzaaziza.rutex.component.RuteBackTopBar
import com.ibaiqzzaaziza.rutex.model.Destinasi
import com.ibaiqzzaaziza.rutex.model.Kendaraan
import com.ibaiqzzaaziza.rutex.model.ModeRute
import com.ibaiqzzaaziza.rutex.model.RencanaRute
import com.ibaiqzzaaziza.rutex.model.TitikItinerary
import com.ibaiqzzaaziza.rutex.navigation.Screen
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBackground
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBadge
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBlue
import com.ibaiqzzaaziza.rutex.ui.theme.RuteChip
import com.ibaiqzzaaziza.rutex.ui.theme.RuteDivider
import com.ibaiqzzaaziza.rutex.ui.theme.RuteInput
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDark
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDeep
import com.ibaiqzzaaziza.rutex.ui.theme.RutePeach
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate
import com.ibaiqzzaaziza.rutex.util.ViewModelFactory
import com.ibaiqzzaaziza.rutex.util.bagikanTeks
import com.ibaiqzzaaziza.rutex.util.bukaNavigasi
import com.ibaiqzzaaziza.rutex.util.bukaPeta
import com.ibaiqzzaaziza.rutex.util.formatTanggalRencana

private enum class TabDetailRute(val label: Int, val icon: ImageVector) {
    ITINERARY(R.string.tab_itinerary, Icons.Outlined.FormatListBulleted),
    PETA(R.string.tab_peta_rute, Icons.Outlined.Map)
}

@Composable
fun DetailRuteScreen(navController: NavHostController, id: Long) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: DetailRuteViewModel = viewModel(factory = factory)
    val detailLokasi by viewModel.detailLokasi

    var rencana by remember { mutableStateOf<RencanaRute?>(null) }
    var dimuat by remember { mutableStateOf(false) }
    var tab by rememberSaveable { mutableStateOf(TabDetailRute.ITINERARY) }

    // Rencana diambil dari Room berdasarkan ID yang dikirim saat navigasi (Modul 08 & 09)
    LaunchedEffect(id) {
        rencana = viewModel.getRencana(id)
        dimuat = true
    }

    val data = rencana
    val teksBagikan = data?.let { teksRingkasan(it) }

    Scaffold(
        topBar = {
            RuteBackTopBar(
                title = stringResource(R.string.detail_rute_title),
                onBackClick = { navController.popBackStack() },
                actions = {
                    if (teksBagikan != null) {
                        IconButton(onClick = { context.bagikanTeks(teksBagikan) }) {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = stringResource(R.string.bagikan),
                                tint = RuteNavy
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (data != null && data.itinerary.size >= 2) {
                BarNavigasi(rencana = data)
            }
        },
        containerColor = RuteBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when {
                !dimuat -> CircularProgressIndicator(color = RuteOrange, modifier = Modifier.align(Alignment.Center))
                data == null -> Text(
                    text = stringResource(R.string.rencana_tidak_ditemukan),
                    color = RuteSlate,
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> Column(modifier = Modifier.fillMaxSize()) {
                    TabDetail(terpilih = tab, onSelect = { tab = it })
                    when (tab) {
                        TabDetailRute.ITINERARY -> IsiItinerary(
                            rencana = data,
                            detailLokasi = detailLokasi,
                            onDetailDestinasi = { navController.navigate(Screen.DetailDestinasi.withId(it.id)) }
                        )
                        TabDetailRute.PETA -> PetaRute(
                            titik = data.itinerary,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TabDetail(terpilih: TabDetailRute, onSelect: (TabDetailRute) -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .background(RuteBadge, RoundedCornerShape(14.dp))
            .padding(4.dp)
            .selectableGroup()
    ) {
        TabDetailRute.entries.forEach { item ->
            val selected = item == terpilih
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (selected) Color.White else Color.Transparent)
                    .selectable(selected = selected, onClick = { onSelect(item) }, role = Role.Tab)
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = if (selected) RuteOrange else RuteSlate,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(item.label),
                    fontSize = 13.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    color = if (selected) RuteNavy else RuteSlate
                )
            }
        }
    }
}

@Composable
private fun IsiItinerary(
    rencana: RencanaRute,
    detailLokasi: Map<String, Destinasi>,
    onDetailDestinasi: (Destinasi) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        KartuRingkasan(rencana)

        if (rencana.itinerary.isEmpty()) {
            Text(
                text = stringResource(R.string.itinerary_kosong),
                color = RuteSlate,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp)
            )
            return@Column
        }

        rencana.itinerary.forEachIndexed { index, titik ->
            if (index > 0) {
                InfoPerjalanan(detik = titik.waktuTempuhDetik, kendaraan = rencana.kendaraan)
            }
            val detail = detailLokasi[titik.nama]
            if (index == 0) {
                KartuTitikAwal(titik = titik, detail = detail)
            } else {
                KartuDestinasi(
                    nomor = index,
                    titik = titik,
                    detail = detail,
                    onDetailClick = detail?.let { { onDetailDestinasi(it) } },
                    onMapsClick = { context.bukaPeta(titik.lat, titik.lng, titik.nama) }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun KartuRingkasan(rencana: RencanaRute) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, shape)
            .background(Color.White, shape)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier
                .background(RutePeach, CircleShape)
                .padding(horizontal = 10.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.AutoAwesome,
                contentDescription = null,
                tint = RuteOrangeDeep,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.rute_teroptimasi),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = RuteOrangeDeep,
                letterSpacing = 0.5.sp
            )
        }
        Text(
            text = judulRencana(rencana),
            fontSize = 20.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.Bold,
            color = RuteNavy,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = RuteOrange,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = listOfNotNull(
                    stringResource(R.string.jumlah_destinasi, (rencana.itinerary.size - 1).coerceAtLeast(0)),
                    rencana.kendaraan?.let { stringResource(it.label) },
                    formatTanggalRencana(rencana.tanggal)
                ).joinToString(" • "),
                fontSize = 13.sp,
                color = RuteSlate
            )
        }
    }
}

@Composable
private fun KartuTitikAwal(titik: TitikItinerary, detail: Destinasi?) {
    BarisTimeline(penanda = { PenandaTimeline(teks = null, warna = RuteNavy) }) {
        val shape = RoundedCornerShape(16.dp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(1.dp, shape)
                .background(Color.White, shape)
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.titik_keberangkatan),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = RuteSlate,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.weight(1f)
                )
                titik.berangkat?.let {
                    Text(
                        text = stringResource(R.string.jam_wib, it),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = RuteNavy,
                        modifier = Modifier
                            .background(RuteBadge, CircleShape)
                            .padding(horizontal = 10.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = titik.nama, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = RuteNavy)
            detail?.address?.let {
                Text(text = it, fontSize = 12.sp, color = RuteSlate, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun KartuDestinasi(
    nomor: Int,
    titik: TitikItinerary,
    detail: Destinasi?,
    onDetailClick: (() -> Unit)?,
    onMapsClick: () -> Unit
) {
    BarisTimeline(penanda = { PenandaTimeline(teks = "%02d".format(nomor), warna = RuteOrange) }) {
        val shape = RoundedCornerShape(16.dp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(1.dp, shape)
                .clip(shape)
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                if (detail != null) {
                    FotoDestinasi(destinasi = detail, modifier = Modifier.fillMaxSize())
                    Text(
                        text = stringResource(detail.kategori.label),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RuteOrangeDark,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                            .background(RutePeach, CircleShape)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize().background(RuteInput))
                }
                if (titik.tiba != null) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(10.dp)
                            .background(RuteNavy.copy(alpha = 0.75f), CircleShape)
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = listOfNotNull(titik.tiba, titik.berangkat).joinToString(" – "),
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Column {
                    Text(text = titik.nama, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = RuteNavy)
                    detail?.address?.let {
                        Text(text = it, fontSize = 12.sp, color = RuteSlate, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
                if (detail != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(RuteInput, RoundedCornerShape(12.dp))
                            .padding(vertical = 8.dp)
                    ) {
                        InfoKecil(stringResource(R.string.tiket_masuk), hargaTiket(detail.ticketPrice), Modifier.weight(1f))
                        InfoKecil(
                            stringResource(R.string.jam_buka),
                            stringResource(R.string.jam_operasional, detail.jamBuka, detail.jamTutup),
                            Modifier.weight(1f)
                        )
                        InfoKecil(stringResource(R.string.tiba_pukul), titik.tiba ?: "-", Modifier.weight(1f))
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (onDetailClick != null) {
                        Row(
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable(role = Role.Button, onClick = onDetailClick)
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.detail_destinasi_title),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = RuteOrangeDark
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                                contentDescription = null,
                                tint = RuteOrangeDark,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(RuteBadge)
                            .clickable(role = Role.Button, onClick = onMapsClick)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.NearMe,
                            contentDescription = null,
                            tint = RuteBlue,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.buka_maps),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RuteBlue
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoKecil(label: String, nilai: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 11.sp, color = RuteSlate, textAlign = TextAlign.Center)
        Text(
            text = nilai,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = RuteNavy,
            textAlign = TextAlign.Center
        )
    }
}

/** Baris timeline: penanda bulat di kiri dengan garis vertikal, konten di kanan. */
@Composable
private fun BarisTimeline(
    penanda: @Composable () -> Unit,
    konten: @Composable () -> Unit
) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(
            modifier = Modifier
                .width(32.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            penanda()
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .weight(1f)
                    .background(RuteDivider)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier.weight(1f)) { konten() }
    }
}

@Composable
private fun PenandaTimeline(teks: String?, warna: Color) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .background(warna, CircleShape)
            .border(2.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (teks != null) {
            Text(text = teks, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
        } else {
            Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
        }
    }
}

@Composable
private fun InfoPerjalanan(detik: Int?, kendaraan: Kendaraan?) {
    if (detik == null) return
    Row(
        modifier = Modifier.padding(start = 40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .background(RuteChip, CircleShape)
                .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (kendaraan) {
                    Kendaraan.MOTOR -> Icons.Outlined.TwoWheeler
                    Kendaraan.JALAN_KAKI -> Icons.AutoMirrored.Outlined.DirectionsWalk
                    else -> Icons.Outlined.DirectionsCar
                },
                contentDescription = null,
                tint = RuteBlue,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.waktu_tempuh, formatDetik(detik)),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = RuteNavy
            )
        }
    }
}

@Composable
private fun BarNavigasi(rencana: RencanaRute) {
    val context = LocalContext.current
    val selesai = rencana.itinerary.lastOrNull()?.berangkat
    Surface(color = Color.White, shadowElevation = 12.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).background(RuteOrange, CircleShape))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = listOfNotNull(
                        stringResource(R.string.jumlah_destinasi, rencana.itinerary.size - 1),
                        selesai?.let { stringResource(R.string.perkiraan_selesai, it) }
                    ).joinToString(" • "),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RuteNavy
                )
            }
            Button(
                onClick = {
                    val titik = rencana.itinerary.mapNotNull { t ->
                        if (t.lat != null && t.lng != null) t.lat to t.lng else null
                    }
                    context.bukaNavigasi(titik, rencana.kendaraan?.modeGoogleMaps ?: "driving")
                },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = RuteOrange),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(imageVector = Icons.Outlined.Navigation, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.mulai_navigasi), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun judulRencana(rencana: RencanaRute): String {
    val destinasi = rencana.itinerary.drop(1).map { it.nama }
    return when {
        rencana.mode != ModeRute.MANUAL && !rencana.preferensi.isNullOrBlank() -> rencana.preferensi
        destinasi.isNotEmpty() -> destinasi.joinToString(" & ")
        else -> rencana.destinasi.joinToString(" & ") { it.nama }
    }
}

@Composable
private fun teksRingkasan(rencana: RencanaRute): String {
    val baris = rencana.itinerary.mapIndexed { index, t ->
        val jam = if (index == 0) t.berangkat else t.tiba
        "${'A' + index}. ${t.nama}${jam?.let { " ($it)" } ?: ""}"
    }
    return stringResource(R.string.bagikan_rute, formatTanggalRencana(rencana.tanggal), baris.joinToString("\n"))
}

@Composable
private fun formatDetik(detik: Int): String {
    val menit = (detik + 59) / 60
    return if (menit >= 60) {
        stringResource(R.string.durasi_singkat_jam_menit, menit / 60, menit % 60)
    } else {
        stringResource(R.string.durasi_singkat_menit, menit)
    }
}
