package com.ibaiqzzaaziza.rutex.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val RuteXColorScheme = lightColorScheme(
    primary = RuteOrange,
    onPrimary = Color.White,
    primaryContainer = RutePeach,
    onPrimaryContainer = RuteOrangeDeep,
    secondary = RuteBlue,
    onSecondary = Color.White,
    secondaryContainer = RuteBadge,
    onSecondaryContainer = RuteSlate,
    tertiary = RuteOrangeDark,
    onTertiary = Color.White,
    background = RuteBackground,
    onBackground = RuteNavy,
    surface = Color.White,
    onSurface = RuteNavy,
    surfaceVariant = RuteInput,
    onSurfaceVariant = RuteSlate,
    outline = RuteDivider,
    error = RuteError
)

// Desain RuteX di Figma hanya memakai tema terang,
// jadi aplikasi selalu memakai skema warna RuteX (tanpa dynamic color).
@Composable
fun RuteXTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RuteXColorScheme,
        typography = Typography,
        content = content
    )
}
