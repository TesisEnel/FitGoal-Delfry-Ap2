package edu.ucne.fitgoal.presentation.editarPerfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.fitgoal.presentation.components.LoadingIndicator
import edu.ucne.fitgoal.ui.theme.DarkGreen

@Composable
fun EditarPerfilScreen(
    userId: String,
    editarPerfilViewModel: EditarPerfilViewModel = hiltViewModel(),
    onSaveSuccess: () -> Unit,
    onBackToProfile: () -> Unit
) {
    val uiState by editarPerfilViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        editarPerfilViewModel.fetchUserData(userId)
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator()
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Editar Edad y Altura",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        NumberInputField(
                            label = "Edad",
                            value = uiState.edad.toString(),
                            onValueChange = { editarPerfilViewModel.onEvent(EditarPerfilEvent.UpdateEdad(it.toIntOrNull() ?: 0)) }
                        )
                        NumberInputField(
                            label = "Altura (m)",
                            value = uiState.altura.toString(),
                            onValueChange = { editarPerfilViewModel.onEvent(EditarPerfilEvent.UpdateAltura(it.toFloatOrNull() ?: 0f)) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { onBackToProfile() },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkGreen,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(text = "Volver", fontSize = 18.sp)
                    }

                    Button(
                        onClick = {
                            editarPerfilViewModel.onEvent(EditarPerfilEvent.GuardarCambios)
                            onSaveSuccess()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkGreen,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Guardar",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(text = "Guardar", fontSize = 18.sp)
                    }
                }

                if (uiState.error.isNotEmpty()) {
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NumberInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

