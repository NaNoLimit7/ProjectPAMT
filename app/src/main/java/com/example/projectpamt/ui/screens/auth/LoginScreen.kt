package com.example.projectpamt.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
import com.example.projectpamt.ui.components.AppTextField
import com.example.projectpamt.ui.utils.ValidationUtils
import com.example.projectpamt.ui.navigation.Register
import com.example.projectpamt.ui.theme.ProjectPAMTTheme
import com.example.projectpamt.viewmodel.auth.AuthUiState
import com.example.projectpamt.viewmodel.auth.AuthViewModel

@Composable
fun LoginScreen(
    modifier: Modifier,
    authViewModel: AuthViewModel,
    navController: NavController,
) {
    val email by authViewModel.email.collectAsStateWithLifecycle()
    val password by authViewModel.password.collectAsStateWithLifecycle()
    val authUiState by authViewModel.uiState.collectAsStateWithLifecycle()

    LoginContent(
        modifier = modifier,
        email = email,
        password = password,
        uiState = authUiState,
        onEmailChange = authViewModel::onEmailChange,
        onPasswordChange = authViewModel::onPasswordChange,
        onLoginClick = {
            authViewModel.login()
        },
        onNavigateToRegister = {
            navController.navigate(Register)
        }
    )
}

@Composable
private fun LoginContent(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    uiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    val isButtonEnabled =
        email.isNotBlank() && password.isNotBlank() && emailError == null && passwordError == null && uiState !is AuthUiState.Loading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_baru),
                contentDescription = "App logo",
                modifier = Modifier
                    .size(150.dp)
            )
            Text(
                text = "GriyaArta",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF006241),
                textAlign = TextAlign.Center,
                letterSpacing = (-0.9).sp
            )
            Text(
                text = "Selamat datang kembali",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0x94000000),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 48.dp),
                letterSpacing = (-0.16).sp
            )
            AppTextField(
                label = "Email",
                value = email,
                onValueChange = {
                    onEmailChange(it)
                    emailError = ValidationUtils.validateEmail(it).errorMessage
                },
                isError = emailError != null,
                errorMessage = emailError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                label = "Kata Sandi",
                value = password,
                onValueChange = {
                    onPasswordChange(it)
                    passwordError = ValidationUtils.validatePassword(it).errorMessage
                },
                isError = passwordError != null,
                errorMessage = passwordError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        val emailVal = ValidationUtils.validateEmail(email)
                        val passwordVal = ValidationUtils.validatePassword(password)
                        emailError = emailVal.errorMessage
                        passwordError = passwordVal.errorMessage
                        if (emailVal.isValid && passwordVal.isValid) {
                            onLoginClick()
                        }
                    }
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Lupa kata sandi?",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF00754A),
                    modifier = Modifier.clickable { /* Link to Forgot Password */ }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val emailVal = ValidationUtils.validateEmail(email)
                    val passwordVal = ValidationUtils.validatePassword(password)
                    emailError = emailVal.errorMessage
                    passwordError = passwordVal.errorMessage
                    if (emailVal.isValid && passwordVal.isValid) {
                        onLoginClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(9999.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00754A)),
                enabled = isButtonEnabled
            ) {
                if (uiState is AuthUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Masuk",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }


            if (uiState is AuthUiState.Error) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Belum punya akun? ")
                        withLink(
                            LinkAnnotation.Clickable(
                                tag = "REGISTER",
                                linkInteractionListener = { onNavigateToRegister() }
                            )
                        ) {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00754A)
                                )
                            ) {
                                append("Daftar")
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
private fun LoginScreenPreview() {
    ProjectPAMTTheme {
        Scaffold(Modifier.fillMaxSize()) { innerPadding ->
            LoginContent(
                email = "",
                password = "",
                uiState = AuthUiState.Idle,
                onEmailChange = {},
                onPasswordChange = {},
                onLoginClick = {},
                onNavigateToRegister = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}