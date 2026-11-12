package com.ibaiqzzaaziza.rutex.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.component.RuteBackTopBar
import com.ibaiqzzaaziza.rutex.model.Destinasi
import com.ibaiqzzaaziza.rutex.network.ApiStatus
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBackground
import com.ibaiqzzaaziza.rutex.ui.theme.RuteChip
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDark
import com.ibaiqzzaaziza.rutex.ui.theme.RutePeach
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate
import com.ibaiqzzaaziza.rutex.util.bagikanTeks
import com.ibaiqzzaaziza.rutex.util.bukaPeta

@Composable
fun DetailDestinasiScreen(
    navController: NavHostController,
    id: Int,
    onTambahKeRute: ((Destinasi) -> Unit)? = null
) {
    val context = LocalContext.current
    val viewModel: DetailDestinasiViewModel = viewModel()
    val status by viewModel.status.collectAsState()
    val destinasi by viewModel.destinasi

    // Data diambil 1x saat layar dibuka, sama seperti Modul 08
    LaunchedEffect(id) {
        viewModel.retrieveData(id)
    }

    Scaffold(
        topBar = {
            RuteBackTopBar(
                title = stringResource(R.string.detail_destinasi_title),
                onBackClick = { navController.popBackStack() },
                actions = {
                    val data = destinasi
                    if (data != null) {
                        val teks = stringResource(R.string.bagikan_destinasi, data.name, data.address)
                        IconButton(onClick = { context.bagikanTeks(teks) }) {
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
            val data = destinasi
            if (status == ApiStatus.SUCCESS && data != null) {
                TombolAksiDestinasi(
                    onPetaClick = { context.bukaPeta(data.lat, data.lng, data.name) },
                    onTambahClick = onTambahKeRute?.let { { it(data) } }
                )
            }
        },
        containerColor = RuteBackground
    ) { innerPadding ->
        val data = destinasi
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when {
                status == ApiStatus.LOADING -> CircularProgressIndicator(
                    color = RuteOrange,
                    modifier = Modifier.align(Alignment.Center)
                )
                status == ApiStatus.FAILED || data == null -> Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.error_muat_destinasi),
                        color = RuteSlate,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = { viewModel.retrieveData(id) }) {
                        Text(text = stringResource(R.string.tombol_coba_lagi), color = RuteOrangeDark)
                    }
                }
                else -> IsiDetailDestinasi(data)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun IsiDetailDestinasi(destinasi: Destinasi) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        GaleriFoto(destinasi)

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = stringResource(destinasi.kategori.label),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RuteOrangeDark,
                    modifier = Modifier
                        .background(RutePeach, CircleShape)
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                )
                Text(
                    text = destinasi.name,
                    fontSize = 22.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = RuteNavy
                )
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = RuteSlate,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = destinasi.address,
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = RuteSlate
                    )
                }
            }

            // Ringkasan informasi penting dalam satu kartu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(16.dp))
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(vertical = 12.dp)
            ) {
                InfoDestinasi(
                    icon = Icons.Outlined.Schedule,
                    label = stringResource(R.string.jam_buka),
                    nilai = stringResource(R.string.jam_operasional, destinasi.jamBuka, destinasi.jamTutup),
                    modifier = Modifier.weight(1f)
                )
                InfoDestinasi(
                    icon = Icons.Outlined.ConfirmationNumber,
                    label = stringResource(R.string.tiket_masuk),
                    nilai = hargaTiket(destinasi.ticketPrice),
                    modifier = Modifier.weight(1f)
                )
                InfoDestinasi(
                    icon = Icons.Outlined.Category,
                    label = stringResource(R.string.kategori),
                    nilai = stringResource(destinasi.kategori.label),
                    modifier = Modifier.weight(1f)
                )
            }

            if (destinasi.facilities.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    JudulBagian(text = stringResource(R.string.fasilitas))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        destinasi.facilities.forEach { fasilitas ->
                            Row(
                                modifier = Modifier
                                    .background(RuteChip, CircleShape)
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = null,
                                    tint = RuteOrange,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = fasilitas, fontSize = 12.sp, color = RuteNavy)
                            }
                        }
                    }
                }
            }

            if (destinasi.description.isNotBlank()) {
                HorizontalDivider(color = RuteChip)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    JudulBagian(text = stringResource(R.string.tentang_destinasi))
                    Text(
                        text = destinasi.description,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = RuteSlate
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun GaleriFoto(destinasi: Destinasi) {
    val jumlah = destinasi.images.size.coerceAtLeast(1)
    val pagerState = rememberPagerState(pageCount = { jumlah })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {
        HorizontalPager(state = pagerState) { halaman ->
            FotoDestinasi(
                destinasi = destinasi,
                indexFoto = halaman,
                modifier = Modifier.fillMaxSize()
            )
        }
        if (jumlah > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
                    .background(RuteNavy.copy(alpha = 0.5f), CircleShape)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(jumlah) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == pagerState.currentPage) 8.dp else 6.dp)
                            .clip(CircleShape)
                            .background(if (index == pagerState.currentPage) Color.White else Color.White.copy(alpha = 0.5f))
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoDestinasi(icon: ImageVector, label: String, nilai: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = RuteOrange, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 11.sp, color = RuteSlate)
        Text(
            text = nilai,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = RuteNavy,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun JudulBagian(text: String) {
    Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = RuteNavy)
}

@Composable
private fun TombolAksiDestinasi(
    onPetaClick: () -> Unit,
    onTambahClick: (() -> Unit)?
) {
    Surface(color = Color.White, shadowElevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onPetaClick,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Icon(imageVector = Icons.Outlined.Map, contentDescription = null, tint = RuteNavy, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = stringResource(R.string.buka_di_maps), color = RuteNavy)
            }
            if (onTambahClick != null) {
                Button(
                    onClick = onTambahClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RuteOrange),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(text = stringResource(R.string.tambah_ke_rute), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
