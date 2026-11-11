package com.ibaiqzzaaziza.rutex.component

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.model.User
import com.ibaiqzzaaziza.rutex.navigation.Screen
import com.ibaiqzzaaziza.rutex.network.UserDataStore
import com.ibaiqzzaaziza.rutex.network.signOut
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBackground
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDark
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuteMainTopBar(navController: NavHostController) {
    val context = LocalContext.current
    val comingSoon = stringResource(R.string.feature_coming_soon)
    val pesanLogout = stringResource(R.string.logout_success)

    val dataStore = remember { UserDataStore(context) }
    val user by dataStore.userFlow.collectAsState(User())
    val scope = rememberCoroutineScope()
    var showProfil by rememberSaveable { mutableStateOf(false) }
    var isLoggingOut by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Column {
                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 24.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = RuteNavy,
                    letterSpacing = (-0.6).sp
                )
                Text(
                    text = stringResource(R.string.app_subtitle),
                    fontSize = 10.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RuteOrangeDark,
                    letterSpacing = 0.24.sp
                )
            }
        },
        actions = {
            IconButton(
                onClick = { Toast.makeText(context, comingSoon, Toast.LENGTH_SHORT).show() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = stringResource(R.string.notifications_desc),
                    tint = RuteNavy
                )
            }
            UserAvatar(
                modifier = Modifier.padding(end = 12.dp),
                photoUrl = user.photoUrl,
                onClick = { showProfil = true }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = RuteBackground)
    )

    if (showProfil) {
        ProfilDialog(
            user = user,
            isLoggingOut = isLoggingOut,
            onDismissRequest = { showProfil = false },
            onConfirmation = {
                // signOut adalah suspend function, jadi dipanggil dari coroutine
                scope.launch {
                    isLoggingOut = true
                    signOut(context, dataStore)
                    isLoggingOut = false
                    showProfil = false
                    Toast.makeText(context, pesanLogout, Toast.LENGTH_SHORT).show()
                    // Seluruh riwayat layar dihapus agar tombol Back tidak kembali ke halaman yang butuh login
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            }
        )
    }
}

private enum class BottomNavItem(
    @param:StringRes val label: Int,
    val icon: ImageVector,
    val route: String?
) {
    BERANDA(R.string.nav_home, Icons.Outlined.Home, Screen.Home.route),
    BUAT_RUTE(R.string.nav_create, Icons.Outlined.AutoAwesome, Screen.BuatRute.route),
    RIWAYAT(R.string.nav_history, Icons.Outlined.History, Screen.Riwayat.route),
    PROFIL(R.string.nav_profile, Icons.Outlined.Person, null)
}

@Composable
fun RuteBottomBar(navController: NavHostController, currentRoute: String) {
    val context = LocalContext.current
    val comingSoon = stringResource(R.string.feature_coming_soon)

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        BottomNavItem.entries.forEach { item ->
            val selected = item.route == currentRoute
            NavigationBarItem(
                selected = selected,
                onClick = {
                    val route = item.route
                    if (route == null) {
                        // Screen Profil belum dibuat
                        Toast.makeText(context, comingSoon, Toast.LENGTH_SHORT).show()
                    } else if (!selected) {
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = stringResource(item.label),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.24.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = RuteOrange,
                    selectedTextColor = RuteOrange,
                    unselectedIconColor = RuteSlate,
                    unselectedTextColor = RuteSlate,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
