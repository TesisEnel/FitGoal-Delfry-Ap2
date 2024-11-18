package edu.ucne.fitgoal.presentation.auth

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalConfiguration
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
import edu.ucne.fitgoal.presentation.components.TextFielComponent
import edu.ucne.fitgoal.ui.theme.DarkGreen

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OlvidarContraseñaScreen(
    goLogin: () -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.smallestScreenWidthDp >= 600
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()
    val onEvent = authViewModel::onEvent

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BackGroundImage(id = R.drawable.verifyandsendemail_background)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
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
                    text = "Ingresa tu correo para restablecer tu contraseña",
                    modifier = Modifier.align(Alignment.Start)
                        .padding(horizontal = 20.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                TextFielComponent("ejemplo@email.com",uiState.email,
                    KeyboardType.Text, ImeAction.Done,uiState.email != "", uiState.emailError){
                    onEvent(AuthEvent.EmailChanged(it))
                }

                Button(
                    onClick = {
                        onEvent(AuthEvent.ResetPassword(uiState.email){
                            goLogin()
                        })
                    },
                    colors = ButtonDefaults.buttonColors(DarkGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 70.dp),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(12.dp),
                    enabled = !uiState.isLoading
                ) {
                    Text(
                        text = "Enviar correo",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}