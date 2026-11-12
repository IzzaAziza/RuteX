package com.ibaiqzzaaziza.rutex.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.ibaiqzzaaziza.rutex.BuildConfig
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.component.PilihanGandaDialog
import com.ibaiqzzaaziza.rutex.component.PilihanTunggalDialog
import com.ibaiqzzaaziza.rutex.component.RuteBottomBar
import com.ibaiqzzaaziza.rutex.component.RuteMainTopBar
import com.ibaiqzzaaziza.rutex.model.DurasiPerjalanan
import com.ibaiqzzaaziza.rutex.model.GayaPerjalanan
import com.ibaiqzzaaziza.rutex.model.KategoriDestinasi
import com.ibaiqzzaaziza.rutex.model.PreferensiPerjalanan
import com.ibaiqzzaaziza.rutex.model.User
import com.ibaiqzzaaziza.rutex.navigation.Screen
import com.ibaiqzzaaziza.rutex.network.ApiStatus
import com.ibaiqzzaaziza.rutex.network.UserDataStore
import com.ibaiqzzaaziza.rutex.network.signOut
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBackground
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBadge
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBlue
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBlueLight
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBrown
import com.ibaiqzzaaziza.rutex.ui.theme.RuteChip
import com.ibaiqzzaaziza.rutex.ui.theme.RuteError
import com.ibaiqzzaaziza.rutex.ui.theme.RuteErrorContainer
import com.ibaiqzzaaziza.rutex.ui.theme.RuteErrorDark
import com.ibaiqzzaaziza.rutex.ui.theme.RuteInput
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDark
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDeep
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeInk
import com.ibaiqzzaaziza.rutex.ui.theme.RutePeach
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate
import com.ibaiqzzaaziza.rutex.ui.theme.RuteXTheme
import com.ibaiqzzaaziza.rutex.util.ViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun ProfilScreen(navController: NavHostController) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: ProfilViewModel = viewModel(factory = factory)
    val user by viewModel.user.collectAsState()
    val jumlahRute by viewModel.jumlahRute.collectAsState()
    val preferensi by viewModel.preferensi.collectAsState()
    val notifikasi by viewModel.notifikasi.collectAsState()
    val editStatus by viewModel.editStatus.collectAsState()
    val errorMessage by viewModel.errorMessage

    // Dialog yang sedang terbuka (null jika tidak ada)
    var dialog by rememberSaveable { mutableStateOf<DialogProfil?>(null) }

    val dataStore = remember { UserDataStore(context) }
    val scope = rememberCoroutineScope()
    var isLoggingOut by remember { mutableStateOf(false) }

    val pesanEditSukses = stringResource(R.string.edit_profil_sukses)
    val pesanGagalKoneksi = stringResource(R.string.error_koneksi)
    val pesanLogout = stringResource(R.string.logout_success)

    LaunchedEffect(editStatus) {
        when (editStatus) {
            ApiStatus.SUCCESS -> {
                Toast.makeText(context, pesanEditSukses, Toast.LENGTH_SHORT).show()
                dialog = null
                viewModel.clearEditStatus()
            }
            ApiStatus.FAILED -> {
                Toast.makeText(context, errorMessage ?: pesanGagalKoneksi, Toast.LENGTH_LONG).show()
                viewModel.clearEditStatus()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = { RuteMainTopBar(navController) },
        bottomBar = { RuteBottomBar(navController, Screen.Profil.route) },
        containerColor = RuteBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            ProfilHeader()
            KartuAkun(
                user = user,
                jumlahRute = jumlahRute,
                onEditClick = { dialog = DialogProfil.EDIT_PROFIL }
            )
            PreferensiSection(
                preferensi = preferensi,
                onJenisClick = { dialog = DialogProfil.JENIS_WISATA },
                onDurasiClick = { dialog = DialogProfil.DURASI },
                onGayaClick = { dialog = DialogProfil.GAYA }
            )
            PengaturanSection(
                notifikasi = notifikasi,
                onNotifikasiChange = { viewModel.simpanNotifikasi(it) }
            )
            BantuanSection(
                onBantuanClick = { navController.navigate(Screen.Bantuan.route) },
                onTentangClick = { navController.navigate(Screen.Tentang.route) }
            )
            TombolKeluar(onClick = { dialog = DialogProfil.LOGOUT })

            Text(
                text = stringResource(R.string.profil_footer),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = RuteSlate,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
        }
    }

    when (dialog) {
        DialogProfil.JENIS_WISATA -> PilihanGandaDialog(
            title = stringResource(R.string.pref_jenis_wisata),
            options = KATEGORI_WISATA,
            selected = preferensi.jenisWisata,
            label = { stringResource(it.label) },
            onDismissRequest = { dialog = null },
            onConfirmation = {
                viewModel.simpanJenisWisata(it)
                dialog = null
            }
        )
        DialogProfil.DURASI -> PilihanTunggalDialog(
            title = stringResource(R.string.pref_durasi),
            options = DurasiPerjalanan.entries,
            selected = preferensi.durasi,
            label = { stringResource(it.label) },
            onDismissRequest = { dialog = null },
            onConfirmation = {
                viewModel.simpanDurasi(it)
                dialog = null
            }
        )
        DialogProfil.GAYA -> PilihanTunggalDialog(
            title = stringResource(R.string.pref_gaya),
            options = GayaPerjalanan.entries,
            selected = preferensi.gaya,
            label = { stringResource(it.label) },
            onDismissRequest = { dialog = null },
            onConfirmation = {
                viewModel.simpanGaya(it)
                dialog = null
            }
        )
        DialogProfil.EDIT_PROFIL -> EditNamaDialog(
            namaAwal = user.name,
            isLoading = editStatus == ApiStatus.LOADING,
            onDismissRequest = { dialog = null },
            onConfirmation = { viewModel.editNama(it) }
        )
        DialogProfil.LOGOUT -> AlertDialog(
            title = { Text(text = stringResource(R.string.logout_konfirmasi_title)) },
            text = { Text(text = stringResource(R.string.logout_konfirmasi_pesan)) },
            confirmButton = {
                TextButton(
                    enabled = !isLoggingOut,
                    onClick = {
                        // signOut adalah suspend function, jadi dipanggil dari coroutine
                        scope.launch {
                            isLoggingOut = true
                            signOut(context, dataStore)
                            isLoggingOut = false
                            dialog = null
                            Toast.makeText(context, pesanLogout, Toast.LENGTH_SHORT).show()
                            // Seluruh riwayat layar dihapus agar tombol Back tidak kembali ke halaman yang butuh login
                            navController.navigate(Screen.Login.route) {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.tombol_logout), color = RuteError)
                }
            },
            dismissButton = {
                TextButton(enabled = !isLoggingOut, onClick = { dialog = null }) {
                    Text(text = stringResource(R.string.tombol_batal))
                }
            },
            onDismissRequest = { if (!isLoggingOut) dialog = null },
            containerColor = Color.White
        )
        null -> {}
    }
}

private enum class DialogProfil { JENIS_WISATA, DURASI, GAYA, EDIT_PROFIL, LOGOUT }

@Composable
private fun EditNamaDialog(
    namaAwal: String,
    isLoading: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit
) {
    var nama by rememberSaveable { mutableStateOf(namaAwal) }
    var namaError by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        title = { Text(text = stringResource(R.string.edit_profil)) },
        text = {
            OutlinedTextField(
                value = nama,
                onValueChange = {
                    nama = it
                    namaError = false
                },
                label = { Text(text = stringResource(R.string.label_fullname)) },
                isError = namaError,
                supportingText = if (namaError) {
                    { Text(text = stringResource(R.string.error_fullname_empty)) }
                } else {
                    null
                },
                singleLine = true,
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                enabled = !isLoading,
                onClick = {
                    namaError = nama.isBlank()
                    if (!namaError) onConfirmation(nama.trim())
                }
            ) {
                if (isLoading) {
                    CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                } else {
                    Text(text = stringResource(R.string.tombol_simpan))
                }
            }
        },
        dismissButton = {
            TextButton(enabled = !isLoading, onClick = onDismissRequest) {
                Text(text = stringResource(R.string.tombol_batal))
            }
        },
        onDismissRequest = { if (!isLoading) onDismissRequest() },
        containerColor = Color.White
    )
}

@Composable
private fun TombolKeluar(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(RuteErrorContainer)
            .clickable(role = Role.Button, onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.Logout,
            contentDescription = null,
            tint = RuteErrorDark,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.keluar_dari_akun),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = RuteErrorDark
        )
    }
}

@Composable
private fun JudulSection(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        fontWeight = FontWeight.Bold,
        color = RuteNavy,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
private fun PengaturanSection(
    notifikasi: Boolean,
    onNotifikasiChange: (Boolean) -> Unit
) {
    Column {
        JudulSection(text = stringResource(R.string.pengaturan_title))
        KartuMenu {
            // Baris notifikasi bisa diklik di mana saja untuk mengubah toggle
            Row(
                modifier = Modifier.toggleable(
                    value = notifikasi,
                    onValueChange = onNotifikasiChange,
                    role = Role.Switch
                )
            ) {
                ItemMenu(
                    icon = Icons.Outlined.NotificationsActive,
                    iconBackground = RuteChip,
                    iconTint = RuteNavy,
                    title = stringResource(R.string.pengaturan_notifikasi),
                    subtitle = stringResource(R.string.pengaturan_notifikasi_desc),
                    onClick = null
                ) {
                    Switch(
                        checked = notifikasi,
                        onCheckedChange = null,
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = RuteOrange,
                            checkedThumbColor = Color.White,
                            checkedBorderColor = RuteOrange
                        )
                    )
                }
            }
            PemisahMenu()
            // Saat ini RuteX hanya tersedia dalam Bahasa Indonesia dan tema terang
            ItemMenu(
                icon = Icons.Outlined.Translate,
                iconBackground = RuteChip,
                iconTint = RuteNavy,
                title = stringResource(R.string.pengaturan_bahasa),
                subtitle = stringResource(R.string.pengaturan_bahasa_desc),
                onClick = null
            ) {
                NilaiMenu(text = stringResource(R.string.pengaturan_bahasa_nilai), color = RuteSlate)
            }
            PemisahMenu()
            ItemMenu(
                icon = Icons.Outlined.LightMode,
                iconBackground = RuteChip,
                iconTint = RuteNavy,
                title = stringResource(R.string.pengaturan_tema),
                subtitle = stringResource(R.string.pengaturan_tema_desc),
                onClick = null
            ) {
                NilaiMenu(text = stringResource(R.string.pengaturan_tema_nilai), color = RuteSlate)
            }
        }
    }
}

@Composable
private fun BantuanSection(
    onBantuanClick: () -> Unit,
    onTentangClick: () -> Unit
) {
    Column {
        JudulSection(text = stringResource(R.string.bantuan_section_title))
        KartuMenu {
            ItemMenu(
                icon = Icons.AutoMirrored.Outlined.HelpOutline,
                iconBackground = RuteChip,
                iconTint = RuteNavy,
                title = stringResource(R.string.bantuan_title),
                subtitle = stringResource(R.string.bantuan_desc),
                onClick = onBantuanClick
            )
            PemisahMenu()
            ItemMenu(
                icon = Icons.Outlined.Info,
                iconBackground = RuteChip,
                iconTint = RuteNavy,
                title = stringResource(R.string.tentang_title),
                subtitle = stringResource(R.string.tentang_desc),
                onClick = onTentangClick
            ) {
                Text(
                    text = stringResource(R.string.versi_singkat, BuildConfig.VERSION_NAME),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = RuteBrown,
                    modifier = Modifier
                        .background(RuteBadge, CircleShape)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
    }
}

// Kategori "Semua" tidak relevan sebagai minat, jadi tidak ditampilkan
private val KATEGORI_WISATA = KategoriDestinasi.entries.filter { it != KategoriDestinasi.SEMUA }

@Composable
private fun ProfilHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    modifier = Modifier
                        .background(RuteOrange.copy(alpha = 0.15f), CircleShape)
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Bolt,
                        contentDescription = null,
                        tint = RuteOrangeDark,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.profil_badge),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = RuteOrangeDark,
                        letterSpacing = 0.5.sp
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.profil_konteks),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = RuteSlate
                )
            }
            Text(
                text = stringResource(R.string.profil_title),
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold,
                color = RuteNavy
            )
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(RuteChip, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.VerifiedUser,
                contentDescription = stringResource(R.string.profil_akun_terverifikasi),
                tint = RuteOrange,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun KartuAkun(user: User, jumlahRute: Int, onEditClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, shape)
            .clip(shape)
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarProfil(user = user)
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user.name.ifBlank { stringResource(R.string.profil_tanpa_nama) },
                        fontSize = 18.sp,
                        lineHeight = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = RuteNavy,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.profil_peran),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RuteBrown,
                        modifier = Modifier
                            .background(RuteBadge, CircleShape)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = user.email,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = RuteSlate,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(RuteInput.copy(alpha = 0.6f))
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Explore,
                contentDescription = null,
                tint = RuteSlate,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = pluralStringResource(R.plurals.profil_rute_tersimpan, jumlahRute, jumlahRute),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = RuteSlate,
                modifier = Modifier.weight(1f)
            )
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(role = Role.Button, onClick = onEditClick)
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.edit_profil),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RuteBlue
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null,
                    tint = RuteBlue,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
private fun PreferensiSection(
    preferensi: PreferensiPerjalanan,
    onJenisClick: () -> Unit,
    onDurasiClick: () -> Unit,
    onGayaClick: () -> Unit
) {
    val jenisTerpilih = KATEGORI_WISATA
        .filter { it in preferensi.jenisWisata }
        .map { stringResource(it.label) }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.AutoAwesome,
                contentDescription = null,
                tint = RuteOrange,
                modifier = Modifier.size(17.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.pref_title),
                fontSize = 18.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.Bold,
                color = RuteNavy,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.pref_otomatis_ai),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = RuteOrangeDark
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.pref_desc),
            fontSize = 12.sp,
            lineHeight = 19.5.sp,
            fontWeight = FontWeight.Medium,
            color = RuteSlate
        )
        Spacer(modifier = Modifier.height(12.dp))

        KartuMenu {
            ItemMenu(
                icon = Icons.Outlined.Storefront,
                iconBackground = RutePeach.copy(alpha = 0.6f),
                iconTint = RuteOrangeDark,
                title = stringResource(R.string.pref_jenis_wisata),
                subtitle = jenisTerpilih.joinToString(", "),
                onClick = onJenisClick
            ) {
                Text(
                    text = stringResource(R.string.pref_jumlah_dipilih, jenisTerpilih.size),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = RuteOrangeDeep,
                    modifier = Modifier
                        .background(RutePeach, CircleShape)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
            PemisahMenu()
            ItemMenu(
                icon = Icons.Outlined.Schedule,
                iconBackground = RuteBlueLight.copy(alpha = 0.6f),
                iconTint = RuteNavy,
                title = stringResource(R.string.pref_durasi),
                subtitle = stringResource(R.string.pref_durasi_desc),
                onClick = onDurasiClick
            ) {
                NilaiMenu(text = stringResource(preferensi.durasi.label))
            }
            PemisahMenu()
            ItemMenu(
                icon = Icons.AutoMirrored.Outlined.DirectionsWalk,
                iconBackground = RuteBlueLight.copy(alpha = 0.6f),
                iconTint = RuteNavy,
                title = stringResource(R.string.pref_gaya),
                subtitle = stringResource(preferensi.gaya.deskripsi),
                onClick = onGayaClick
            ) {
                NilaiMenu(text = stringResource(preferensi.gaya.label))
            }
        }
    }
}

/** Kartu putih yang membungkus beberapa [ItemMenu]. */
@Composable
private fun KartuMenu(content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, shape)
            .clip(shape)
            .background(Color.White),
        content = content
    )
}

@Composable
private fun ItemMenu(
    icon: ImageVector,
    iconBackground: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: (() -> Unit)?,
    trailing: @Composable () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .heightIn(min = 56.dp)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconBackground, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = RuteNavy
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium,
                color = RuteSlate,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        trailing()
        if (onClick != null) {
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = RuteSlate,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun NilaiMenu(text: String, color: Color = RuteNavy) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = if (color == RuteNavy) FontWeight.SemiBold else FontWeight.Medium,
        color = color
    )
}

@Composable
private fun PemisahMenu() {
    HorizontalDivider(
        color = RuteChip,
        modifier = Modifier.padding(horizontal = 14.dp)
    )
}

/** Foto akun Google jika ada, jika tidak tampil inisial nama. */
@Composable
private fun AvatarProfil(user: User) {
    Box {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(RutePeach),
            contentAlignment = Alignment.Center
        ) {
            if (user.photoUrl.isNotEmpty()) {
                AsyncImage(
                    model = user.photoUrl,
                    contentDescription = stringResource(R.string.profile_desc),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(
                    text = inisial(user),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = RuteOrangeInk
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 4.dp, y = 4.dp)
                .size(20.dp)
                .border(2.dp, Color.White, CircleShape)
                .background(RuteOrange, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(10.dp)
            )
        }
    }
}

// "Izza Aziza" -> "IZ", jika nama kosong memakai huruf depan email
private fun inisial(user: User): String {
    val sumber = user.name.ifBlank { user.email }
    val kata = sumber.trim().split(" ").filter { it.isNotEmpty() }
    return when {
        kata.isEmpty() -> "?"
        kata.size == 1 -> kata[0].take(2)
        else -> "${kata[0].first()}${kata[1].first()}"
    }.uppercase()
}

@Preview(showBackground = true)
@Composable
fun ProfilScreenPreview() {
    RuteXTheme {
        ProfilScreen(rememberNavController())
    }
}
