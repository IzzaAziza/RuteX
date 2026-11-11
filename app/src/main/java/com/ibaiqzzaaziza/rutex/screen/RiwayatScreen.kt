package com.ibaiqzzaaziza.rutex.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.EditCalendar
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.component.RuteBottomBar
import com.ibaiqzzaaziza.rutex.component.RuteMainTopBar
import com.ibaiqzzaaziza.rutex.model.ModeRute
import com.ibaiqzzaaziza.rutex.model.RencanaRute
import com.ibaiqzzaaziza.rutex.navigation.Screen
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBackground
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBadge
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBlueLight
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBrown
import com.ibaiqzzaaziza.rutex.ui.theme.RuteChip
import com.ibaiqzzaaziza.rutex.ui.theme.RuteDivider
import com.ibaiqzzaaziza.rutex.ui.theme.RuteError
import com.ibaiqzzaaziza.rutex.ui.theme.RuteInput
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDark
import com.ibaiqzzaaziza.rutex.ui.theme.RutePeach
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate
import com.ibaiqzzaaziza.rutex.ui.theme.RuteXTheme
import com.ibaiqzzaaziza.rutex.util.ViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val FORMAT_TANGGAL_RIWAYAT: DateTimeFormatter =
    DateTimeFormatter.ofPattern("d MMM yyyy", Locale.forLanguageTag("id-ID"))

private enum class FilterRiwayat(@param:StringRes val label: Int) {
    SEMUA(R.string.filter_semua),
    MANUAL(R.string.filter_manual),
    AI(R.string.filter_ai);

    fun cocok(rencana: RencanaRute): Boolean = when (this) {
        SEMUA -> true
        MANUAL -> rencana.mode == ModeRute.MANUAL
        AI -> rencana.mode != ModeRute.MANUAL
    }
}

@Composable
fun RiwayatScreen(navController: NavHostController) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: RiwayatViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()

    var query by rememberSaveable { mutableStateOf("") }
    var filter by rememberSaveable { mutableStateOf(FilterRiwayat.SEMUA) }
    var idAkanDihapus by rememberSaveable { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = { RuteMainTopBar(navController) },
        bottomBar = { RuteBottomBar(navController, Screen.Riwayat.route) },
        containerColor = RuteBackground
    ) { innerPadding ->
        val semua = data
        if (semua == null) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = RuteOrange)
            }
            return@Scaffold
        }

        val tampil = semua.filter { filter.cocok(it) && it.cocokDengan(query) }

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { RiwayatHeader() }
            item { KolomCari(query = query, onQueryChange = { query = it }) }
            item { StatistikRiwayat(semua) }
            item {
                FilterTabs(
                    terpilih = filter,
                    jumlah = { f -> semua.count { f.cocok(it) } },
                    onSelect = { filter = it }
                )
            }

            if (tampil.isEmpty()) {
                item {
                    RiwayatKosong(belumAdaData = semua.isEmpty())
                }
            } else {
                items(tampil, key = { it.id }) { rencana ->
                    RiwayatCard(
                        rencana = rencana,
                        onDeleteClick = { idAkanDihapus = rencana.id }
                    )
                }
            }
        }
    }

    val id = idAkanDihapus
    if (id != null) {
        AlertDialog(
            title = { Text(text = stringResource(R.string.hapus_riwayat_title)) },
            text = { Text(text = stringResource(R.string.hapus_riwayat_pesan)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.delete(id)
                    idAkanDihapus = null
                }) {
                    Text(text = stringResource(R.string.tombol_hapus), color = RuteError)
                }
            },
            dismissButton = {
                TextButton(onClick = { idAkanDihapus = null }) {
                    Text(text = stringResource(R.string.tombol_batal))
                }
            },
            onDismissRequest = { idAkanDihapus = null },
            containerColor = Color.White
        )
    }
}

// Pencarian berdasarkan titik awal, nama destinasi, atau preferensi AI
private fun RencanaRute.cocokDengan(query: String): Boolean {
    val kata = query.trim()
    if (kata.isEmpty()) return true
    return titikAwal.contains(kata, ignoreCase = true) ||
            destinasi.any { it.nama.contains(kata, ignoreCase = true) } ||
            preferensi.orEmpty().contains(kata, ignoreCase = true)
}

@Composable
private fun RiwayatHeader() {
    Column {
        Text(
            text = stringResource(R.string.riwayat_title),
            fontSize = 24.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.SemiBold,
            color = RuteNavy,
            letterSpacing = (-0.6).sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.riwayat_subtitle),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = RuteSlate
        )
    }
}

@Composable
private fun KolomCari(query: String, onQueryChange: (String) -> Unit) {
    val textStyle = TextStyle(fontSize = 14.sp, color = RuteNavy)
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        textStyle = textStyle,
        cursorBrush = SolidColor(RuteOrange),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .shadow(1.dp, CircleShape)
                    .background(Color.White, CircleShape)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = RuteSlate,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            text = stringResource(R.string.riwayat_cari),
                            style = textStyle.copy(color = RuteSlate),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Composable
private fun StatistikRiwayat(data: List<RencanaRute>) {
    val jumlahDestinasi = data.sumOf { if (it.destinasi.isNotEmpty()) it.destinasi.size else it.jumlahDestinasi }
    // Total durasi kunjungan hanya diketahui untuk rute manual
    val totalMenit = data.sumOf { rencana -> rencana.destinasi.sumOf { it.durasiMenit } }

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        KartuStatistik(
            icon = Icons.Outlined.Route,
            iconBackground = RuteBadge,
            nilai = data.size.toString(),
            label = stringResource(R.string.stat_rute_tersimpan),
            modifier = Modifier.weight(1f)
        )
        KartuStatistik(
            icon = Icons.Outlined.LocationOn,
            iconBackground = RuteBlueLight,
            nilai = jumlahDestinasi.toString(),
            label = stringResource(R.string.stat_destinasi),
            modifier = Modifier.weight(1f)
        )
        KartuStatistik(
            icon = Icons.Outlined.AccessTime,
            iconBackground = RutePeach,
            nilai = formatDurasi(totalMenit),
            label = stringResource(R.string.stat_total_durasi),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun KartuStatistik(
    icon: ImageVector,
    iconBackground: Color,
    nilai: String,
    label: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .shadow(1.dp, shape)
            .background(Color.White, shape)
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(iconBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = RuteNavy, modifier = Modifier.size(14.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = nilai,
            fontSize = 20.sp,
            lineHeight = 25.sp,
            fontWeight = FontWeight.SemiBold,
            color = RuteNavy
        )
        Text(
            text = label,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Medium,
            color = RuteSlate,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun FilterTabs(
    terpilih: FilterRiwayat,
    jumlah: (FilterRiwayat) -> Int,
    onSelect: (FilterRiwayat) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(RuteInput, RoundedCornerShape(16.dp))
            .padding(4.dp)
            .selectableGroup()
    ) {
        FilterRiwayat.entries.forEach { filter ->
            val selected = filter == terpilih
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (selected) RuteNavy else Color.Transparent)
                    .selectable(selected = selected, onClick = { onSelect(filter) }, role = Role.Tab)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(filter.label),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (selected) RuteBackground else RuteSlate
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = jumlah(filter).toString(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = RuteNavy,
                    modifier = Modifier
                        .background(RuteDivider, CircleShape)
                        .padding(horizontal = 6.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RiwayatCard(
    rencana: RencanaRute,
    onDeleteClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    val isManual = rencana.mode == ModeRute.MANUAL

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, shape)
            .background(Color.White, shape)
            .padding(16.dp)
    ) {
        // Baris atas: jenis rencana, tanggal, dan tombol hapus
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(
                modifier = Modifier
                    .background(RuteChip, CircleShape)
                    .padding(horizontal = 10.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(RuteOrange, CircleShape)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(rencana.mode.label()),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = RuteBrown
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = formatTanggal(rencana.tanggal),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = RuteSlate,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDeleteClick, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Outlined.DeleteOutline,
                    contentDescription = stringResource(R.string.hapus_riwayat_title),
                    tint = RuteSlate,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(if (isManual) RuteBlueLight else RutePeach, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isManual) Icons.Outlined.EditCalendar else Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = if (isManual) RuteNavy else RuteOrangeDark,
                    modifier = Modifier.size(28.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(if (isManual) R.string.kategori_rute_manual else R.string.kategori_rute_ai),
                    fontSize = 11.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = RuteOrangeDark,
                    letterSpacing = 0.27.sp
                )
                Text(
                    text = if (isManual) {
                        rencana.destinasi.joinToString(" & ") { it.nama }
                    } else {
                        rencana.preferensi.orEmpty()
                    },
                    fontSize = 17.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RuteNavy,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = ringkasan(rencana),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = RuteSlate
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(RuteInput, RoundedCornerShape(12.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Route,
                    contentDescription = null,
                    tint = RuteOrange,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.jalur_rute),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = RuteSlate
                )
            }
            // Titik awal diikuti destinasi sesuai urutan kunjungan
            val titik = listOf(rencana.titikAwal) + rencana.destinasi.map { it.nama }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                itemVerticalAlignment = Alignment.CenterVertically
            ) {
                titik.forEachIndexed { index, nama ->
                    Text(
                        text = nama,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = RuteNavy
                    )
                    if (index < titik.lastIndex) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                            contentDescription = null,
                            tint = RuteSlate,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@StringRes
private fun ModeRute.label(): Int = when (this) {
    ModeRute.MANUAL -> R.string.mode_label_manual
    ModeRute.DENGAN_CONSTRAINT -> R.string.mode_label_constraint
    ModeRute.TANPA_CONSTRAINT -> R.string.mode_label_tanpa_constraint
}

@Composable
private fun ringkasan(rencana: RencanaRute): String {
    val pemisah = " • "
    return when (rencana.mode) {
        ModeRute.MANUAL -> listOfNotNull(
            stringResource(R.string.jumlah_destinasi, rencana.destinasi.size),
            formatDurasi(rencana.destinasi.sumOf { it.durasiMenit }),
            rencana.kendaraan?.let { stringResource(it.label) }
        ).joinToString(pemisah)
        ModeRute.DENGAN_CONSTRAINT -> listOf(
            stringResource(R.string.jumlah_destinasi, rencana.jumlahDestinasi),
            stringResource(R.string.mulai_jam, rencana.jamMulai)
        ).joinToString(pemisah)
        ModeRute.TANPA_CONSTRAINT -> listOf(
            stringResource(R.string.jumlah_otomatis),
            stringResource(R.string.mulai_jam, rencana.jamMulai)
        ).joinToString(pemisah)
    }
}

@Composable
private fun formatDurasi(totalMenit: Int): String {
    val jam = totalMenit / 60
    val menit = totalMenit % 60
    return when {
        jam == 0 -> stringResource(R.string.durasi_singkat_menit, menit)
        menit == 0 -> stringResource(R.string.durasi_singkat_jam, jam)
        else -> stringResource(R.string.durasi_singkat_jam_menit, jam, menit)
    }
}

// Tanggal disimpan dengan format yyyy-MM-dd, ditampilkan menjadi "24 Okt 2024"
private fun formatTanggal(tanggal: String): String {
    return try {
        LocalDate.parse(tanggal).format(FORMAT_TANGGAL_RIWAYAT)
    } catch (e: Exception) {
        tanggal
    }
}

@Composable
private fun RiwayatKosong(belumAdaData: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.History,
            contentDescription = null,
            tint = RuteSlate.copy(alpha = 0.6f),
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(if (belumAdaData) R.string.riwayat_kosong else R.string.riwayat_tidak_ditemukan),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = RuteSlate,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RiwayatScreenPreview() {
    RuteXTheme {
        RiwayatScreen(rememberNavController())
    }
}
