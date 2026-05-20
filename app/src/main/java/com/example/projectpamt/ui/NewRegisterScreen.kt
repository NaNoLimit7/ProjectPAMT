package com.example.projectpamt.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectpamt.R
import com.example.projectpamt.ui.theme.ProjectPAMTTheme
import com.example.projectpamt.viewmodel.auth.AuthUiState

@Composable
fun NewRegisterScreen(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    name: String,
    phone: String,
    uiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {

    val backgroundColor = Color(0xFFF2F0EB)
    val brandGreen = Color(0xFF00482F)
    val primaryGreen = Color(0xFF00754A)
    val secondaryTextColor = Color(0x94000000)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, bottom = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_baru),
                    contentDescription = "App logo",
                    modifier = Modifier.size(150.dp)
                )

                Text(
                    text = "Daftar Akun Baru",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = brandGreen,
                    letterSpacing = (-0.55).sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Mulai kelola keuangan bisnis Anda dengan\nlebih mudah",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = secondaryTextColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    letterSpacing = (-0.14).sp
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                AuthForm(
                    label = "Nama Lengkap",
                    value = name,
                    onValueChange = onNameChange,
                )

                AuthForm(
                    label = "Nomor Telepon",
                    value = phone,
                    onValueChange = onPhoneChange,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    )
                )

                AuthForm(
                    value = email,
                    label = "Email",
                    onValueChange = onEmailChange,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                )

                AuthForm(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = "Kata Sandi",
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))


                Button(
                    onClick = onRegisterClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(elevation = 1.dp, shape = RoundedCornerShape(50.dp)),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                    enabled = uiState !is AuthUiState.Loading
                ) {
                    if (uiState is AuthUiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Daftar Sekarang",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            letterSpacing = 0.26.sp
                        )
                    }
                }


                if (uiState is AuthUiState.Error) {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .clickable { onNavigateToLogin() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Sudah punya akun? ",
                    color = secondaryTextColor,
                    letterSpacing = (-0.14).sp
                )
                Text(
                    text = "Masuk",
                    fontWeight = FontWeight.SemiBold,
                    color = brandGreen,
                    letterSpacing = 0.26.sp
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NewRegisterScreenPreview() {
    ProjectPAMTTheme {
        Scaffold(Modifier.fillMaxSize()) { innerPadding ->
            NewRegisterScreen(
                email = "",
                password = "",
                name = "",
                phone = "",
                uiState = AuthUiState.Idle,
                onEmailChange = {},
                onPasswordChange = {},
                onNameChange = {},
                onPhoneChange = {},
                onRegisterClick = {},
                onNavigateToLogin = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
