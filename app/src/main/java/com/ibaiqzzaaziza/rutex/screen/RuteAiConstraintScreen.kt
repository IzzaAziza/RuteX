package com.ibaiqzzaaziza.rutex.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.component.AiCard
import com.ibaiqzzaaziza.rutex.component.AiHint
import com.ibaiqzzaaziza.rutex.component.AiInputField
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
import com.ibaiqzzaaziza.rutex.screen.RuteAiViewModel.Companion.MAKS_PREFERENSI
import com.ibaiqzzaaziza.rutex.screen.RuteAiViewModel.Companion.MAKS_TEMPAT
import com.ibaiqzzaaziza.rutex.screen.RuteAiViewModel.Companion.MIN_PREFERENSI
import com.ibaiqzzaaziza.rutex.screen.RuteAiViewModel.Companion.MIN_TEMPAT
import com.ibaiqzzaaziza.rutex.screen.RuteManualViewModel.Companion.FORMAT_JAM
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBackground
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBadge
import com.ibaiqzzaaziza.rutex.ui.theme.RuteError
import com.ibaiqzzaaziza.rutex.ui.theme.RuteInput
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDark
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate
import com.ibaiqzzaaziza.rutex.ui.theme.RuteXTheme
import com.ibaiqzzaaziza.rutex.util.ViewModelFactory
import java.time.LocalTime

@Composable
fun RuteAiConstraintScreen(navController: NavHostController) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: RuteAiViewModel = viewModel(factory = factory)

    val lokasiDefault = stringResource(R.string.default_lokasi_awal)
    var preferensi by rememberSaveable { mutableStateOf("") }
    var lokasiAwal by rememberSaveable { mutableStateOf(lokasiDefault) }
    var waktuMulai by rememberSaveable { mutableStateOf(LocalTime.now().withSecond(0).withNano(0)) }
    var jumlahTempat by rememberSaveable { mutableIntStateOf(4) }

    var tampilkanError by rememberSaveable { mutableStateOf(false) }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    var showKeluarDialog by rememberSaveable { mutableStateOf(false) }

    val preferensiError = preferensi.trim().length < MIN_PREFERENSI
    val lokasiError = lokasiAwal.isBlank()
    val adaIsian = preferensi.isNotBlank()

    BackHandler(enabled = adaIsian) { showKeluarDialog = true }

    val generate = {
        tampilkanError = true
        if (preferensiError || lokasiError) {
            Toast.makeText(context, R.string.error_form_belum_lengkap, Toast.LENGTH_SHORT).show()
        } else {
            viewModel.simpan(
                mode = ModeRute.DENGAN_CONSTRAINT,
                preferensi = preferensi,
                titikAwal = lokasiAwal,
                jam = waktuMulai,
                jumlahDestinasi = jumlahTempat
            )
            Toast.makeText(context, R.string.rencana_ai_tersimpan, Toast.LENGTH_SHORT).show()
            // Rencana yang baru disimpan langsung terlihat di Riwayat
            navController.navigate(Screen.Riwayat.route) {
                popUpTo(Screen.Home.route)
            }
        }
    }

    Scaffold(
        topBar = { RuteMainTopBar(navController) },
        bottomBar = {
            Column {
                // Tombol Generate menempel di atas bottom navigation, sesuai desain
                Surface(
                    color = Color.White.copy(alpha = 0.95f),
                    shadowElevation = 12.dp
                ) {
                    GenerateRuteButton(
                        text = stringResource(R.string.btn_generate_rute),
                        onClick = { generate() },
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp)
                    )
                }
                RuteBottomBar(navController, Screen.BuatRute.route)
            }
        },
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
            FormHeader(
                badge = stringResource(R.string.badge_mode_constraint),
                title = stringResource(R.string.ai_form_title),
                subtitle = stringResource(R.string.ai_form_subtitle),
                onBackClick = {
                    if (adaIsian) showKeluarDialog = true else navController.popBackStack()
                }
            )

            PreferensiConstraintCard(
                preferensi = preferensi,
                onPreferensiChange = { if (it.length <= MAKS_PREFERENSI) preferensi = it },
                isError = tampilkanError && preferensiError
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AiCard(modifier = Modifier.weight(1f), contentPadding = 14.dp) {
                    AiLabel(icon = Icons.Outlined.LocationOn, text = stringResource(R.string.label_lokasi_awal))
                    AiInputField(
                        value = lokasiAwal,
                        onValueChange = { lokasiAwal = it },
                        placeholder = stringResource(R.string.placeholder_lokasi_awal),
                        isError = tampilkanError && lokasiError
                    )
                }
                AiCard(modifier = Modifier.weight(1f), contentPadding = 14.dp) {
                    AiLabel(icon = Icons.Outlined.AccessTime, text = stringResource(R.string.label_waktu_mulai))
                    AiTimeField(
                        value = waktuMulai.format(FORMAT_JAM),
                        trailingIcon = Icons.Outlined.AccessTime,
                        onClick = { showTimePicker = true }
                    )
                }
            }

            TargetDestinasiCard(
                jumlah = jumlahTempat,
                onKurang = { if (jumlahTempat > MIN_TEMPAT) jumlahTempat-- },
                onTambah = { if (jumlahTempat < MAKS_TEMPAT) jumlahTempat++ }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(RuteBadge.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = RuteOrange,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = stringResource(R.string.ai_info_constraint),
                    fontSize = 12.sp,
                    lineHeight = 19.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = RuteSlate,
                    letterSpacing = 0.24.sp
                )
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

@Composable
private fun PreferensiConstraintCard(
    preferensi: String,
    onPreferensiChange: (String) -> Unit,
    isError: Boolean
) {
    val shape = RoundedCornerShape(12.dp)
    val textStyle = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.24.sp,
        color = RuteNavy
    )

    AiCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.EditNote,
                contentDescription = null,
                tint = RuteOrange,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.preferensi_title),
                fontSize = 18.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.Bold,
                color = RuteNavy
            )
        }

        BasicTextField(
            value = preferensi,
            onValueChange = onPreferensiChange,
            textStyle = textStyle,
            cursorBrush = SolidColor(RuteOrange),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 90.dp)
                        .background(RuteInput, shape)
                        .border(1.dp, if (isError) RuteError else RuteBadge, shape)
                        .padding(15.dp)
                ) {
                    if (preferensi.isEmpty()) {
                        Text(
                            text = stringResource(R.string.preferensi_placeholder_constraint),
                            style = textStyle.copy(color = RuteSlate)
                        )
                    }
                    innerTextField()
                }
            }
        )

        AiHint(
            icon = Icons.Outlined.Info,
            text = if (isError) {
                stringResource(R.string.error_preferensi, MIN_PREFERENSI)
            } else {
                stringResource(R.string.preferensi_hint_constraint)
            },
            isError = isError,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun TargetDestinasiCard(
    jumlah: Int,
    onKurang: () -> Unit,
    onTambah: () -> Unit
) {
    AiCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = RuteOrange,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.target_destinasi_title),
                fontSize = 18.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.Bold,
                color = RuteNavy,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.target_destinasi_saran),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = RuteOrangeDark,
                letterSpacing = 0.24.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(RuteInput, RoundedCornerShape(12.dp))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.jumlah_tempat_label),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RuteNavy
                )
                Text(
                    text = stringResource(R.string.jumlah_tempat_desc),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = RuteSlate,
                    letterSpacing = 0.24.sp
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StepperButton(
                    simbol = "-",
                    contentDescription = stringResource(R.string.kurangi_tempat),
                    enabled = jumlah > MIN_TEMPAT,
                    onClick = onKurang
                )
                Text(
                    text = jumlah.toString(),
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = RuteNavy,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(28.dp)
                )
                StepperButton(
                    simbol = "+",
                    contentDescription = stringResource(R.string.tambah_tempat),
                    enabled = jumlah < MAKS_TEMPAT,
                    onClick = onTambah
                )
            }
        }
    }
}

@Composable
private fun StepperButton(
    simbol: String,
    contentDescription: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    FilledIconButton(
        onClick = onClick,
        enabled = enabled,
        shape = CircleShape,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = Color.White,
            contentColor = RuteNavy,
            disabledContainerColor = Color.White.copy(alpha = 0.6f),
            disabledContentColor = RuteSlate.copy(alpha = 0.4f)
        ),
        modifier = Modifier.size(40.dp)
    ) {
        // Simbol berupa teks agar sama persis dengan desain, label untuk pembaca layar diberikan terpisah
        Text(
            text = simbol,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clearAndSetSemantics { this.contentDescription = contentDescription }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RuteAiConstraintScreenPreview() {
    RuteXTheme {
        RuteAiConstraintScreen(rememberNavController())
    }
}
