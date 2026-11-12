package com.ibaiqzzaaziza.rutex.screen

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LockReset
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.MarkEmailRead
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.component.AuthTopBar
import com.ibaiqzzaaziza.rutex.component.RutePrimaryButton
import com.ibaiqzzaaziza.rutex.component.RuteTextField
import com.ibaiqzzaaziza.rutex.network.ApiStatus
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBackground
import com.ibaiqzzaaziza.rutex.ui.theme.RuteError
import com.ibaiqzzaaziza.rutex.ui.theme.RuteGreen
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrangeDark
import com.ibaiqzzaaziza.rutex.ui.theme.RutePeach
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate
import com.ibaiqzzaaziza.rutex.ui.theme.RuteXTheme

@Composable
fun LupaPasswordScreen(navController: NavHostController) {
    val viewModel: LupaPasswordViewModel = viewModel()
    val status by viewModel.status.collectAsState()
    val errorMessage by viewModel.errorMessage

    var email by rememberSaveable { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AuthTopBar(
                title = stringResource(R.string.lupa_password_topbar),
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = RuteBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val terkirim = status == ApiStatus.SUCCESS
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(if (terkirim) RuteGreen.copy(alpha = 0.15f) else RutePeach, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (terkirim) Icons.Outlined.MarkEmailRead else Icons.Outlined.LockReset,
                    contentDescription = null,
                    tint = if (terkirim) RuteGreen else RuteOrangeDark,
                    modifier = Modifier.size(34.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(if (terkirim) R.string.lupa_password_terkirim_title else R.string.lupa_password_title),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = RuteNavy,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (terkirim) {
                    stringResource(R.string.lupa_password_terkirim_desc, email.trim())
                } else {
                    stringResource(R.string.lupa_password_desc)
                },
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = RuteSlate,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (terkirim) {
                        RutePrimaryButton(
                            text = stringResource(R.string.kembali_ke_login),
                            onClick = { navController.popBackStack() }
                        )
                        TextButton(
                            onClick = { viewModel.ulangi() },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = stringResource(R.string.kirim_ulang), color = RuteOrange)
                        }
                    } else {
                        RuteTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                emailError = false
                            },
                            label = stringResource(R.string.label_email_login),
                            placeholder = stringResource(R.string.placeholder_email),
                            leadingIcon = Icons.Outlined.MailOutline,
                            errorMessage = if (emailError) stringResource(R.string.error_email_invalid) else null,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        )
                        if (status == ApiStatus.FAILED) {
                            Text(
                                text = errorMessage ?: stringResource(R.string.error_koneksi),
                                fontSize = 13.sp,
                                color = RuteError
                            )
                        }
                        RutePrimaryButton(
                            text = stringResource(R.string.kirim_tautan_reset),
                            isLoading = status == ApiStatus.LOADING,
                            onClick = {
                                emailError = !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
                                if (!emailError) viewModel.kirimTautan(email)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LupaPasswordScreenPreview() {
    RuteXTheme {
        LupaPasswordScreen(rememberNavController())
    }
}
