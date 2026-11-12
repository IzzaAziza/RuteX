package com.ibaiqzzaaziza.rutex.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.TwoWheeler
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.component.DropdownField
import com.ibaiqzzaaziza.rutex.component.FieldHint
import com.ibaiqzzaaziza.rutex.component.FormCard
import com.ibaiqzzaaziza.rutex.component.FormHeader
import com.ibaiqzzaaziza.rutex.component.FormLabel
import com.ibaiqzzaaziza.rutex.component.KonfirmasiKeluarDialog
import com.ibaiqzzaaziza.rutex.component.RuteBottomBar
import com.ibaiqzzaaziza.rutex.component.RuteDatePickerDialog
import com.ibaiqzzaaziza.rutex.component.RuteMainTopBar
import com.ibaiqzzaaziza.rutex.component.RutePrimaryButton
import com.ibaiqzzaaziza.rutex.component.RuteTimePickerDialog
import com.ibaiqzzaaziza.rutex.component.SelectField
import com.ibaiqzzaaziza.rutex.model.KategoriDestinasi
import com.ibaiqzzaaziza.rutex.model.Kendaraan
import com.ibaiqzzaaziza.rutex.model.KunjunganDestinasi
import com.ibaiqzzaaziza.rutex.navigation.Screen
import com.ibaiqzzaaziza.rutex.network.ApiStatus
import com.ibaiqzzaaziza.rutex.screen.RuteManualViewModel.Companion.FORMAT_JAM
import com.ibaiqzzaaziza.rutex.screen.RuteManualViewModel.Companion.MAKS_DESTINASI
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBackground
import com.ibaiqzzaaziza.rutex.ui.theme.RuteChip
import com.ibaiqzzaaziza.rutex.ui.theme.RuteDivider
import com.ibaiqzzaaziza.rutex.ui.theme.RuteError
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDark
import com.ibaiqzzaaziza.rutex.ui.theme.RutePeach
import com.ibaiqzzaaziza.rutex.ui.theme.RutePeachLight
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate
import com.ibaiqzzaaziza.rutex.ui.theme.RuteXTheme
import com.ibaiqzzaaziza.rutex.util.ViewModelFactory
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val FORMAT_TANGGAL: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

@Composable
fun RuteManualScreen(navController: NavHostController, destinasiAwal: String? = null) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: RuteManualViewModel = viewModel(factory = factory)

    val statusLokasi by viewModel.statusLokasi.collectAsState()
    val statusSimpan by viewModel.statusSimpan.collectAsState()
    val idTersimpan by viewModel.idTersimpan.collectAsState()

    var kategori by rememberSaveable { mutableStateOf(viewModel.kategori.first()) }
    var tanggal by rememberSaveable { mutableStateOf<LocalDate?>(null) }
    var jam by rememberSaveable { mutableStateOf<LocalTime?>(null) }
    var titikAwal by rememberSaveable { mutableStateOf("") }
    // Jika dibuka dari Detail Destinasi, destinasi tersebut langsung menjadi tujuan pertama
    var kunjungan by rememberSaveable {
        mutableStateOf(listOf(KunjunganDestinasi(nama = destinasiAwal.orEmpty()), KunjunganDestinasi()))
    }
    var kendaraan by rememberSaveable { mutableStateOf<Kendaraan?>(null) }

    // Error baru ditampilkan setelah pengguna menekan tombol Buat Plan
    var tampilkanError by rememberSaveable { mutableStateOf(false) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    var showKeluarDialog by rememberSaveable { mutableStateOf(false) }

    val error = validasiRuteManual(tanggal, jam, titikAwal, kunjungan, kendaraan)

    // Tab kategori menyesuaikan destinasi yang dikirim dari Detail Destinasi
    LaunchedEffect(statusLokasi) {
        if (statusLokasi == ApiStatus.SUCCESS && !destinasiAwal.isNullOrEmpty()) {
            viewModel.cariKategori(destinasiAwal)?.let { kategori = it }
        }
    }

    val pesanGagalKoneksi = stringResource(R.string.error_koneksi)
    val pesanButuhLogin = stringResource(R.string.error_butuh_login_email)
    val pesanGagalRute = stringResource(R.string.error_buat_rute)
    LaunchedEffect(statusSimpan) {
        when (statusSimpan) {
            ApiStatus.SUCCESS -> {
                Toast.makeText(context, R.string.rencana_tersimpan, Toast.LENGTH_SHORT).show()
                val id = idTersimpan
                viewModel.clearStatusSimpan()
                if (id != null) {
                    // Rencana yang baru disimpan langsung terlihat di Riwayat
                    navController.navigate(Screen.Riwayat.route) {
                        popUpTo(Screen.Home.route)
                    }
                }
            }
            ApiStatus.FAILED -> {
                val pesan = when {
                    viewModel.butuhLoginEmail.value -> pesanButuhLogin
                    viewModel.errorMessage.value != null -> viewModel.errorMessage.value
                    else -> pesanGagalKoneksi
                }
                Toast.makeText(context, pesan ?: pesanGagalRute, Toast.LENGTH_LONG).show()
                viewModel.clearStatusSimpan()
            }
            else -> {}
        }
    }
    val adaIsian = tanggal != null || jam != null || kendaraan != null ||
            kunjungan.any { it.nama.isNotEmpty() }

    val kembali = {
        if (adaIsian) showKeluarDialog = true else navController.popBackStack()
    }
    BackHandler(enabled = adaIsian) { showKeluarDialog = true }

    Scaffold(
        topBar = { RuteMainTopBar(navController) },
        bottomBar = { RuteBottomBar(navController, Screen.BuatRute.route) },
        containerColor = RuteBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            FormHeader(
                badge = stringResource(R.string.badge_mode_manual),
                title = stringResource(R.string.manual_title),
                subtitle = stringResource(R.string.manual_subtitle),
                onBackClick = { kembali() }
            )

            Spacer(modifier = Modifier.height(20.dp))
            SapaanCard()

            Spacer(modifier = Modifier.height(16.dp))
            FormCard {
                StatusDataLokasi(status = statusLokasi, onRetry = { viewModel.muatLokasi() })
                KategoriTabs(
                    pilihan = viewModel.kategori,
                    terpilih = kategori,
                    onSelect = { kategori = it }
                )
                Text(
                    text = stringResource(R.string.manual_hint_kategori),
                    fontSize = 10.sp,
                    color = RuteSlate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))
                WaktuKeberangkatan(
                    tanggal = tanggal,
                    jam = jam,
                    tanggalError = tampilkanError && error.tanggal,
                    jamError = if (tampilkanError) error.jam else null,
                    onTanggalClick = { showDatePicker = true },
                    onJamClick = { showTimePicker = true }
                )

                Spacer(modifier = Modifier.height(16.dp))
                FormLabel(text = stringResource(R.string.label_titik_awal))
                Spacer(modifier = Modifier.height(6.dp))
                DropdownField(
                    value = titikAwal,
                    options = viewModel.getTitikAwal(),
                    onSelect = { titikAwal = it },
                    placeholder = stringResource(R.string.placeholder_titik_awal),
                    leadingIcon = Icons.Outlined.LocationOn,
                    isError = tampilkanError && error.titikAwalKosong
                )
                FieldHint(
                    text = stringResource(
                        if (tampilkanError && error.titikAwalKosong) R.string.error_titik_awal_kosong
                        else R.string.hint_titik_awal
                    ),
                    isError = tampilkanError && (error.titikAwalSamaDestinasi || error.titikAwalKosong)
                )

                HorizontalDivider(
                    color = RuteDivider,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                DaftarKunjungan(
                    kunjungan = kunjungan,
                    pilihanDestinasi = viewModel.getDestinasi(kategori),
                    pilihanDurasi = viewModel.durasi,
                    errorPerBaris = if (tampilkanError) error.destinasi else emptyList(),
                    onChange = { kunjungan = it }
                )

                Spacer(modifier = Modifier.height(16.dp))
                FormLabel(text = stringResource(R.string.label_kendaraan), required = true)
                Spacer(modifier = Modifier.height(8.dp))
                PilihanKendaraan(
                    terpilih = kendaraan,
                    onSelect = { kendaraan = it }
                )
                if (tampilkanError && error.kendaraan) {
                    FieldHint(text = stringResource(R.string.error_kendaraan), isError = true)
                }

                Spacer(modifier = Modifier.height(24.dp))
                RutePrimaryButton(
                    text = stringResource(R.string.btn_buat_plan),
                    isLoading = statusSimpan == ApiStatus.LOADING,
                    onClick = {
                        tampilkanError = true
                        if (error.adaError) {
                            Toast.makeText(context, R.string.error_form_belum_lengkap, Toast.LENGTH_SHORT).show()
                            return@RutePrimaryButton
                        }
                        // Server menyusun urutan kunjungan tercepat, lalu hasilnya disimpan ke Room
                        viewModel.buatPlan(
                            titikAwal = titikAwal,
                            tanggal = tanggal!!,
                            jam = jam!!,
                            kunjungan = kunjungan,
                            kendaraan = kendaraan!!
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = RuteOrange,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.manual_footer),
                    fontSize = 11.sp,
                    color = RuteSlate
                )
            }
        }
    }

    if (showDatePicker) {
        RuteDatePickerDialog(
            initialDate = tanggal,
            onDismissRequest = { showDatePicker = false },
            onConfirmation = {
                tanggal = it
                showDatePicker = false
            }
        )
    }

    if (showTimePicker) {
        RuteTimePickerDialog(
            initialTime = jam,
            onDismissRequest = { showTimePicker = false },
            onConfirmation = {
                jam = it
                showTimePicker = false
            }
        )
    }

    if (showKeluarDialog) {
        KonfirmasiKeluarDialog(
            onDismissRequest = { showKeluarDialog = false },
            onConfirmation = {
                showKeluarDialog = false
                navController.popBackStack()
            }
        )
    }
}

/** Hasil pengecekan form. Setiap properti bernilai true/berisi pesan jika field tersebut salah. */
private data class ErrorRuteManual(
    val tanggal: Boolean,
    val jam: Int?,
    val titikAwalKosong: Boolean,
    val titikAwalSamaDestinasi: Boolean,
    val destinasi: List<Int?>,
    val kendaraan: Boolean
) {
    val adaError: Boolean
        get() = tanggal || jam != null || titikAwalKosong || titikAwalSamaDestinasi ||
                destinasi.any { it != null } || kendaraan
}

private fun validasiRuteManual(
    tanggal: LocalDate?,
    jam: LocalTime?,
    titikAwal: String,
    kunjungan: List<KunjunganDestinasi>,
    kendaraan: Kendaraan?
): ErrorRuteManual {
    val jamError = when {
        jam == null -> R.string.error_jam_kosong
        // Jika berangkat hari ini, jam keberangkatan tidak boleh sudah lewat
        tanggal == LocalDate.now() && jam.isBefore(LocalTime.now()) -> R.string.error_jam_lewat
        else -> null
    }

    val destinasiError = kunjungan.mapIndexed { index, item ->
        when {
            item.nama.isEmpty() -> R.string.error_destinasi_kosong
            kunjungan.take(index).any { it.nama == item.nama } -> R.string.hint_destinasi_sama
            else -> null
        }
    }

    return ErrorRuteManual(
        tanggal = tanggal == null,
        jam = jamError,
        titikAwalKosong = titikAwal.isEmpty(),
        titikAwalSamaDestinasi = titikAwal.isNotEmpty() && kunjungan.any { it.nama == titikAwal },
        destinasi = destinasiError,
        kendaraan = kendaraan == null
    )
}

/** Indikator saat daftar destinasi dari server sedang dimuat atau gagal dimuat (Modul 13). */
@Composable
private fun StatusDataLokasi(status: ApiStatus, onRetry: () -> Unit) {
    when (status) {
        ApiStatus.LOADING -> Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(color = RuteOrange, strokeWidth = 2.dp, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.memuat_destinasi), fontSize = 12.sp, color = RuteSlate)
        }
        ApiStatus.FAILED -> Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .background(RuteError.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.error_muat_destinasi),
                fontSize = 12.sp,
                color = RuteError,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onRetry) {
                Text(text = stringResource(R.string.tombol_coba_lagi), color = RuteOrangeDark)
            }
        }
        ApiStatus.SUCCESS -> {}
    }
}

@Composable
private fun SapaanCard() {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Brush.horizontalGradient(listOf(RutePeachLight, RutePeach.copy(alpha = 0.6f))))
            .border(1.dp, RutePeach, shape)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = RuteOrange,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.sapaan_label),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = RuteOrange,
                    letterSpacing = 0.5.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.sapaan_title),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = RuteNavy
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(R.string.sapaan_desc),
                fontSize = 11.sp,
                color = RuteSlate
            )
        }
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(RutePeach, RoundedCornerShape(12.dp))
                .border(1.dp, RuteOrange.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.AccountBalance,
                contentDescription = null,
                tint = RuteOrangeDark,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun KategoriTabs(
    pilihan: List<KategoriDestinasi>,
    terpilih: KategoriDestinasi,
    onSelect: (KategoriDestinasi) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(RuteChip, RoundedCornerShape(12.dp))
            .padding(4.dp)
            .selectableGroup()
    ) {
        pilihan.forEach { kategori ->
            val selected = kategori == terpilih
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (selected) RuteOrange else Color.Transparent)
                    .selectable(
                        selected = selected,
                        onClick = { onSelect(kategori) },
                        role = Role.Tab
                    )
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = kategori.icon(),
                    contentDescription = null,
                    tint = if (selected) Color.White else RuteOrangeDark,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(kategori.label),
                    fontSize = 13.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    color = if (selected) Color.White else RuteNavy
                )
            }
        }
    }
}

private fun KategoriDestinasi.icon(): ImageVector = when (this) {
    KategoriDestinasi.ALAM -> Icons.Outlined.Park
    KategoriDestinasi.KULINER -> Icons.Outlined.RestaurantMenu
    KategoriDestinasi.BELANJA -> Icons.Outlined.ShoppingBag
    else -> Icons.Outlined.AccountBalance
}

@Composable
private fun WaktuKeberangkatan(
    tanggal: LocalDate?,
    jam: LocalTime?,
    tanggalError: Boolean,
    jamError: Int?,
    onTanggalClick: () -> Unit,
    onJamClick: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            FormLabel(text = stringResource(R.string.label_tanggal_berangkat), required = true)
            Spacer(modifier = Modifier.height(6.dp))
            SelectField(
                value = tanggal?.format(FORMAT_TANGGAL).orEmpty(),
                placeholder = stringResource(R.string.placeholder_tanggal),
                trailingIcon = Icons.Outlined.CalendarToday,
                onClick = onTanggalClick,
                isError = tanggalError
            )
            if (tanggalError) {
                FieldHint(text = stringResource(R.string.error_tanggal_kosong), isError = true)
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            FormLabel(text = stringResource(R.string.label_jam_berangkat), required = true)
            Spacer(modifier = Modifier.height(6.dp))
            SelectField(
                value = jam?.format(FORMAT_JAM).orEmpty(),
                placeholder = stringResource(R.string.placeholder_jam),
                trailingIcon = Icons.Outlined.AccessTime,
                onClick = onJamClick,
                isError = jamError != null
            )
            if (jamError != null) {
                FieldHint(text = stringResource(jamError), isError = true)
            }
        }
    }
}

@Composable
private fun DaftarKunjungan(
    kunjungan: List<KunjunganDestinasi>,
    pilihanDestinasi: List<String>,
    pilihanDurasi: List<Int>,
    errorPerBaris: List<Int?>,
    onChange: (List<KunjunganDestinasi>) -> Unit
) {
    val labelDurasi = pilihanDurasi.map { durasiLabel(it) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        kunjungan.forEachIndexed { index, item ->
            val nomor = index + 1
            val isLast = index == kunjungan.lastIndex
            val bisaTambah = isLast && kunjungan.size < MAKS_DESTINASI
            val errorBaris = errorPerBaris.getOrNull(index)

            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormLabel(
                        text = stringResource(R.string.label_tujuan_destinasi, nomor),
                        required = true,
                        modifier = Modifier.weight(1.6f)
                    )
                    FormLabel(
                        text = stringResource(R.string.label_durasi_kunjungan, nomor),
                        required = true,
                        modifier = Modifier.weight(1f)
                    )
                    if (bisaTambah) {
                        Spacer(modifier = Modifier.width(46.dp))
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DropdownField(
                        value = item.nama,
                        options = pilihanDestinasi,
                        onSelect = { nama ->
                            onChange(kunjungan.mapIndexed { i, k -> if (i == index) k.copy(nama = nama) else k })
                        },
                        placeholder = stringResource(R.string.placeholder_destinasi),
                        isError = errorBaris != null,
                        modifier = Modifier.weight(1.6f)
                    )
                    DropdownField(
                        value = durasiLabel(item.durasiMenit),
                        options = labelDurasi,
                        onSelect = { label ->
                            val menit = pilihanDurasi[labelDurasi.indexOf(label)]
                            onChange(kunjungan.mapIndexed { i, k -> if (i == index) k.copy(durasiMenit = menit) else k })
                        },
                        placeholder = "",
                        modifier = Modifier.weight(1f)
                    )
                    if (bisaTambah) {
                        FilledIconButton(
                            onClick = { onChange(kunjungan + KunjunganDestinasi()) },
                            shape = RoundedCornerShape(10.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = RuteNavy),
                            modifier = Modifier.size(46.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = stringResource(R.string.tambah_destinasi),
                                tint = Color.White
                            )
                        }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.weight(1f)) {
                        FieldHint(
                            text = stringResource(errorBaris ?: R.string.hint_destinasi_sama),
                            isError = errorBaris != null
                        )
                    }
                    // Destinasi tambahan (ke-3 dan seterusnya) boleh dihapus
                    if (index >= 2) {
                        TextButton(
                            onClick = { onChange(kunjungan.filterIndexed { i, _ -> i != index }) },
                            modifier = Modifier.height(32.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.hapus_destinasi),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RuteError
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun durasiLabel(menit: Int): String {
    return if (menit < 60) {
        stringResource(R.string.durasi_menit, menit)
    } else if (menit % 60 == 0) {
        stringResource(R.string.durasi_jam, (menit / 60).toString())
    } else {
        stringResource(R.string.durasi_jam, "${menit / 60},${(menit % 60) * 10 / 60}")
    }
}

@Composable
private fun PilihanKendaraan(
    terpilih: Kendaraan?,
    onSelect: (Kendaraan) -> Unit
) {
    Row(
        modifier = Modifier.selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Kendaraan.entries.forEach { kendaraan ->
            val selected = kendaraan == terpilih
            val shape = RoundedCornerShape(10.dp)
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(shape)
                    .background(if (selected) RutePeachLight else RuteBackground)
                    .border(
                        width = if (selected) 1.5.dp else 1.dp,
                        color = if (selected) RuteOrange else RuteDivider,
                        shape = shape
                    )
                    .selectable(
                        selected = selected,
                        onClick = { onSelect(kendaraan) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = kendaraan.icon(),
                    contentDescription = null,
                    tint = if (selected) RuteOrange else RuteNavy,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(kendaraan.label),
                    fontSize = 12.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    color = if (selected) RuteOrange else RuteNavy
                )
            }
        }
    }
}

private fun Kendaraan.icon(): ImageVector = when (this) {
    Kendaraan.MOBIL -> Icons.Outlined.DirectionsCar
    Kendaraan.MOTOR -> Icons.Outlined.TwoWheeler
    Kendaraan.JALAN_KAKI -> Icons.AutoMirrored.Outlined.DirectionsWalk
}

@Preview(showBackground = true)
@Composable
fun RuteManualScreenPreview() {
    RuteXTheme {
        RuteManualScreen(rememberNavController())
    }
}
