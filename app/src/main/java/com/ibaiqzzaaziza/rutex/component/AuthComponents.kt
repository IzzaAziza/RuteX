package com.ibaiqzzaaziza.rutex.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBackground
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBadge
import com.ibaiqzzaaziza.rutex.ui.theme.RuteInput
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDark
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTopBar(title: String, onBackClick: (() -> Unit)? = null) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = RuteNavy,
                letterSpacing = (-0.45).sp
            )
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = RuteNavy
                    )
                }
            }
        },
        actions = {
            UserAvatar(modifier = Modifier.padding(end = 12.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = RuteBackground)
    )
}

/**
 * Avatar bulat pengguna. Jika [photoUrl] ada (login Google), foto ditampilkan,
 * jika tidak, tampil ikon orang. Avatar bisa diklik bila [onClick] diisi.
 */
@Composable
fun UserAvatar(
    modifier: Modifier = Modifier,
    photoUrl: String = "",
    size: Dp = 32.dp,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(RuteOrangeDark)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        onClickLabel = stringResource(R.string.lihat_profil),
                        role = Role.Button,
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (photoUrl.isNotEmpty()) {
            AsyncImage(
                model = photoUrl,
                contentDescription = stringResource(R.string.profile_desc),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = stringResource(R.string.profile_desc),
                tint = Color.White,
                modifier = Modifier.size(size / 2)
            )
        }
    }
}

/** Logo kubus RuteX beserta tulisan "RUTEX" bergradasi oranye. */
@Composable
fun RuteLogoWithText(modifier: Modifier = Modifier, height: Dp = 60.dp) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.logo_rutex),
            contentDescription = stringResource(R.string.logo_desc),
            modifier = Modifier.size(height)
        )
        Spacer(modifier = Modifier.width(height * 0.08f))
        Text(
            text = stringResource(R.string.app_name).uppercase(),
            style = TextStyle(
                brush = Brush.verticalGradient(listOf(RuteOrange, RuteOrangeDark)),
                fontSize = (height.value * 0.42f).sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
        )
    }
}

@Composable
fun RutePrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        // Selama proses berjalan tombol dinonaktifkan agar tidak terkirim dua kali
        enabled = !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = RuteOrange,
            contentColor = Color.White,
            disabledContainerColor = RuteOrange.copy(alpha = 0.7f),
            disabledContentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier.size(20.dp)
            )
            return@Button
        }
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.14.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun GoogleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = RuteInput,
    elevation: Dp = 0.dp,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = RuteNavy,
            disabledContainerColor = containerColor,
            disabledContentColor = RuteNavy
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = elevation)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = RuteOrange,
                strokeWidth = 2.dp,
                modifier = Modifier.size(20.dp)
            )
            return@Button
        }
        Image(
            painter = painterResource(R.drawable.ic_google),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.14.sp
        )
    }
}

@Composable
fun DividerWithText(text: String, modifier: Modifier = Modifier, lineColor: Color = RuteBadge) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = lineColor)
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = RuteSlate,
            letterSpacing = 0.24.sp
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = lineColor)
    }
}
