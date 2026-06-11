package com.example.projectpamt.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.projectpamt.R
import com.example.projectpamt.ui.components.AuthForm
import com.example.projectpamt.ui.theme.ProjectPAMTTheme
import com.example.projectpamt.viewmodel.auth.AuthUiState
import com.example.projectpamt.viewmodel.auth.AuthViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier,
    authViewModel: AuthViewModel,
    navController: NavController,
) {
    val email by authViewModel.email.collectAsStateWithLifecycle()
    val password by authViewModel.password.collectAsStateWithLifecycle()
    val fullname by authViewModel.fullname.collectAsStateWithLifecycle()
    val phone by authViewModel.phone.collectAsStateWithLifecycle()
    val authUiState by authViewModel.uiState.collectAsStateWithLifecycle()

    RegisterContent(
        modifier = modifier,
        email = email,
        password = password,
        fullname = fullname,
        phone = phone,
        uiState = authUiState,
        onEmailChange = authViewModel::onEmailChange,
        onPasswordChange = authViewModel::onPasswordChange,
        onNameChange = authViewModel::onNameChange,
        onPhoneChange = authViewModel::onPhoneChange,
        onRegisterClick = {
            authViewModel.register()
        },
        onNavigateToLogin = {
            navController.popBackStack()
        },
    )
}

@Composable
private fun RegisterContent(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    fullname: String,
    phone: String,
    uiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
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
                    color = Color(0xFF00482F),
                    letterSpacing = (-0.55).sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Mulai kelola keuangan bisnis Anda dengan\nlebih mudah",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0x94000000),
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
                    value = fullname,
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00754A)),
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Sudah punya akun? ")
                        withLink(
                            LinkAnnotation.Clickable(
                                tag = "LOGIN",
                                linkInteractionListener = { onNavigateToLogin() }
                            )
                        ) {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00754A)
                                )
                            ) {
                                append("Masuk")
                            }
                        }
                    },
                    fontWeight = FontWeight.Medium,
                    color = Color(0x94000000),
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterScreenPreview() {
    ProjectPAMTTheme {
        Scaffold(Modifier.fillMaxSize()) { innerPadding ->
            RegisterContent(
                email = "",
                password = "",
                fullname = "",
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
