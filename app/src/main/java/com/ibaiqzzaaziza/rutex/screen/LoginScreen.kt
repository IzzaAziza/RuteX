package com.ibaiqzzaaziza.rutex.screen

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.ibaiqzzaaziza.rutex.component.DividerWithText
import com.ibaiqzzaaziza.rutex.component.GoogleButton
import com.ibaiqzzaaziza.rutex.component.RuteLogoWithText
import com.ibaiqzzaaziza.rutex.component.RutePrimaryButton
import com.ibaiqzzaaziza.rutex.component.RuteTextField
import com.ibaiqzzaaziza.rutex.navigation.Screen
import com.ibaiqzzaaziza.rutex.network.ApiStatus
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBackground
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBlue
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate
import com.ibaiqzzaaziza.rutex.ui.theme.RuteXTheme
import com.ibaiqzzaaziza.rutex.util.ViewModelFactory

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: AuthViewModel = viewModel(factory = factory)
    val status by viewModel.status.collectAsState()
    val errorMessage by viewModel.errorMessage

    val pesanSukses = stringResource(R.string.login_success)
    val keBeranda = {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Login.route) { inclusive = true }
        }
    }
    val googleSignIn = rememberGoogleSignIn(onSuccess = keBeranda)
    val pesanGagalKoneksi = stringResource(R.string.error_koneksi)

    LaunchedEffect(status) {
        when (status) {
            ApiStatus.SUCCESS -> {
                Toast.makeText(context, pesanSukses, Toast.LENGTH_SHORT).show()
                viewModel.clearStatus()
                keBeranda()
            }
            ApiStatus.FAILED -> {
                Toast.makeText(context, errorMessage ?: pesanGagalKoneksi, Toast.LENGTH_LONG).show()
                viewModel.clearStatus()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = { AuthTopBar(title = stringResource(R.string.login_topbar_title)) },
        containerColor = RuteBackground
    ) { innerPadding ->
        LoginContent(
            modifier = Modifier.padding(innerPadding),
            isLoading = status == ApiStatus.LOADING,
            isGoogleLoading = googleSignIn.isLoading,
            onGoogleClick = googleSignIn.signIn,
            onLoginClick = { email, password, rememberMe ->
                viewModel.login(email, password, rememberMe)
            },
            onRegisterClick = { navController.navigate(Screen.Register.route) },
            onLupaPasswordClick = { navController.navigate(Screen.LupaPassword.route) }
        )
    }
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isGoogleLoading: Boolean = false,
    onGoogleClick: () -> Unit = {},
    onLoginClick: (email: String, password: String, rememberMe: Boolean) -> Unit,
    onRegisterClick: () -> Unit,
    onLupaPasswordClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val comingSoon = stringResource(R.string.feature_coming_soon)

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var rememberMe by rememberSaveable { mutableStateOf(true) }
    var emailError by rememberSaveable { mutableStateOf(false) }
    var passwordError by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RuteLogoWithText(height = 60.dp)

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.login_title),
            fontSize = 24.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Bold,
            color = RuteNavy,
            letterSpacing = (-0.6).sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.login_subtitle),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = RuteSlate,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                RuteTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = stringResource(R.string.label_email_login),
                    placeholder = stringResource(R.string.placeholder_email),
                    leadingIcon = Icons.Outlined.MailOutline,
                    errorMessage = if (emailError) stringResource(R.string.error_email_invalid) else null,
                    keyboardType = KeyboardType.Email,
                    cornerRadius = 8.dp,
                    labelFontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(16.dp))
                RuteTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = stringResource(R.string.label_password),
                    placeholder = stringResource(R.string.placeholder_password_login),
                    leadingIcon = Icons.Outlined.Lock,
                    errorMessage = if (passwordError) stringResource(R.string.error_password_empty) else null,
                    isPassword = true,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    cornerRadius = 8.dp,
                    labelFontSize = 14.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.clickable { rememberMe = !rememberMe },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = RuteOrange,
                                uncheckedColor = RuteSlate
                            )
                        )
                        Text(
                            text = stringResource(R.string.remember_me),
                            fontSize = 14.sp,
                            color = RuteSlate
                        )
                    }
                    TextButton(onClick = onLupaPasswordClick) {
                        Text(
                            text = stringResource(R.string.forgot_password),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RuteBlue
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                RutePrimaryButton(
                    text = stringResource(R.string.btn_login),
                    isLoading = isLoading,
                    onClick = {
                        // Sanity check sebelum dikirim ke server
                        emailError = !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
                        passwordError = password.isBlank()
                        if (!emailError && !passwordError) {
                            onLoginClick(email, password, rememberMe)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
                DividerWithText(text = stringResource(R.string.login_divider))

                Spacer(modifier = Modifier.height(24.dp))
                GoogleButton(
                    text = stringResource(R.string.btn_google_login),
                    onClick = onGoogleClick,
                    isLoading = isGoogleLoading
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.txt_no_account),
                fontSize = 14.sp,
                color = RuteSlate
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.link_register),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = RuteOrange,
                modifier = Modifier.clickable(onClick = onRegisterClick)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    RuteXTheme {
        LoginScreen(rememberNavController())
    }
}
