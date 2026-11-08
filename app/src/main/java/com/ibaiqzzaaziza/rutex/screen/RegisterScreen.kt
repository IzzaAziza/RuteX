package com.ibaiqzzaaziza.rutex.screen

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Sms
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBackground
import com.ibaiqzzaaziza.rutex.ui.theme.RuteBlue
import com.ibaiqzzaaziza.rutex.ui.theme.RuteChip
import com.ibaiqzzaaziza.rutex.ui.theme.RuteDivider
import com.ibaiqzzaaziza.rutex.ui.theme.RuteInput
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate
import com.ibaiqzzaaziza.rutex.ui.theme.RuteXTheme

private const val MIN_PASSWORD_LENGTH = 8
private const val MIN_PHONE_LENGTH = 9
private const val MAX_PHONE_LENGTH = 13

@Composable
fun RegisterScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            AuthTopBar(
                title = stringResource(R.string.register_topbar_title),
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = RuteBackground
    ) { innerPadding ->
        RegisterContent(
            modifier = Modifier.padding(innerPadding),
            onRegisterSuccess = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            },
            onLoginClick = { navController.popBackStack() }
        )
    }
}

@Composable
fun RegisterContent(
    modifier: Modifier = Modifier,
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    val context = LocalContext.current
    val comingSoon = stringResource(R.string.feature_coming_soon)
    val termsError = stringResource(R.string.error_terms)
    val successMessage = stringResource(R.string.register_success)

    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var agreeTerms by rememberSaveable { mutableStateOf(false) }

    var fullNameError by rememberSaveable { mutableStateOf(false) }
    var emailError by rememberSaveable { mutableStateOf(false) }
    var phoneError by rememberSaveable { mutableStateOf(false) }
    var passwordError by rememberSaveable { mutableStateOf(false) }
    var confirmError by rememberSaveable { mutableStateOf(false) }

    val passwordMatches = confirmPassword.isNotEmpty() && confirmPassword == password

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RuteLogoWithText(height = 40.dp)

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.register_header_title),
            fontSize = 24.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.SemiBold,
            color = RuteNavy,
            letterSpacing = (-0.6).sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.register_header_subtitle),
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = RuteSlate,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 320.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RuteTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = stringResource(R.string.label_fullname),
                    placeholder = stringResource(R.string.placeholder_fullname),
                    leadingIcon = Icons.Outlined.Person,
                    errorMessage = if (fullNameError) stringResource(R.string.error_fullname_empty) else null
                )

                RuteTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = stringResource(R.string.label_email),
                    placeholder = stringResource(R.string.placeholder_email),
                    leadingIcon = Icons.Outlined.MailOutline,
                    errorMessage = if (emailError) stringResource(R.string.error_email_invalid) else null,
                    keyboardType = KeyboardType.Email
                )

                RuteTextField(
                    value = phone,
                    // Nomor WhatsApp hanya boleh berisi angka
                    onValueChange = { input -> phone = input.filter { it.isDigit() } },
                    label = stringResource(R.string.label_whatsapp),
                    labelTrailing = stringResource(R.string.label_optional),
                    placeholder = stringResource(R.string.placeholder_phone),
                    leadingIcon = Icons.Outlined.Sms,
                    leadingExtra = { PhonePrefix() },
                    errorMessage = if (phoneError) stringResource(R.string.error_phone_invalid) else null,
                    keyboardType = KeyboardType.Phone
                )

                RuteTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = stringResource(R.string.label_password),
                    placeholder = stringResource(R.string.placeholder_password),
                    leadingIcon = Icons.Outlined.Lock,
                    errorMessage = if (passwordError) stringResource(R.string.error_password_min) else null,
                    isPassword = true,
                    keyboardType = KeyboardType.Password
                )

                RuteTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = stringResource(R.string.label_confirm_password),
                    placeholder = stringResource(R.string.placeholder_confirm_password),
                    leadingIcon = Icons.Outlined.VerifiedUser,
                    errorMessage = if (confirmError) stringResource(R.string.error_password_mismatch) else null,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    trailingIcon = if (passwordMatches) {
                        {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = null,
                                tint = RuteOrange
                            )
                        }
                    } else {
                        null
                    }
                )

                TermsCheckbox(
                    checked = agreeTerms,
                    onCheckedChange = { agreeTerms = it },
                    onLinkClick = { Toast.makeText(context, comingSoon, Toast.LENGTH_SHORT).show() }
                )

                RutePrimaryButton(
                    text = stringResource(R.string.btn_create_account),
                    onClick = {
                        // Sanity check sebelum akun dibuat
                        fullNameError = fullName.isBlank()
                        emailError = !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
                        phoneError = phone.isNotEmpty() &&
                                phone.length !in MIN_PHONE_LENGTH..MAX_PHONE_LENGTH
                        passwordError = password.length < MIN_PASSWORD_LENGTH
                        confirmError = confirmPassword != password

                        val formValid = !fullNameError && !emailError && !phoneError &&
                                !passwordError && !confirmError
                        if (formValid && !agreeTerms) {
                            Toast.makeText(context, termsError, Toast.LENGTH_SHORT).show()
                        } else if (formValid) {
                            Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                            onRegisterSuccess()
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        DividerWithText(
            text = stringResource(R.string.divider_or),
            lineColor = RuteDivider
        )

        Spacer(modifier = Modifier.height(24.dp))
        GoogleButton(
            text = stringResource(R.string.btn_google),
            onClick = { Toast.makeText(context, comingSoon, Toast.LENGTH_SHORT).show() },
            containerColor = Color.White,
            elevation = 1.dp
        )

        Spacer(modifier = Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.txt_have_account).trim(),
                fontSize = 14.sp,
                color = RuteSlate
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.link_login),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = RuteOrange,
                modifier = Modifier.clickable(onClick = onLoginClick)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .widthIn(max = 320.dp)
                .fillMaxWidth()
                .background(RuteInput, CircleShape)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Verified,
                contentDescription = null,
                tint = RuteBlue,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.footer_badge_text),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium,
                color = RuteSlate,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PhonePrefix() {
    Text(
        text = stringResource(R.string.prefix_phone),
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = RuteNavy,
        modifier = Modifier
            .padding(start = 10.dp)
            .background(RuteChip, RoundedCornerShape(4.dp))
            .padding(start = 6.dp, end = 8.dp, top = 2.dp, bottom = 2.dp)
    )
}

@Composable
private fun TermsCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onLinkClick: () -> Unit
) {
    val linkStyle = TextLinkStyles(
        style = SpanStyle(color = RuteBlue, fontWeight = FontWeight.SemiBold)
    )
    val prefix = stringResource(R.string.terms_prefix)
    val terms = stringResource(R.string.terms_link)
    val middle = stringResource(R.string.terms_middle)
    val privacy = stringResource(R.string.privacy_link)
    val suffix = stringResource(R.string.terms_suffix)

    val termsText = buildAnnotatedString {
        append("$prefix ")
        withLink(LinkAnnotation.Clickable(tag = "terms", styles = linkStyle) { onLinkClick() }) {
            append(terms)
        }
        append(" $middle ")
        withLink(LinkAnnotation.Clickable(tag = "privacy", styles = linkStyle) { onLinkClick() }) {
            append(privacy)
        }
        append(" $suffix")
    }

    Row(verticalAlignment = Alignment.Top) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = RuteOrange,
                uncheckedColor = RuteSlate
            )
        )
        Text(
            text = termsText,
            fontSize = 14.sp,
            lineHeight = 19.sp,
            color = RuteSlate,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RuteXTheme {
        RegisterScreen(rememberNavController())
    }
}
