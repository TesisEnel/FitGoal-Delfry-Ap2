package edu.ucne.fitgoal.presentation.auth

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.fitgoal.R
import edu.ucne.fitgoal.presentation.components.BackGroundImage
import edu.ucne.fitgoal.presentation.components.GoogleLoginButton
import edu.ucne.fitgoal.presentation.components.LoadingIndicator
import edu.ucne.fitgoal.presentation.components.ModalError
import edu.ucne.fitgoal.presentation.components.PassWordTextField
import edu.ucne.fitgoal.presentation.components.TextFielComponent
import edu.ucne.fitgoal.ui.theme.DarkGreen
import edu.ucne.fitgoal.ui.theme.ExDarkGreen
import edu.ucne.fitgoal.ui.theme.LightBlue

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreen(
    goOlvidasteContrasena: () -> Unit = {},
    goRegistro: () -> Unit = {},
    goHome: () -> Unit = {},
    goVerify: () -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.smallestScreenWidthDp >= 600
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val paddingTop = if (configuration.smallestScreenWidthDp >= 600) 185.dp else 150.dp
    val activity = LocalContext.current as? Activity
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()
    val onEvent = authViewModel::onEvent

    LaunchedEffect(uiState.isAuthorized) {
        if (uiState.isAuthorized && uiState.isEmailVerified) {
            goHome()
        }
        if(uiState.isAuthorized && !uiState.isEmailVerified){
            goVerify()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BackGroundImage(id = R.drawable.login_background)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingTop),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isTablet || !isLandscape) {
                        Image(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = null,
                            modifier = Modifier
                                .size(200.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Correo",
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(horizontal = 20.dp),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        TextFielComponent(
                            "ejemplo@gmail.com",
                            uiState.email,
                            KeyboardType.Text,
                            ImeAction.Next,
                            uiState.emailError != "",
                            uiState.emailError
                        ) {
                            onEvent(AuthEvent.EmailChanged(it))
                        }
                        Text(
                            text = "Contraseña",
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(horizontal = 20.dp),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        PassWordTextField(
                            "********",
                            uiState.password,
                            uiState.canSeePassword,
                            uiState.keyboardType,
                            ImeAction.Done,
                            uiState.passwordError != "",
                            uiState.passwordError,
                            { onEvent(AuthEvent.ChangePasswordVisibility) }
                        ) {
                            onEvent(AuthEvent.PasswordChanged(it))
                        }

                        Text(
                            text = "Olvidaste tu contraseña?",
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(horizontal = 20.dp)
                                .clickable {
                                    goOlvidasteContrasena()
                                },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = LightBlue,
                            textDecoration = TextDecoration.Underline
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                onEvent(AuthEvent.SignIn)
                            },
                            colors = ButtonDefaults.buttonColors(DarkGreen),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(12.dp),
                            enabled = !uiState.isLoading
                        ) {
                            Text(
                                text = "INICIAR SESIÓN",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        GoogleLoginButton(!uiState.isLoading) {
                            activity?.let { onEvent(AuthEvent.SignInWithGoogle(it, goHome)) }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { goRegistro() },
                            colors = ButtonDefaults.buttonColors(ExDarkGreen),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 70.dp),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(12.dp),
                            enabled = !uiState.isLoading
                        ) {
                            Text(
                                text = "REGISTRARSE",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
        if (uiState.isLoading) {
            LoadingIndicator()
        }
        if(uiState.isModalErrorVisible){
            ModalError(
                error = uiState.error,
                onclick = {onEvent(AuthEvent.CloseErrorModal)}
            )
        }
    }
}