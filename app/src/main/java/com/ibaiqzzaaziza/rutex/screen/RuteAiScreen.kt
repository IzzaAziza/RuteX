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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.FamilyRestroom
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.ibaiqzzaaziza.rutex.component.DropdownField
import com.ibaiqzzaaziza.rutex.component.FieldHint
import com.ibaiqzzaaziza.rutex.component.FormCard
import com.ibaiqzzaaziza.rutex.component.FormHeader
import com.ibaiqzzaaziza.rutex.component.KonfirmasiKeluarDialog
import com.ibaiqzzaaziza.rutex.component.RuteBottomBar
import com.ibaiqzzaaziza.rutex.component.RuteMainTopBar
import com.ibaiqzzaaziza.rutex.component.RuteTimePickerDialog
import com.ibaiqzzaaziza.rutex.component.SelectField
import com.ibaiqzzaaziza.rutex.model.ModeRute
import com.ibaiqzzaaziza.rutex.navigation.Screen
import com.ibaiqzzaaziza.rutex.screen.RuteAiViewModel.Companion.MAKS_PREFERENSI
import com.ibaiqzzaaziza.rutex.screen.RuteAiViewModel.Companion.MAKS_TEMPAT
import com.ibaiqzzaaziza.rutex.screen.RuteAiViewModel.Companion.MIN_PREFERENSI
import com.ibaiqzzaaziza.rutex.screen.RuteAiViewModel.Companion.MIN_TEMPAT
import com.ibaiqzzaaziza.rutex.screen.RuteManualViewModel.Companion.FORMAT_JAM
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBackground
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBadge
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBlue
import com.ibaiqzzaaziza.rutex.ui.theme.RuteChip
import com.ibaiqzzaaziza.rutex.ui.theme.RuteDivider
import com.ibaiqzzaaziza.rutex.ui.theme.RuteError
import com.ibaiqzzaaziza.rutex.ui.theme.RuteInput
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDark
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDeep
import com.ibaiqzzaaziza.rutex.ui.theme.RutePeach
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate
import com.ibaiqzzaaziza.rutex.ui.theme.RuteXTheme
import com.ibaiqzzaaziza.rutex.util.ViewModelFactory
import java.time.LocalTime

// Pilihan cepat untuk mengisi preferensi pada mode tanpa constraint
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
fun RuteAiScreen(navController: NavHostController, pakaiConstraint: Boolean) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: RuteAiViewModel = viewModel(factory = factory)

    var preferensi by rememberSaveable { mutableStateOf("") }
    var lokasiAwal by rememberSaveable { mutableStateOf(viewModel.titikAwal.first()) }
    var waktuMulai by rememberSaveable { mutableStateOf(LocalTime.now().withSecond(0).withNano(0)) }
    var jumlahTempat by rememberSaveable { mutableIntStateOf(4) }

    var tampilkanError by rememberSaveable { mutableStateOf(false) }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    var showKeluarDialog by rememberSaveable { mutableStateOf(false) }

    val preferensiError = preferensi.trim().length < MIN_PREFERENSI
    val adaIsian = preferensi.isNotBlank()

    val kembali = {
        if (adaIsian) showKeluarDialog = true else navController.popBackStack()
    }
    BackHandler(enabled = adaIsian) { showKeluarDialog = true }

    Scaffold(
        topBar = { RuteMainTopBar() },
        bottomBar = { RuteBottomBar(navController, Screen.BuatRute.route) },
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
                badge = stringResource(
                    if (pakaiConstraint) R.string.badge_mode_constraint else R.string.badge_mode_tanpa_constraint
                ),
                title = stringResource(R.string.ai_form_title),
                subtitle = stringResource(R.string.ai_form_subtitle),
                onBackClick = { kembali() }
            )

            PreferensiCard(
                pakaiConstraint = pakaiConstraint,
                preferensi = preferensi,
                onPreferensiChange = { if (it.length <= MAKS_PREFERENSI) preferensi = it },
                isError = tampilkanError && preferensiError,
                onDikteClick = {
                    Toast.makeText(context, R.string.feature_coming_soon, Toast.LENGTH_SHORT).show()
                }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FormCard(modifier = Modifier.weight(1f)) {
                    JudulKecil(icon = Icons.Outlined.LocationOn, text = stringResource(R.string.label_lokasi_awal))
                    Spacer(modifier = Modifier.height(8.dp))
                    DropdownField(
                        value = lokasiAwal,
                        options = viewModel.titikAwal,
                        onSelect = { lokasiAwal = it },
                        placeholder = stringResource(R.string.placeholder_titik_awal)
                    )
                }
                FormCard(modifier = Modifier.weight(1f)) {
                    JudulKecil(icon = Icons.Outlined.AccessTime, text = stringResource(R.string.label_waktu_mulai))
                    Spacer(modifier = Modifier.height(8.dp))
                    SelectField(
                        value = waktuMulai.format(FORMAT_JAM),
                        placeholder = stringResource(R.string.placeholder_jam),
                        trailingIcon = Icons.Outlined.AccessTime,
                        onClick = { showTimePicker = true }
                    )
                    if (!pakaiConstraint) {
                        Text(
                            text = stringResource(R.string.waktu_lokal),
                            fontSize = 11.sp,
                            color = RuteSlate,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            }

            if (pakaiConstraint) {
                TargetDestinasiCard(
                    jumlah = jumlahTempat,
                    onKurang = { if (jumlahTempat > MIN_TEMPAT) jumlahTempat-- },
                    onTambah = { if (jumlahTempat < MAKS_TEMPAT) jumlahTempat++ }
                )
                InfoBox(
                    title = null,
                    text = stringResource(R.string.ai_info_constraint),
                    icon = Icons.Outlined.Info
                )
            } else {
                InfoBox(
                    title = stringResource(R.string.ai_info_tanpa_title),
                    text = stringResource(R.string.ai_info_tanpa),
                    icon = Icons.Outlined.Bolt
                )
            }

            GenerateButton(
                onClick = {
                    tampilkanError = true
                    if (preferensiError) {
                        Toast.makeText(context, R.string.error_form_belum_lengkap, Toast.LENGTH_SHORT).show()
                        return@GenerateButton
                    }
                    viewModel.simpan(
                        mode = if (pakaiConstraint) ModeRute.DENGAN_CONSTRAINT else ModeRute.TANPA_CONSTRAINT,
                        preferensi = preferensi,
                        titikAwal = lokasiAwal,
                        jam = waktuMulai,
                        jumlahDestinasi = if (pakaiConstraint) jumlahTempat else 0
                    )
                    Toast.makeText(context, R.string.rencana_ai_tersimpan, Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
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
private fun PreferensiCard(
    pakaiConstraint: Boolean,
    preferensi: String,
    onPreferensiChange: (String) -> Unit,
    isError: Boolean,
    onDikteClick: () -> Unit
) {
    FormCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(RutePeach, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.EditNote,
                    contentDescription = null,
                    tint = RuteOrangeDark,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.preferensi_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = RuteNavy,
                modifier = Modifier.weight(1f)
            )
            if (!pakaiConstraint) {
                Row(
                    modifier = Modifier
                        .background(RutePeach, CircleShape)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(RuteOrange, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.badge_natural_language),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RuteOrangeDeep
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        val shape = RoundedCornerShape(12.dp)
        TextField(
            value = preferensi,
            onValueChange = onPreferensiChange,
            placeholder = {
                Text(
                    text = stringResource(
                        if (pakaiConstraint) R.string.preferensi_placeholder_constraint
                        else R.string.preferensi_placeholder_tanpa
                    ),
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = RuteSlate.copy(alpha = 0.7f)
                )
            },
            isError = isError,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            textStyle = TextStyle(fontSize = 13.sp, lineHeight = 20.sp),
            shape = shape,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = RuteInput,
                unfocusedContainerColor = RuteInput,
                errorContainerColor = RuteInput,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedTextColor = RuteNavy,
                unfocusedTextColor = RuteNavy,
                cursorColor = RuteOrange
            ),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 110.dp)
                .border(1.dp, if (isError) RuteError else RuteDivider, shape)
        )

        Row(
            modifier = Modifier.padding(top = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = if (isError) RuteError else RuteSlate,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isError) {
                        stringResource(R.string.error_preferensi, MIN_PREFERENSI)
                    } else {
                        stringResource(
                            if (pakaiConstraint) R.string.preferensi_hint_constraint
                            else R.string.preferensi_hint_tanpa
                        )
                    },
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    color = if (isError) RuteError else RuteSlate
                )
            }
            Text(
                text = "${preferensi.length}/$MAKS_PREFERENSI",
                fontSize = 11.sp,
                color = RuteSlate,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        if (!pakaiConstraint) {
            TextButton(
                onClick = onDikteClick,
                colors = ButtonDefaults.textButtonColors(containerColor = RuteBadge),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp)
                    .height(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Mic,
                    contentDescription = null,
                    tint = RuteOrange,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.btn_dikte),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RuteNavy
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            JudulKecil(icon = Icons.Outlined.Lightbulb, text = stringResource(R.string.inspirasi_title))
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InspirasiCepat.entries.forEach { inspirasi ->
                    val kalimat = stringResource(inspirasi.kalimat)
                    Row(
                        modifier = Modifier
                            .background(RuteChip, RoundedCornerShape(8.dp))
                            .clickable { onPreferensiChange(kalimat) }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = inspirasi.icon,
                            contentDescription = null,
                            tint = RuteNavy,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(inspirasi.label),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = RuteNavy
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun JudulKecil(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = RuteOrange,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = RuteNavy
        )
    }
}

@Composable
private fun TargetDestinasiCard(
    jumlah: Int,
    onKurang: () -> Unit,
    onTambah: () -> Unit
) {
    FormCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = RuteOrange,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.target_destinasi_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = RuteNavy,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.target_destinasi_saran),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = RuteOrangeDark
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(RuteInput, RoundedCornerShape(12.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.jumlah_tempat_label),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RuteNavy
                )
                Text(
                    text = stringResource(R.string.jumlah_tempat_desc),
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    color = RuteSlate
                )
            }
            StepperButton(
                icon = Icons.Outlined.Remove,
                contentDescription = stringResource(R.string.kurangi_tempat),
                enabled = jumlah > MIN_TEMPAT,
                onClick = onKurang
            )
            Text(
                text = jumlah.toString(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = RuteNavy,
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.Center
            )
            StepperButton(
                icon = Icons.Outlined.Add,
                contentDescription = stringResource(R.string.tambah_tempat),
                enabled = jumlah < MAKS_TEMPAT,
                onClick = onTambah
            )
        }
    }
}

@Composable
private fun StepperButton(
    icon: ImageVector,
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
        modifier = Modifier.size(38.dp)
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun InfoBox(title: String?, text: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(RuteBadge, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        if (title == null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = RuteOrange,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(RuteOrange, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            if (title != null) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = RuteNavy
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(
                text = text,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = if (title == null) RuteBlue else RuteNavy
            )
        }
    }
}

@Composable
private fun GenerateButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = RuteOrange, contentColor = Color.White),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Bolt,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.btn_generate_rute),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, name = "Dengan Constraint")
@Composable
fun RuteAiConstraintPreview() {
    RuteXTheme {
        RuteAiScreen(rememberNavController(), pakaiConstraint = true)
    }
}

@Preview(showBackground = true, name = "Tanpa Constraint")
@Composable
fun RuteAiTanpaConstraintPreview() {
    RuteXTheme {
        RuteAiScreen(rememberNavController(), pakaiConstraint = false)
    }
}
