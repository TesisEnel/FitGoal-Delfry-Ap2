package edu.ucne.fitgoal.presentation.plantilla

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import edu.ucne.fitgoal.data.local.entities.EjerciciosEntity
import edu.ucne.fitgoal.data.remote.dto.PlantillaDto
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PlantillaDeleteDialog(
    plantilla: PlantillaDto,
    onDismiss: () -> Unit,
    onDeleteConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF44336))
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¿Estás seguro de eliminar esta plantilla?",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Cancelar", color = Color.White)
                    }
                    Button(
                        onClick = {
                            onDeleteConfirm()
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFd32f2f))
                    ) {
                        Text("Eliminar", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun EjercicioItem(
    ejercicio: EjerciciosEntity,
    plantilla: PlantillaDto?,
    onDeleteConfirm: () -> Unit,
    viewModel: PlantillaViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var timeInSeconds by remember { mutableIntStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }

    var totalRoutineTimeInSeconds by remember { mutableIntStateOf(0) }
    var isRoutineRunning by remember { mutableStateOf(false) }

    fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (isRunning) {
                delay(1000L)
                timeInSeconds++
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar este ejercicio?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminarPlantilla(plantilla!!.plantillaId)
                    }
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = ejercicio.descripcion,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cerrar")
                    }
                }
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showDialog = true
            }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(ejercicio.foto),
            contentDescription = ejercicio.nombreEjercicio,
            modifier = Modifier
                .size(48.dp)
                .padding(end = 8.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = ejercicio.nombreEjercicio,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = formatTime(timeInSeconds),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
        )
    }


    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                if (!isRoutineRunning) {
                    isRoutineRunning = true
                    isRunning = true
                    isPaused = false
                }
            },
            enabled = !isRoutineRunning
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Iniciar Rutina")
        }

        IconButton(
            onClick = {
                isRoutineRunning = false
                isRunning = false
                isPaused = true
            },
            enabled = isRoutineRunning
        ) {
            Icon(Icons.Default.Pause, contentDescription = "Pausar Rutina")
        }

        IconButton(
            onClick = {
                isRoutineRunning = false
                isRunning = false
                isPaused = false
                totalRoutineTimeInSeconds = 0
                timeInSeconds = 0
            },
            enabled = isRoutineRunning || isPaused
        ) {
            Icon(Icons.Default.Stop, contentDescription = "Detener Rutina")
        }
    }
}