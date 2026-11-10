package com.ibaiqzzaaziza.rutex.screen

import androidx.annotation.StringRes
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.EditCalendar
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.component.RuteBottomBar
import com.ibaiqzzaaziza.rutex.component.RuteMainTopBar
import com.ibaiqzzaaziza.rutex.navigation.Screen
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBackground
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBadge
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBlueLight
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBlueSoft
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBrown
import com.ibaiqzzaaziza.rutex.ui.theme.RuteChip
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDark
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDeep
import com.ibaiqzzaaziza.rutex.ui.theme.RutePeach
import com.ibaiqzzaaziza.rutex.ui.theme.RutePeachLight
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate
import com.ibaiqzzaaziza.rutex.ui.theme.RuteXTheme

private enum class ModePerencanaan(
    @param:StringRes val judul: Int,
    @param:StringRes val badge: Int,
    @param:StringRes val deskripsi: Int,
    val tags: List<Int>,
    val icon: ImageVector,
    val iconBackground: Color,
    val iconTint: Color,
    val route: String
) {
    MANUAL(
        judul = R.string.mode_manual_title,
        badge = R.string.mode_manual_badge,
        deskripsi = R.string.mode_manual_desc,
        tags = listOf(
            R.string.mode_manual_tag_1,
            R.string.mode_manual_tag_2,
            R.string.mode_manual_tag_3
        ),
        icon = Icons.Outlined.EditCalendar,
        iconBackground = RuteBlueLight,
        iconTint = RuteNavy,
        route = Screen.RuteManual.route
    ),
    DENGAN_CONSTRAINT(
        judul = R.string.mode_constraint_title,
        badge = R.string.mode_constraint_badge,
        deskripsi = R.string.mode_constraint_desc,
        tags = listOf(
            R.string.mode_constraint_tag_1,
            R.string.mode_constraint_tag_2,
            R.string.mode_constraint_tag_3
        ),
        icon = Icons.Outlined.Tune,
        iconBackground = RutePeach,
        iconTint = RuteOrangeDark,
        route = Screen.RuteAiConstraint.route
    ),
    TANPA_CONSTRAINT(
        judul = R.string.mode_ai_title,
        badge = R.string.mode_ai_badge,
        deskripsi = R.string.mode_ai_desc,
        tags = listOf(
            R.string.mode_ai_tag_1,
            R.string.mode_ai_tag_2,
            R.string.mode_ai_tag_3
        ),
        icon = Icons.Outlined.AutoAwesome,
        iconBackground = RuteBlueSoft,
        iconTint = RuteNavy,
        route = Screen.RuteAiTanpaConstraint.route
    )
}

@Composable
fun BuatRuteScreen(navController: NavHostController) {
    Scaffold(
        topBar = { RuteMainTopBar() },
        bottomBar = { RuteBottomBar(navController, Screen.BuatRute.route) },
        containerColor = RuteBackground
    ) { innerPadding ->
        BuatRuteContent(
            modifier = Modifier.padding(innerPadding),
            onModeClick = { route -> navController.navigate(route) }
        )
    }
}

@Composable
fun BuatRuteContent(
    modifier: Modifier = Modifier,
    onModeClick: (route: String) -> Unit = {}
) {
    // Mode "Dengan Constraint" menjadi pilihan awal karena direkomendasikan
    var modeTerpilih by rememberSaveable { mutableStateOf(ModePerencanaan.DENGAN_CONSTRAINT) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.buat_rute_title),
            fontSize = 24.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Bold,
            color = RuteNavy,
            letterSpacing = (-0.6).sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.buat_rute_subtitle),
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = RuteSlate
        )

        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.metode_penjadwalan),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = RuteNavy,
                letterSpacing = 0.14.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Verified,
                    contentDescription = null,
                    tint = RuteOrangeDark,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = pluralStringResource(
                        R.plurals.jumlah_pilihan,
                        ModePerencanaan.entries.size,
                        ModePerencanaan.entries.size
                    ),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RuteOrangeDark
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = Modifier.selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ModePerencanaan.entries.forEach { mode ->
                ModeCard(
                    mode = mode,
                    selected = mode == modeTerpilih,
                    onClick = {
                        modeTerpilih = mode
                        onModeClick(mode.route)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ModeCard(
    mode: ModePerencanaan,
    selected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = if (selected) 4.dp else 1.dp, shape = shape)
            .clip(shape)
            .background(if (selected) RutePeachLight else Color.White)
            .selectable(selected = selected, onClick = onClick, role = Role.RadioButton)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(44.dp)
                    .background(
                        color = if (selected) RuteOrange else mode.iconBackground,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = mode.icon,
                    contentDescription = null,
                    tint = if (selected) Color.White else mode.iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(mode.judul),
                        fontSize = 18.sp,
                        lineHeight = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = RuteNavy,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Text(
                        text = stringResource(mode.badge),
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (selected) RuteOrangeDeep else RuteSlate,
                        modifier = Modifier
                            .background(
                                color = if (selected) RuteOrange.copy(alpha = 0.2f) else RuteBadge,
                                shape = CircleShape
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = stringResource(mode.deskripsi),
                    fontSize = 14.sp,
                    lineHeight = 19.sp,
                    color = if (selected) RuteBrown else RuteSlate
                )
            }
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            mode.tags.forEach { tag ->
                Text(
                    text = stringResource(tag),
                    fontSize = 12.sp,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                    color = if (selected) RuteOrangeDeep else RuteSlate,
                    letterSpacing = 0.24.sp,
                    modifier = Modifier
                        .background(
                            color = if (selected) RutePeach.copy(alpha = 0.6f) else RuteChip,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BuatRuteScreenPreview() {
    RuteXTheme {
        BuatRuteScreen(rememberNavController())
    }
}
