package com.ibaiqzzaaziza.rutex.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.navigation.Screen
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBackground
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBadge
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBlue
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBlueLight
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBrown
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBrownMuted
import com.ibaiqzzaaziza.rutex.ui.theme.RuteInput
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDark
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate
import com.ibaiqzzaaziza.rutex.ui.theme.RuteXTheme

private const val SPLASH_DURATION_MS = 2500

@Composable
fun SplashScreen(navController: NavHostController) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = SPLASH_DURATION_MS, easing = LinearEasing)
        )
        // Splash tidak boleh muncul lagi ketika pengguna menekan tombol Back
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    SplashContent(progress = progress.value)
}

@Composable
fun SplashContent(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RuteBackground)
    ) {
        SplashBackgroundGlow()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(vertical = 24.dp)
        ) {
            SplashTopRow()

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SplashLogo()
                Spacer(modifier = Modifier.height(24.dp))
                SplashBrand()
            }

            SplashBottomSection(progress = progress)
        }
    }
}

@Composable
private fun SplashBackgroundGlow() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val orangeCenter = Offset(size.width - 96.dp.toPx(), 96.dp.toPx())
        val orangeRadius = 260.dp.toPx()
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(RuteOrange.copy(alpha = 0.12f), Color.Transparent),
                center = orangeCenter,
                radius = orangeRadius
            ),
            radius = orangeRadius,
            center = orangeCenter
        )

        val blueCenter = Offset(32.dp.toPx(), size.height / 2 + 160.dp.toPx())
        val blueRadius = 230.dp.toPx()
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(RuteBlue.copy(alpha = 0.1f), Color.Transparent),
                center = blueCenter,
                radius = blueRadius
            ),
            radius = blueRadius,
            center = blueCenter
        )

        val lavenderCenter = Offset(size.width - 200.dp.toPx(), size.height - 80.dp.toPx())
        val lavenderRadius = 220.dp.toPx()
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(RuteBlueLight.copy(alpha = 0.25f), Color.Transparent),
                center = lavenderCenter,
                radius = lavenderRadius
            ),
            radius = lavenderRadius,
            center = lavenderCenter
        )
    }
}

@Composable
private fun SplashTopRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .shadow(elevation = 1.dp, shape = CircleShape)
                .background(Color.White, CircleShape)
                .padding(start = 12.dp, end = 14.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(RuteOrange, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.cirebon_edition),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = RuteNavy,
                letterSpacing = 0.3.sp
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.Verified,
                contentDescription = null,
                tint = RuteSlate,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.smart_city_ai),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = RuteSlate
            )
        }
    }
}

@Composable
private fun SplashLogo() {
    Box(
        modifier = Modifier.size(192.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(RuteOrange.copy(alpha = 0.18f), Color.Transparent)
                ),
                radius = size.minDimension / 2
            )

            val outerRadius = size.minDimension / 2 - 6.dp.toPx()
            drawCircle(
                color = RuteBlueLight,
                radius = outerRadius,
                style = Stroke(
                    width = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(4.dp.toPx(), 4.dp.toPx())
                    )
                )
            )

            val innerRadius = outerRadius - 10.dp.toPx()
            drawCircle(
                color = RuteNavy,
                radius = innerRadius,
                style = Stroke(
                    width = 1.5.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(10.dp.toPx(), 8.dp.toPx())
                    )
                )
            )

            drawCircle(
                color = RuteOrange,
                radius = 5.dp.toPx(),
                center = Offset(center.x, center.y - outerRadius)
            )
            drawCircle(
                color = RuteBlue,
                radius = 4.dp.toPx(),
                center = Offset(center.x + innerRadius, center.y)
            )
        }

        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 12.dp,
            modifier = Modifier.size(128.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(R.drawable.logo_rutex),
                    contentDescription = stringResource(R.string.logo_desc),
                    modifier = Modifier.size(85.dp)
                )
            }
        }
    }
}

@Composable
private fun SplashBrand() {
    val appName = stringResource(R.string.app_name)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                // Huruf terakhir ("X") diberi warna oranye sesuai desain
                text = buildAnnotatedString {
                    append(appName.dropLast(1))
                    withStyle(SpanStyle(color = RuteOrange)) {
                        append(appName.takeLast(1))
                    }
                },
                fontSize = 36.sp,
                lineHeight = 44.sp,
                fontWeight = FontWeight.ExtraBold,
                color = RuteNavy,
                letterSpacing = (-0.9).sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.ai_label),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = RuteBlue,
                letterSpacing = 0.65.sp,
                modifier = Modifier
                    .background(RuteBadge, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.splash_tagline),
            fontSize = 18.sp,
            lineHeight = 26.sp,
            fontWeight = FontWeight.Medium,
            color = RuteBrown
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .shadow(elevation = 1.dp, shape = CircleShape)
                .background(RuteInput, CircleShape)
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.AutoAwesome,
                contentDescription = null,
                tint = RuteOrange,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.splash_pill),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = RuteSlate,
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Composable
private fun SplashBottomSection(progress: Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 320.dp)
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape)
                .background(RuteBadge)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(RuteOrange, RuteOrangeDark, RuteBlue)
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(
                color = RuteOrange,
                strokeWidth = 2.dp,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.splash_loading),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = RuteBrown
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.VerifiedUser,
                contentDescription = null,
                tint = RuteBlue,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.splash_trust),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = RuteSlate,
                letterSpacing = 0.24.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.splash_version),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = RuteBrownMuted,
            letterSpacing = 0.24.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    RuteXTheme {
        SplashContent(progress = 0.33f)
    }
}
