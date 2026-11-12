package com.ibaiqzzaaziza.rutex.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FamilyRestroom
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.component.AiCard
import com.ibaiqzzaaziza.rutex.component.AiDropdownField
import com.ibaiqzzaaziza.rutex.component.AiHint
import com.ibaiqzzaaziza.rutex.component.AiLabel
import com.ibaiqzzaaziza.rutex.component.AiTimeField
import com.ibaiqzzaaziza.rutex.component.FormHeader
import com.ibaiqzzaaziza.rutex.component.GenerateRuteButton
import com.ibaiqzzaaziza.rutex.component.KonfirmasiKeluarDialog
import com.ibaiqzzaaziza.rutex.component.RuteBottomBar
import com.ibaiqzzaaziza.rutex.component.RuteMainTopBar
import com.ibaiqzzaaziza.rutex.component.RuteTimePickerDialog
import com.ibaiqzzaaziza.rutex.model.ModeRute
import com.ibaiqzzaaziza.rutex.navigation.Screen
import com.ibaiqzzaaziza.rutex.network.ApiStatus
import com.ibaiqzzaaziza.rutex.screen.RuteAiViewModel.Companion.MAKS_PREFERENSI
import com.ibaiqzzaaziza.rutex.screen.RuteAiViewModel.Companion.MIN_PREFERENSI
import com.ibaiqzzaaziza.rutex.screen.RuteManualViewModel.Companion.FORMAT_JAM
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBackground
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBadge
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBlue
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBrown
import com.ibaiqzzaaziza.rutex.ui.theme.RuteChip
import com.ibaiqzzaaziza.rutex.ui.theme.RuteError
import com.ibaiqzzaaziza.rutex.ui.theme.RuteInput
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDark
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeInk
import com.ibaiqzzaaziza.rutex.ui.theme.RutePeach
import com.ibaiqzzaaziza.rutex.ui.theme.RuteXTheme
import com.ibaiqzzaaziza.rutex.util.ViewModelFactory
import java.time.LocalTime

// Pilihan cepat untuk mengisi preferensi perjalanan
private enum class InspirasiCepat(
    @param:StringRes val label: Int,
    @param:StringRes val kalimat: Int,
    val icon: ImageVector
) {
    KULINER(R.string.inspirasi_kuliner, R.string.inspirasi_kuliner_kalimat, Icons.Outlined.Restaurant),
    KERATON(R.string.inspirasi_keraton, R.string.inspirasi_keraton_kalimat, Icons.Outlined.Storefront),
    KELUARGA(R.string.inspirasi_keluarga, R.string.inspirasi_keluarga_kalimat, Icons.Outlined.FamilyRestroom)
}

@Composable
fun RuteAiTanpaConstraintScreen(navController: NavHostController) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: RuteAiViewModel = viewModel(factory = factory)

    val statusGenerate by viewModel.statusGenerate.collectAsState()
    val lokasi by viewModel.lokasi
    PantauHasilGenerate(viewModel = viewModel, navController = navController)

    var preferensi by rememberSaveable { mutableStateOf("") }
    var lokasiAwal by rememberSaveable { mutableStateOf("") }
    var waktuMulai by rememberSaveable { mutableStateOf(LocalTime.now().withSecond(0).withNano(0)) }

    var tampilkanError by rememberSaveable { mutableStateOf(false) }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    var showKeluarDialog by rememberSaveable { mutableStateOf(false) }

    val preferensiError = preferensi.trim().length < MIN_PREFERENSI
    val lokasiError = lokasiAwal.isBlank()
    val adaIsian = preferensi.isNotBlank()

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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            FormHeader(
                badge = stringResource(R.string.badge_mode_tanpa_constraint),
                title = stringResource(R.string.ai_form_title),
                subtitle = stringResource(R.string.ai_form_subtitle),
                onBackClick = {
                    if (adaIsian) showKeluarDialog = true else navController.popBackStack()
                },
                badgeTextColor = RuteOrangeInk,
                badgeBordered = true,
                titleWeight = FontWeight.Bold,
                subtitleColor = RuteBrown
            )

            PreferensiTanpaConstraintCard(
                preferensi = preferensi,
                onPreferensiChange = { if (it.length <= MAKS_PREFERENSI) preferensi = it },
                isError = tampilkanError && preferensiError,
                onDikteClick = {
                    Toast.makeText(context, R.string.feature_coming_soon, Toast.LENGTH_SHORT).show()
                }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AiCard(
                    modifier = Modifier.weight(1f),
                    bordered = true,
                    contentPadding = 9.dp,
                    verticalSpacing = 6.dp
                ) {
                    AiLabel(
                        icon = Icons.Outlined.LocationOn,
                        text = stringResource(R.string.label_lokasi_awal),
                        iconTint = RuteBlue,
                        fontSize = 12.sp
                    )
                    AiDropdownField(
                        value = lokasiAwal,
                        options = lokasi.map { it.name },
                        onSelect = { lokasiAwal = it },
                        placeholder = stringResource(R.string.placeholder_lokasi_awal),
                        leadingIcon = Icons.Outlined.MyLocation,
                        isError = tampilkanError && lokasiError,
                        contentPadding = PaddingValues(horizontal = 11.dp, vertical = 9.dp)
                    )
                }
                AiCard(
                    modifier = Modifier.weight(1f),
                    bordered = true,
                    contentPadding = 9.dp,
                    verticalSpacing = 6.dp
                ) {
                    AiLabel(
                        icon = Icons.Outlined.AccessTime,
                        text = stringResource(R.string.label_waktu_mulai),
                        fontSize = 12.sp
                    )
                    AiTimeField(
                        value = waktuMulai.format(FORMAT_JAM),
                        trailingIcon = Icons.Outlined.KeyboardArrowDown,
                        onClick = { showTimePicker = true },
                        contentPadding = PaddingValues(horizontal = 11.dp, vertical = 9.dp)
                    )
                    Text(
                        text = stringResource(R.string.waktu_lokal),
                        fontSize = 11.sp,
                        lineHeight = 16.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = RuteBrown,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }

            OtomasiAiCard()

            Column(
                modifier = Modifier.padding(top = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GenerateRuteButton(
                    text = stringResource(R.string.btn_generate_rute),
                    fontSize = 16.sp,
                    isLoading = statusGenerate == ApiStatus.LOADING,
                    onClick = {
                        tampilkanError = true
                        if (preferensiError || lokasiError) {
                            Toast.makeText(context, R.string.error_form_belum_lengkap, Toast.LENGTH_SHORT).show()
                            return@GenerateRuteButton
                        }
                        // Jumlah destinasi ditentukan otomatis pada mode tanpa constraint
                        viewModel.generate(
                            mode = ModeRute.TANPA_CONSTRAINT,
                            preferensi = preferensi,
                            titikAwal = lokasiAwal,
                            jam = waktuMulai,
                            jumlahDestinasi = null
                        )
                    }
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Speed,
                        contentDescription = null,
                        tint = RuteOrange,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.estimasi_kalkulasi),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = RuteBrown,
                        letterSpacing = 0.24.sp
                    )
                }
            }
        }
    }

    if (showTimePicker) {
        RuteTimePickerDialog(
            initialTime = waktuMulai,
            onDismissRequest = { showTimePicker = false },
            onConfirmation = {
                waktuMulai = it
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PreferensiTanpaConstraintCard(
    preferensi: String,
    onPreferensiChange: (String) -> Unit,
    isError: Boolean,
    onDikteClick: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    val textStyle = TextStyle(fontSize = 14.sp, lineHeight = 22.75.sp, color = RuteNavy)

    AiCard(bordered = true, contentPadding = 17.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(RutePeach, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = null,
                    tint = RuteOrangeDark,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.preferensi_title),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
                color = RuteNavy,
                letterSpacing = 0.14.sp,
                modifier = Modifier.weight(1f)
            )
            Row(
                modifier = Modifier
                    .background(RutePeach, CircleShape)
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
                    text = stringResource(R.string.badge_natural_language),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = RuteOrangeDark,
                    letterSpacing = 0.24.sp
                )
            }
        }

        // Area textarea: kolom teks, garis pemisah, lalu petunjuk & tombol Dikte
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(RuteInput, shape)
                .border(1.dp, if (isError) RuteError else RuteBadge.copy(alpha = 0.4f), shape)
                .padding(9.dp)
        ) {
            BasicTextField(
                value = preferensi,
                onValueChange = onPreferensiChange,
                textStyle = textStyle,
                cursorBrush = SolidColor(RuteOrange),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 91.dp),
                decorationBox = { innerTextField ->
                    Box {
                        if (preferensi.isEmpty()) {
                            Text(
                                text = stringResource(R.string.preferensi_placeholder_tanpa),
                                style = textStyle.copy(color = RuteBrown.copy(alpha = 0.6f))
                            )
                        }
                        innerTextField()
                    }
                }
            )
            HorizontalDivider(
                color = RuteBadge.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 10.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AiHint(
                    icon = Icons.Outlined.Info,
                    text = if (isError) {
                        stringResource(R.string.error_preferensi, MIN_PREFERENSI)
                    } else {
                        stringResource(R.string.preferensi_hint_tanpa)
                    },
                    isError = isError,
                    color = RuteBrown,
                    fontSize = 10.sp,
                    modifier = Modifier.weight(1f)
                )
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(RuteBadge)
                        .clickable(role = Role.Button, onClick = onDikteClick)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Mic,
                        contentDescription = null,
                        tint = RuteOrange,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.btn_dikte),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RuteNavy
                    )
                }
            }
        }

        Column(
            modifier = Modifier.padding(top = 4.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    tint = RuteOrangeDark,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.inspirasi_title),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RuteBrown,
                    letterSpacing = 0.24.sp
                )
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                InspirasiCepat.entries.forEach { inspirasi ->
                    val kalimat = stringResource(inspirasi.kalimat)
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(RuteChip)
                            .clickable(role = Role.Button) { onPreferensiChange(kalimat) }
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = inspirasi.icon,
                            contentDescription = null,
                            tint = RuteNavy,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(inspirasi.label),
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = RuteNavy,
                            letterSpacing = 0.24.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OtomasiAiCard() {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(RuteChip, shape)
            .border(1.dp, RuteBadge, shape)
            .padding(17.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(RuteOrange, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Bolt,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(R.string.ai_info_tanpa_title),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
                color = RuteNavy,
                letterSpacing = 0.14.sp
            )
            Text(
                text = stringResource(R.string.ai_info_tanpa),
                fontSize = 14.sp,
                lineHeight = 22.75.sp,
                color = RuteBrown
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RuteAiTanpaConstraintScreenPreview() {
    RuteXTheme {
        RuteAiTanpaConstraintScreen(rememberNavController())
    }
}
