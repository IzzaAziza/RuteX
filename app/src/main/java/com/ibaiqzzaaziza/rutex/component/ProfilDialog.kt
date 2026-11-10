package com.ibaiqzzaaziza.rutex.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ibaiqzzaaziza.rutex.R
import com.ibaiqzzaaziza.rutex.model.User
import com.ibaiqzzaaziza.rutex.ui.theme.RuteDivider
import com.ibaiqzzaaziza.rutex.ui.theme.RuteError
import com.ibaiqzzaaziza.rutex.ui.theme.RuteNavy
import com.ibaiqzzaaziza.rutex.ui.theme.RuteSlate
import com.ibaiqzzaaziza.rutex.ui.theme.RuteXTheme

/**
 * Dialog profil pengguna (Modul 14): foto, nama, dan email,
 * dengan tombol Tutup dan Keluar (logout).
 */
@Composable
fun ProfilDialog(
    user: User,
    isLoggingOut: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    Dialog(onDismissRequest = { if (!isLoggingOut) onDismissRequest() }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserAvatar(photoUrl = user.photoUrl, size = 96.dp)

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    // Akun email/password yang belum punya nama tetap menampilkan teks pengganti
                    text = user.name.ifBlank { stringResource(R.string.profil_tanpa_nama) },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = RuteNavy,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = user.email,
                    fontSize = 14.sp,
                    color = RuteSlate,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismissRequest,
                        enabled = !isLoggingOut,
                        border = BorderStroke(1.dp, RuteDivider),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = stringResource(R.string.tombol_tutup), color = RuteNavy)
                    }
                    OutlinedButton(
                        onClick = onConfirmation,
                        enabled = !isLoggingOut,
                        border = BorderStroke(1.dp, RuteError),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RuteError),
                        modifier = Modifier.weight(1f)
                    ) {
                        if (isLoggingOut) {
                            CircularProgressIndicator(
                                color = RuteError,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Text(text = stringResource(R.string.tombol_logout))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilDialogPreview() {
    RuteXTheme {
        ProfilDialog(
            user = User(name = "Izza Aziza", email = "izza@email.com"),
            isLoggingOut = false,
            onDismissRequest = {},
            onConfirmation = {}
        )
    }
}
