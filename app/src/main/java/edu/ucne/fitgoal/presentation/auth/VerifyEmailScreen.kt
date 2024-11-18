package edu.ucne.fitgoal.presentation.auth

import android.app.Activity
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.fitgoal.R
import edu.ucne.fitgoal.presentation.components.BackGroundImage
import edu.ucne.fitgoal.ui.theme.DarkGreen
import edu.ucne.fitgoal.ui.theme.ExDarkGreen
import edu.ucne.fitgoal.ui.theme.LightBlue

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VerifyEmailScreen(
    goLogin: () -> Unit = {},
    goHome: () -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.smallestScreenWidthDp >= 600
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()
    val onEvent = authViewModel::onEvent
    val isVerified by authViewModel.emailVerified.observeAsState()
    val activity = LocalContext.current as? Activity

    LaunchedEffect(key1 = isVerified) {
        if (isVerified == true) {
            goHome()
        }
        authViewModel.onEvent(AuthEvent.StartEmailVerification)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BackGroundImage(id = R.drawable.verifyandsendemail_background)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                if (isTablet || !isLandscape) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    text = "PARA CONTINUAR\nVERIFICA TU CORREO",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                CircularProgressIndicator(
                    modifier = Modifier
                        .size(150.dp)
                        .padding(20.dp),
                    color = DarkGreen,
                )

                Text(
                    text = "NO RECIBÍ EL CORREO DE VERIFICACIÓN",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = LightBlue
                )

                Button(
                    onClick = { onEvent(AuthEvent.SendEmailVerification) },
                    colors = ButtonDefaults.buttonColors(DarkGreen),
                    modifier = Modifier
                        .padding(horizontal = 70.dp)
                        .fillMaxWidth(0.6f),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(vertical = 10.dp),
                    enabled = uiState.isButtonEnabled
                ) {
                    Text(
                        text = "reenviar correo",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                if(!uiState.isButtonEnabled){
                    Text(
                        text = "Tiempo restante: ${uiState.tiempo}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 60.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { activity?.let { onEvent(AuthEvent.Logout(it, goLogin)) } },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logout),
                    contentDescription = null,
                    tint = ExDarkGreen,
                    modifier = Modifier.size(50.dp)
                )
            }
            Text(
                text = "Cerrar Sesión",
                color = ExDarkGreen,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(top = 3.dp)
            )
        }
    }
}