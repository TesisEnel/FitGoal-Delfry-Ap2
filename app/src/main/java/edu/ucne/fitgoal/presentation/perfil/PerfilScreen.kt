package edu.ucne.fitgoal.presentation.perfil

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import edu.ucne.fitgoal.presentation.components.LoadingIndicator
import edu.ucne.fitgoal.presentation.components.ModalError
import edu.ucne.fitgoal.presentation.navigation.Screen

@Composable
fun PerfilScreen(
    navController: NavController,
    perfilViewModel: PerfilViewModel = hiltViewModel()
) {
    val uiState = perfilViewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (uiState.value.isLoading) {
            LoadingIndicator()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.Gray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.value.photoUrl != null) {
                        AsyncImage(
                            model = uiState.value.photoUrl,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.Gray, CircleShape)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Foto de perfil",
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = uiState.value.nombre.ifEmpty { "Nombre no disponible" },
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )

                Text(
                    text = uiState.value.correo.ifEmpty { "Correo no disponible" },
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(30.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.elevatedCardElevation()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Edad: ${uiState.value.edad} años",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 20.sp
                        )
                        Text(
                            "Altura: ${uiState.value.altura} m",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 20.sp
                        )
                        Text(
                            "Peso Actual: ${uiState.value.pesoActual} lb",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 20.sp
                        )
                        Text(
                            "Peso Ideal: ${uiState.value.pesoIdeal} lb",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 20.sp
                        )
                        Text(
                            "Agua Diaria: ${uiState.value.aguaDiaria} L",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                ProfileButton(
                    text = "Calculadora",
                    icon = Icons.Default.Calculate,
                    onClick = { perfilViewModel.onEvent(PerfilEvent.NavigateToCalculadora) }
                )

                Spacer(modifier = Modifier.height(14.dp))

                ProfileButton(
                    text = "Tips",
                    icon = Icons.Default.Lightbulb,
                    onClick = { perfilViewModel.onEvent(PerfilEvent.NavigateToTips) }
                )

                Spacer(modifier = Modifier.height(14.dp))

                ProfileButton(
                    text = "Ayuda",
                    icon = Icons.AutoMirrored.Filled.Help,
                    onClick = { perfilViewModel.onEvent(PerfilEvent.NavigateToAyuda) }
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedButton(
                    onClick = {
                        perfilViewModel.onEvent(PerfilEvent.Logout)
                        navController.navigate(Screen.AuthNavHostScreen)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Red
                    ),
                    border = BorderStroke(1.dp, Color.Red)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = Color.Red
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Cerrar sesión",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                    }
                }
            }
        }

        if (uiState.value.isModalErrorVisible) {
            ModalError(
                error = uiState.value.error,
                onclick = { perfilViewModel.onEvent(PerfilEvent.CloseErrorModal) }
            )
        }
    }
}

@Composable
fun ProfileButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF358F5E))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "$text Icon",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, color = Color.White, fontSize = 18.sp)
        }
    }
}
