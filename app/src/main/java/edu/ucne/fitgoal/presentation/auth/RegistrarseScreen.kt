package edu.ucne.fitgoal.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.captionBarPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.fitgoal.R
import edu.ucne.fitgoal.presentation.components.BackGroundImage
import edu.ucne.fitgoal.presentation.components.PassWordTextField
import edu.ucne.fitgoal.presentation.components.TextFielComponent
import edu.ucne.fitgoal.ui.theme.DarkGreen

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistrarseScreen(
    goLogin: () -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()
    val onEvent = authViewModel::onEvent

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BackGroundImage(id = R.drawable.registrarse_background)
        LazyColumn(
            modifier = Modifier.captionBarPadding().systemBarsPadding()
                .fillMaxWidth()
                .padding(top = 90.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp),
                    contentScale = ContentScale.Crop
                )
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Nombre",
                        modifier = Modifier.align(Alignment.Start)
                            .padding(horizontal = 20.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    TextFielComponent(
                        "Delfry",uiState.nombre,
                        KeyboardType.Text,
                        ImeAction.Next,
                        uiState.nombreError != "",
                        uiState.nombreError
                    ){
                        onEvent(AuthEvent.NombreChanged(it))
                    }
                    Text(
                        text = "Apellidos",
                        modifier = Modifier.align(Alignment.Start)
                            .padding(horizontal = 20.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    TextFielComponent(
                        "Paulino Ortega",
                        uiState.apellido,
                        KeyboardType.Text,
                        ImeAction.Next,
                        uiState.apellidoError != "",
                        uiState.apellidoError
                    ){
                        onEvent(AuthEvent.ApellidoChanged(it))
                    }
                    Text(
                        text = "Correo",
                        modifier = Modifier.align(Alignment.Start)
                            .padding(horizontal = 20.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    TextFielComponent("ejemplo@gmail.com",
                        uiState.email,
                        KeyboardType.Text,
                        ImeAction.Next,
                        uiState.emailError != "",
                        uiState.emailError
                    ){
                        onEvent(AuthEvent.EmailChanged(it))
                    }
                    Text(
                        text = "Contraseña",
                        modifier = Modifier.align(Alignment.Start)
                            .padding(horizontal = 20.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    PassWordTextField(
                        "********",
                        uiState.password,
                        uiState.canSeePassword,
                        uiState.keyboardType,
                        ImeAction.Next,
                        uiState.passwordError != "",
                        uiState.passwordError,
                        { onEvent(AuthEvent.ChangePasswordVisibility) }
                    ){
                        onEvent(AuthEvent.PasswordChanged(it))
                    }
                    Text(
                        text = "Confirmar contraseña",
                        modifier = Modifier.align(Alignment.Start)
                            .padding(horizontal = 20.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    PassWordTextField(
                        "********",
                        uiState.verifyPassword,
                        uiState.canSeePassword,
                        uiState.keyboardType,
                        ImeAction.Done,
                        uiState.verifyPasswordError != "",
                        uiState.verifyPasswordError,
                        { onEvent(AuthEvent.ChangePasswordVisibility) }
                    ){
                        onEvent(AuthEvent.VerifyPasswordChanged(it))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { onEvent(AuthEvent.SignUp(goLogin))},
                        colors = ButtonDefaults.buttonColors(DarkGreen),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 70.dp, vertical = 5.dp),
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
}