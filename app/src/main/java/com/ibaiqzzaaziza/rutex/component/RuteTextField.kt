package com.ibaiqzzaaziza.rutex.component

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.ui.theme.RuteError
import com.ibaiqzzaaziza.rutex.ui.theme.RuteInput
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteOrange
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate

/**
 * Text field bergaya RuteX: label di atas, latar biru muda, tanpa garis bawah.
 * Jika [errorMessage] tidak null, field berwarna merah dan pesan error tampil di bawahnya.
 */
@Composable
fun RuteTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    labelTrailing: String? = null,
    errorMessage: String? = null,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    leadingExtra: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    cornerRadius: Dp = 12.dp,
    labelFontSize: TextUnit = 13.sp
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isError = errorMessage != null
    val shape = RoundedCornerShape(cornerRadius)

    val borderColor = when {
        isError -> RuteError
        isFocused -> RuteOrange
        else -> Color.Transparent
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = labelFontSize,
                fontWeight = FontWeight.SemiBold,
                color = RuteNavy,
                letterSpacing = 0.14.sp
            )
            if (labelTrailing != null) {
                Text(
                    text = labelTrailing,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = RuteSlate,
                    letterSpacing = 0.24.sp
                )
            }
        }

        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = borderColor, shape = shape),
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 14.sp,
                    color = RuteSlate.copy(alpha = 0.6f)
                )
            },
            leadingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = if (isError) RuteError else RuteSlate,
                        modifier = Modifier.size(20.dp)
                    )
                    leadingExtra?.invoke()
                }
            },
            trailingIcon = when {
                isPassword -> {
                    {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) {
                                    Icons.Outlined.VisibilityOff
                                } else {
                                    Icons.Outlined.Visibility
                                },
                                contentDescription = stringResource(
                                    if (passwordVisible) R.string.hide_password else R.string.show_password
                                ),
                                tint = RuteSlate
                            )
                        }
                    }
                }
                isError -> {
                    {
                        Icon(
                            imageVector = Icons.Outlined.ErrorOutline,
                            contentDescription = null,
                            tint = RuteError
                        )
                    }
                }
                else -> trailingIcon
            },
            supportingText = if (errorMessage != null) {
                { Text(text = errorMessage) }
            } else {
                null
            },
            isError = isError,
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            singleLine = true,
            shape = shape,
            textStyle = TextStyle(fontSize = 14.sp),
            interactionSource = interactionSource,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = RuteInput,
                unfocusedContainerColor = RuteInput,
                errorContainerColor = RuteInput,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedTextColor = RuteNavy,
                unfocusedTextColor = RuteNavy,
                errorTextColor = RuteNavy,
                cursorColor = RuteOrange,
                errorSupportingTextColor = RuteError
            )
        )
    }
}
