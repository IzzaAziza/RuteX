package com.ibaiqzzaaziza.rutex.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.ui.theme.RuteChip
import com.ibaiqzzaaziza.rutex.ui.theme.RuteDivider
import com.ibaiqzzaaziza.rutex.ui.theme.RuteError
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDeep
import com.ibaiqzzaaziza.rutex.ui.theme.RutePeach
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

/**
 * Bagian atas form perencanaan: tombol kembali, badge mode, judul, dan deskripsi.
 */
@Composable
fun FormHeader(
    badge: String,
    title: String,
    subtitle: String,
    onBackClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                colors = IconButtonDefaults.iconButtonColors(containerColor = RuteChip),
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = RuteNavy
                )
            }
            Row(
                modifier = Modifier
                    .background(RutePeach, CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = RuteOrangeDeep,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = badge,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RuteOrangeDeep
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = title,
            fontSize = 22.sp,
            lineHeight = 30.sp,
            fontWeight = FontWeight.Bold,
            color = RuteNavy,
            letterSpacing = (-0.5).sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            fontSize = 13.sp,
            lineHeight = 20.sp,
            color = RuteSlate
        )
    }
}

/** Kartu putih berujung bulat yang membungkus satu kelompok isian form. */
@Composable
fun FormCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 1.dp, shape = shape)
            .clip(shape)
            .background(Color.White)
            .padding(16.dp),
        content = content
    )
}

/** Label isian form. Field wajib diberi tanda bintang merah. */
@Composable
fun FormLabel(text: String, required: Boolean = false, modifier: Modifier = Modifier) {
    Text(
        text = buildAnnotatedString {
            append(text)
            if (required) {
                withStyle(SpanStyle(color = RuteError)) { append("*") }
            }
        },
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = RuteNavy,
        modifier = modifier
    )
}

/** Teks kecil di bawah field: petunjuk (abu-abu miring) atau pesan error (merah). */
@Composable
fun FieldHint(text: String, isError: Boolean = false) {
    Text(
        text = text,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        fontStyle = if (isError) FontStyle.Normal else FontStyle.Italic,
        fontWeight = if (isError) FontWeight.Medium else FontWeight.Normal,
        color = if (isError) RuteError else RuteSlate,
        modifier = Modifier.padding(top = 4.dp)
    )
}

/**
 * Kotak isian yang tidak bisa diketik, hanya bisa diklik
 * (misalnya untuk membuka dialog tanggal/jam atau daftar pilihan).
 */
@Composable
fun SelectField(
    value: String,
    placeholder: String,
    trailingIcon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    isError: Boolean = false
) {
    val shape = RoundedCornerShape(10.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 46.dp)
            .clip(shape)
            .background(MaterialTheme.colorScheme.background)
            .border(1.dp, if (isError) RuteError else RuteDivider, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = RuteOrange,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = value.ifEmpty { placeholder },
            fontSize = 13.sp,
            color = if (value.isEmpty()) RuteSlate.copy(alpha = 0.7f) else RuteNavy,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = trailingIcon,
            contentDescription = null,
            tint = if (isError) RuteError else RuteSlate,
            modifier = Modifier.size(18.dp)
        )
    }
}

/** Daftar pilihan (dropdown) dengan tampilan yang sama seperti [SelectField]. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    value: String,
    options: List<String>,
    onSelect: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    isError: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        SelectField(
            value = value,
            placeholder = placeholder,
            trailingIcon = Icons.Outlined.KeyboardArrowDown,
            onClick = { expanded = true },
            leadingIcon = leadingIcon,
            isError = isError,
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.White
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            fontSize = 14.sp,
                            fontWeight = if (option == value) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (option == value) RuteOrange else RuteNavy
                        )
                    },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

/** Dialog kalender. Tanggal yang sudah lewat tidak bisa dipilih. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuteDatePickerDialog(
    initialDate: LocalDate?,
    onDismissRequest: () -> Unit,
    onConfirmation: (LocalDate) -> Unit
) {
    val hariIni = LocalDate.now()
    val state = rememberDatePickerState(
        initialSelectedDateMillis = initialDate?.toUtcMillis(),
        yearRange = hariIni.year..(hariIni.year + 1),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return !utcTimeMillis.toLocalDate().isBefore(hariIni)
            }

            override fun isSelectableYear(year: Int): Boolean = year >= hariIni.year
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = { state.selectedDateMillis?.let { onConfirmation(it.toLocalDate()) } },
                enabled = state.selectedDateMillis != null
            ) {
                Text(text = stringResource(R.string.tombol_pilih))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.tombol_batal))
            }
        }
    ) {
        DatePicker(state = state)
    }
}

/** Dialog jam dengan format 24 jam. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuteTimePickerDialog(
    initialTime: LocalTime?,
    onDismissRequest: () -> Unit,
    onConfirmation: (LocalTime) -> Unit
) {
    val awal = initialTime ?: LocalTime.of(8, 0)
    val state = rememberTimePickerState(
        initialHour = awal.hour,
        initialMinute = awal.minute,
        is24Hour = true
    )

    TimePickerDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(R.string.pilih_jam)) },
        confirmButton = {
            TextButton(onClick = { onConfirmation(LocalTime.of(state.hour, state.minute)) }) {
                Text(text = stringResource(R.string.tombol_pilih))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.tombol_batal))
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            TimePicker(state = state)
        }
    }
}

/** Dialog konfirmasi sebelum meninggalkan form yang sudah diisi. */
@Composable
fun KonfirmasiKeluarDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        title = { Text(text = stringResource(R.string.keluar_form_title)) },
        text = { Text(text = stringResource(R.string.keluar_form_pesan)) },
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text(text = stringResource(R.string.tombol_keluar), color = RuteError)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.tombol_lanjut_isi))
            }
        },
        onDismissRequest = onDismissRequest,
        containerColor = Color.White
    )
}

// DatePicker Material 3 bekerja dengan milidetik dalam zona UTC
private fun LocalDate.toUtcMillis(): Long =
    atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

private fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()
