package com.ibaiqzzaaziza.rutex.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBadge
import com.ibaiqzzaaziza.rutex.ui.theme.RuteError
import com.ibaiqzzaaziza.rutex.ui.theme.RuteInput
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate

/**
 * Kartu putih form AI.
 * [bordered] = true dipakai mode tanpa constraint (garis tipis),
 * selain itu kartu memakai bayangan halus seperti mode dengan constraint.
 */
@Composable
fun AiCard(
    modifier: Modifier = Modifier,
    bordered: Boolean = false,
    contentPadding: Dp = 16.dp,
    verticalSpacing: Dp = 8.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = if (bordered) 1.dp else 2.dp, shape = shape, spotColor = RuteNavy.copy(alpha = 0.2f))
            .clip(shape)
            .background(Color.White)
            .border(1.dp, if (bordered) RuteBadge.copy(alpha = 0.6f) else Color.Transparent, shape)
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
        content = content
    )
}

/** Judul kecil berikon di kartu form AI, misalnya "Lokasi Awal" dan "Waktu Mulai". */
@Composable
fun AiLabel(
    icon: ImageVector,
    text: String,
    iconTint: Color = RuteOrange,
    fontSize: TextUnit = 13.sp
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = RuteNavy,
            letterSpacing = 0.24.sp
        )
    }
}

/** Kotak isian satu baris berlatar biru muda yang bisa diketik. */
@Composable
fun AiInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    isError: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(horizontal = 13.dp, vertical = 11.dp)
) {
    val shape = RoundedCornerShape(12.dp)
    val textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = RuteNavy)

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = textStyle,
        cursorBrush = SolidColor(RuteOrange),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Done
        ),
        modifier = modifier.fillMaxWidth(),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(RuteInput, shape)
                    .border(1.dp, if (isError) RuteError else RuteBadge, shape)
                    .padding(contentPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = RuteOrange,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = textStyle.copy(color = RuteSlate.copy(alpha = 0.6f), fontWeight = FontWeight.Medium)
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

/** Kotak jam yang membuka dialog pemilih jam ketika diklik. */
@Composable
fun AiTimeField(
    value: String,
    trailingIcon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 13.dp, vertical = 11.dp)
) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(RuteInput)
            .border(1.dp, RuteBadge, shape)
            .clickable(onClick = onClick)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = value,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Bold,
            color = RuteNavy,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = trailingIcon,
            contentDescription = null,
            tint = RuteSlate,
            modifier = Modifier.size(16.dp)
        )
    }
}

/** Daftar pilihan dengan tampilan kotak isian form AI (dipakai untuk lokasi awal). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiDropdownField(
    value: String,
    options: List<String>,
    onSelect: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    isError: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(horizontal = 13.dp, vertical = 11.dp)
) {
    var expanded by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(12.dp)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
                .clip(shape)
                .background(RuteInput)
                .border(1.dp, if (isError) RuteError else RuteBadge, shape)
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Icon(imageVector = leadingIcon, contentDescription = null, tint = RuteOrange, modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = value.ifEmpty { placeholder },
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = if (value.isEmpty()) FontWeight.Medium else FontWeight.SemiBold,
                color = if (value.isEmpty()) RuteSlate.copy(alpha = 0.7f) else RuteNavy,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                tint = RuteSlate,
                modifier = Modifier.size(16.dp)
            )
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.White
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option, fontSize = 14.sp, color = RuteNavy) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

/** Tombol oranye berbentuk pil dengan ikon petir. */
@Composable
fun GenerateRuteButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 14.sp,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = RuteOrange,
            contentColor = Color.White,
            disabledContainerColor = RuteOrange.copy(alpha = 0.7f),
            disabledContentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp, pressedElevation = 2.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = stringResource(R.string.menyusun_rute), fontSize = fontSize, fontWeight = FontWeight.Bold)
            return@Button
        }
        Icon(
            imageVector = Icons.Outlined.Bolt,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.2.sp
        )
    }
}

/** Baris kecil di bawah textarea: ikon info + petunjuk, atau pesan error jika ada. */
@Composable
fun AiHint(
    icon: ImageVector,
    text: String,
    isError: Boolean,
    modifier: Modifier = Modifier,
    color: Color = RuteSlate,
    fontSize: TextUnit = 12.sp
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isError) RuteError else color,
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = fontSize,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (isError) RuteError else color,
            letterSpacing = 0.24.sp,
            modifier = Modifier.padding(end = 4.dp)
        )
    }
}
