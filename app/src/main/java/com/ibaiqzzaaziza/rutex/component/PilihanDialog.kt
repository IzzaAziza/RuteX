package com.ibaiqzzaaziza.rutex.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate

/** Dialog untuk memilih satu opsi (RadioButton). */
@Composable
fun <T> PilihanTunggalDialog(
    title: String,
    options: List<T>,
    selected: T,
    label: @Composable (T) -> String,
    onDismissRequest: () -> Unit,
    onConfirmation: (T) -> Unit
) {
    var pilihan by remember { mutableStateOf(selected) }

    AlertDialog(
        title = { Text(text = title) },
        text = {
            Column(modifier = Modifier.selectableGroup()) {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = option == pilihan,
                                onClick = { pilihan = option },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = option == pilihan,
                            onClick = null,
                            colors = RadioButtonDefaults.colors(selectedColor = RuteOrange, unselectedColor = RuteSlate)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = label(option), fontSize = 15.sp, color = RuteNavy)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirmation(pilihan) }) {
                Text(text = stringResource(R.string.tombol_simpan))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.tombol_batal))
            }
        },
        onDismissRequest = onDismissRequest,
        containerColor = Color.White
    )
}

/** Dialog untuk memilih beberapa opsi sekaligus (Checkbox). Minimal satu opsi harus dipilih. */
@Composable
fun <T> PilihanGandaDialog(
    title: String,
    options: List<T>,
    selected: Set<T>,
    label: @Composable (T) -> String,
    onDismissRequest: () -> Unit,
    onConfirmation: (Set<T>) -> Unit
) {
    var pilihan by remember { mutableStateOf(selected) }

    AlertDialog(
        title = { Text(text = title) },
        text = {
            Column {
                options.forEach { option ->
                    val checked = option in pilihan
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .toggleable(
                                value = checked,
                                onValueChange = { pilihan = if (it) pilihan + option else pilihan - option },
                                role = Role.Checkbox
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = null,
                            colors = CheckboxDefaults.colors(checkedColor = RuteOrange, uncheckedColor = RuteSlate)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = label(option), fontSize = 15.sp, color = RuteNavy)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirmation(pilihan) },
                enabled = pilihan.isNotEmpty()
            ) {
                Text(text = stringResource(R.string.tombol_simpan))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.tombol_batal))
            }
        },
        onDismissRequest = onDismissRequest,
        containerColor = Color.White
    )
}
