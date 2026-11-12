package com.ibaiqzzaaziza.rutex.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.AltRoute
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.component.RuteBottomBar
import com.ibaiqzzaaziza.rutex.component.RuteMainTopBar
import com.ibaiqzzaaziza.rutex.model.Destinasi
import com.ibaiqzzaaziza.rutex.model.KategoriDestinasi
import com.ibaiqzzaaziza.rutex.model.RencanaRute
import com.ibaiqzzaaziza.rutex.navigation.Screen
import com.ibaiqzzaaziza.rutex.network.ApiStatus
import com.ibaiqzzaaziza.rutex.network.RuteXApi
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBackground
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBadge
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBlue
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBlueLight
import com.ibaiqzzaaziza.rutex.ui.theme.RuteChip
import com.ibaiqzzaaziza.rutex.ui.theme.RuteGreen
import com.ibaiqzzaaziza.rutex.ui.theme.RuteInput
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavyDeep
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavyMid
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDark
import com.ibaiqzzaaziza.rutex.ui.theme.RutePeach
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate
import com.ibaiqzzaaziza.rutex.ui.theme.RuteXTheme
import com.ibaiqzzaaziza.rutex.util.ViewModelFactory
import com.ibaiqzzaaziza.rutex.util.formatRupiah
import com.ibaiqzzaaziza.rutex.util.formatTanggalRencana

@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = { RuteMainTopBar(navController) },
        bottomBar = { RuteBottomBar(navController, Screen.Home.route) },
        containerColor = RuteBackground
    ) { innerPadding ->
        HomeContent(
            modifier = Modifier.padding(innerPadding),
            onDestinasiClick = { navController.navigate(Screen.DetailDestinasi.withId(it.id)) },
            onTambahKeRute = { navController.navigate(Screen.RuteManual.withDestinasi(it.name)) },
            onRuteClick = { navController.navigate(Screen.DetailRute.withId(it.id)) },
            onTipsClick = { navController.navigate(Screen.Bantuan.route) },
            onLihatSemua = {
                navController.navigate(Screen.Riwayat.route) {
                    popUpTo(Screen.Home.route) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onStartRoute = {
                navController.navigate(Screen.BuatRute.route) {
                    popUpTo(Screen.Home.route) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    onDestinasiClick: (Destinasi) -> Unit = {},
    onTambahKeRute: (Destinasi) -> Unit = {},
    onRuteClick: (RencanaRute) -> Unit = {},
    onLihatSemua: () -> Unit = {},
    onTipsClick: () -> Unit = {},
    onStartRoute: () -> Unit
) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: HomeViewModel = viewModel(factory = factory)
    val ruteTerakhir by viewModel.ruteTerakhir.collectAsState()

    var query by rememberSaveable { mutableStateOf("") }
    var kategori by rememberSaveable { mutableStateOf(KategoriDestinasi.SEMUA) }

    val status by viewModel.status.collectAsState()
    val semuaDestinasi by viewModel.destinasiPopuler

    // Destinasi difilter berdasarkan chip kategori dan kata kunci pencarian
    val destinasi = semuaDestinasi.filter {
        (kategori == KategoriDestinasi.SEMUA || it.kategori == kategori) &&
                (query.isBlank() || it.name.contains(query.trim(), ignoreCase = true))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            HomeHeader()
            Spacer(modifier = Modifier.height(16.dp))
            HomeSearchBar(query = query, onQueryChange = { query = it })
            Spacer(modifier = Modifier.height(24.dp))
            AiBannerSection(onStartRoute = onStartRoute)
            Spacer(modifier = Modifier.height(32.dp))
            RecentRouteSection(
                rencana = ruteTerakhir,
                onClick = onRuteClick,
                onLihatSemua = onLihatSemua,
                onBuatRute = onStartRoute
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        PopularDestinationsSection(
            status = status,
            destinasi = destinasi,
            kategoriTerpilih = kategori,
            onKategoriClick = { kategori = it },
            onRetry = { viewModel.retrieveData() },
            onDestinasiClick = onDestinasiClick,
            // Tombol + membuka Rute Manual dengan destinasi ini sebagai tujuan pertama
            onAddClick = onTambahKeRute
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(32.dp))
            TipsSection(onClick = onTipsClick)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HomeHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(R.string.greeting),
            color = RuteOrangeDark,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
        )
        Text(
            text = stringResource(R.string.main_question),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 30.sp,
            letterSpacing = (-0.6).sp,
            color = RuteNavy
        )
        Text(
            text = stringResource(R.string.sub_question),
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = RuteSlate
        )
    }
}

@Composable
private fun HomeSearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = stringResource(R.string.search_placeholder),
                fontSize = 14.sp,
                color = RuteSlate
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                tint = RuteSlate
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 1.dp, shape = CircleShape),
        shape = CircleShape,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = RuteNavy,
            unfocusedTextColor = RuteNavy,
            cursorColor = RuteOrange
        )
    )
}

@Composable
private fun AiBannerSection(onStartRoute: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(colors = listOf(RuteNavy, RuteNavyMid, RuteNavyDeep))
            )
            .drawBehind {
                // Cahaya oranye lembut di pojok kanan atas
                val glowCenter = Offset(size.width - 48.dp.toPx(), 48.dp.toPx())
                val glowRadius = 140.dp.toPx()
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(RuteOrange.copy(alpha = 0.25f), Color.Transparent),
                        center = glowCenter,
                        radius = glowRadius
                    ),
                    radius = glowRadius,
                    center = glowCenter
                )
            }
            .padding(24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.1f), CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = RutePeach,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.ai_badge),
                    color = RutePeach,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.6.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.ai_title),
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                lineHeight = 30.sp,
                letterSpacing = (-0.6).sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.ai_desc),
                color = RuteBadge.copy(alpha = 0.9f),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier.widthIn(max = 270.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onStartRoute,
                colors = ButtonDefaults.buttonColors(containerColor = RuteOrange),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = CircleShape
            ) {
                Text(
                    text = stringResource(R.string.ai_btn),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        color = RuteNavy
    )
}

@Composable
private fun InfoPill(
    icon: ImageVector,
    iconTint: Color,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(RuteInput, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = RuteNavy
        )
    }
}

@Composable
private fun RecentRouteSection(
    rencana: RencanaRute?,
    onClick: (RencanaRute) -> Unit,
    onLihatSemua: () -> Unit,
    onBuatRute: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.AltRoute,
                    contentDescription = null,
                    tint = RuteOrangeDark,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                SectionTitle(text = stringResource(R.string.recent_routes))
            }
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(role = Role.Button, onClick = onLihatSemua)
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.see_all),
                    color = RuteBlue,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = RuteBlue,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        if (rencana == null) {
            KartuBelumAdaRute(onBuatRute = onBuatRute)
            return@Column
        }

        val destinasi = rencana.itinerary.drop(1).map { it.nama }
            .ifEmpty { rencana.destinasi.map { it.nama } }
        val selesai = rencana.itinerary.lastOrNull()?.berangkat

        Card(
            onClick = { onClick(rencana) },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = formatTanggalRencana(rencana.tanggal),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = RuteSlate
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(RuteSlate.copy(alpha = 0.5f), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(RuteChip, CircleShape)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(RuteGreen, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(R.string.status_tersimpan),
                                fontSize = 12.sp,
                                color = RuteSlate,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = destinasi.joinToString(" & "),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        lineHeight = 26.sp,
                        color = RuteNavy,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoPill(
                        icon = Icons.Outlined.Place,
                        iconTint = RuteOrangeDark,
                        text = stringResource(R.string.jumlah_destinasi, destinasi.size)
                    )
                    if (selesai != null) {
                        InfoPill(
                            icon = Icons.Outlined.Schedule,
                            iconTint = RuteBlue,
                            text = stringResource(R.string.rentang_jam, rencana.jamMulai, selesai)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(RuteInput.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = RuteOrangeDark,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.mulai_dari, rencana.titikAwal),
                            fontSize = 12.sp,
                            color = RuteNavy,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                        contentDescription = null,
                        tint = RuteSlate,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun KartuBelumAdaRute(onBuatRute: () -> Unit) {
    Card(
        onClick = onBuatRute,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(RutePeach, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = null, tint = RuteOrangeDark)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.belum_ada_rute),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = RuteNavy
                )
                Text(
                    text = stringResource(R.string.belum_ada_rute_desc),
                    fontSize = 12.sp,
                    color = RuteSlate
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = RuteSlate
            )
        }
    }
}

@Composable
private fun PopularDestinationsSection(
    status: ApiStatus,
    destinasi: List<Destinasi>,
    kategoriTerpilih: KategoriDestinasi,
    onKategoriClick: (KategoriDestinasi) -> Unit,
    onRetry: () -> Unit,
    onDestinasiClick: (Destinasi) -> Unit,
    onAddClick: (Destinasi) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SectionTitle(text = stringResource(R.string.popular_destinations))
            Text(
                text = stringResource(R.string.popular_desc),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = RuteSlate
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(KategoriDestinasi.entries) { kategori ->
                CategoryChip(
                    text = stringResource(kategori.label),
                    selected = kategori == kategoriTerpilih,
                    onClick = { onKategoriClick(kategori) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        if (status == ApiStatus.LOADING) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = RuteOrange)
            }
        } else if (status == ApiStatus.FAILED) {
            // Modul 13: tampilkan pesan dan tombol coba lagi saat data gagal dimuat
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.error_muat_destinasi),
                    fontSize = 14.sp,
                    color = RuteSlate,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = onRetry) {
                    Text(text = stringResource(R.string.tombol_coba_lagi), color = RuteOrangeDark)
                }
            }
        } else if (destinasi.isEmpty()) {
            Text(
                text = stringResource(R.string.empty_destination),
                fontSize = 14.sp,
                color = RuteSlate,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 32.dp)
            )
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(destinasi, key = { it.id }) { item ->
                    DestinationCard(
                        destinasi = item,
                        onClick = { onDestinasiClick(item) },
                        onAddClick = { onAddClick(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (selected) RuteNavy else Color.White,
        shadowElevation = 1.dp,
        modifier = Modifier.height(36.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                color = if (selected) Color.White else RuteNavy,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun DestinationCard(destinasi: Destinasi, onClick: () -> Unit, onAddClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.width(260.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(144.dp)
        ) {
            FotoDestinasi(
                destinasi = destinasi,
                modifier = Modifier.fillMaxSize()
            )

            Text(
                text = stringResource(destinasi.kategori.label),
                color = RuteOrangeDark,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .background(Color.White.copy(alpha = 0.9f), CircleShape)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
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
                    text = stringResource(R.string.jam_operasional, destinasi.jamBuka, destinasi.jamTutup),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(14.dp)
                .heightIn(min = 108.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = destinasi.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 26.sp,
                color = RuteNavy,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = RuteSlate,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = destinasi.address,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = RuteSlate,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.tiket_masuk),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = RuteSlate
                    )
                    Text(
                        text = hargaTiket(destinasi.ticketPrice),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = RuteOrangeDark
                    )
                }
                Surface(
                    onClick = onAddClick,
                    shape = CircleShape,
                    color = RutePeach,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = stringResource(R.string.add_to_plan_desc),
                            tint = RuteOrangeDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun hargaTiket(harga: Int?): String = when {
    harga == null -> stringResource(R.string.harga_tidak_diketahui)
    harga == 0 -> stringResource(R.string.gratis)
    else -> formatRupiah(harga)
}

/**
 * Foto utama destinasi dari server (Modul 12–13).
 * Selama dimuat tampil warna gradasi, dan jika gagal tampil ikon gambar rusak.
 */
@Composable
fun FotoDestinasi(destinasi: Destinasi, modifier: Modifier = Modifier, indexFoto: Int = 0) {
    val path = destinasi.images.getOrNull(indexFoto)?.image
    Box(
        modifier = modifier.background(Brush.linearGradient(listOf(RutePeach, RuteBlueLight))),
        contentAlignment = Alignment.Center
    ) {
        if (path == null) {
            Icon(
                imageVector = Icons.Outlined.Landscape,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(RuteXApi.getImageUrl(path))
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.destination_image_desc, destinasi.name),
                contentScale = ContentScale.Crop,
                placeholder = rememberVectorPainter(Icons.Outlined.Image),
                error = rememberVectorPainter(Icons.Outlined.BrokenImage),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun TipsSection(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(RuteBadge)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(RuteOrange.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.TipsAndUpdates,
                contentDescription = null,
                tint = RuteOrange,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.tips_title),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = RuteNavy
            )
            Text(
                text = stringResource(R.string.tips_desc),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium,
                color = RuteSlate,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = RuteSlate
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    RuteXTheme {
        HomeScreen(rememberNavController())
    }
}
