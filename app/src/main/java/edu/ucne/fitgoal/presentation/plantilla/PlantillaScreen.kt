package edu.ucne.fitgoal.presentation.plantilla

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.PlantillaDto
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantillaScreen(
    viewModel: PlantillaViewModel = hiltViewModel(),
    goToCrearRutina: () -> Unit,
    goToReloj: () -> Unit,
    goToPerfil: () -> Unit,
    goToCalculadora: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var plantillaSeleccionada by remember { mutableStateOf<PlantillaDto?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Plantillas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4CAF50),
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = goToCrearRutina,
                containerColor = Color(0xFF81C784)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear rutina", tint = Color.White)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (uiState) {
                is Resource.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF4CAF50))
                }

                is Resource.Success -> {
                    val data = (uiState as Resource.Success<*>).data
                    if (data is List<*> && data.all { it is PlantillaDto }) {
                        val plantillas = data.filterIsInstance<PlantillaDto>()
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(plantillas) { plantilla ->
                                PlantillaItem(
                                    plantilla = plantilla,
                                    onEliminarClick = {
                                        plantillaSeleccionada = plantilla
                                        showDeleteDialog = true
                                    },
                                    viewModel = viewModel
                                )
                            }
                        }
                    } else {
                        Text("Error: Datos inesperados", color = Color(0xFFD32F2F))
                    }
                }
                is Resource.Error -> {
                    Text(
                        text = "Error: ${(uiState as Resource.Error).message}",
                        color = Color(0xFFD32F2F)
                    )
                }
            }
        }
    }

    if (showDeleteDialog && plantillaSeleccionada != null) {
        PlantillaDeleteDialog(
            plantilla = plantillaSeleccionada!!,
            onDismiss = { showDeleteDialog = false },
            onDeleteConfirm = {
                viewModel.eliminarPlantilla(plantillaSeleccionada!!.plantillaId)
                showDeleteDialog = false
            }
        )
    }
}

@Composable
fun PlantillaItem(
    plantilla: PlantillaDto,
    onEliminarClick: (PlantillaDto) -> Unit,
    viewModel: PlantillaViewModel
) {
    var showEjerciciosDialog by remember { mutableStateOf(false) }
    var tiempoTranscurrido by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }

    val tiempoFormateado = remember { mutableStateOf("") }

    LaunchedEffect(tiempoTranscurrido) {
        tiempoFormateado.value = viewModel.formatToMMSS(tiempoTranscurrido.toString()).first
    }
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (isRunning) {
                delay(1000L)
                tiempoTranscurrido++
            }
        }
    }

    if (showEjerciciosDialog) {
        Dialog(onDismissRequest = { showEjerciciosDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = plantilla.nombrePlantilla,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = tiempoFormateado.value,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                if (!isRunning) {
                                    isRunning = true
                                    isPaused = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Text("Iniciar", color = Color.White)
                        }

                        Button(
                            onClick = {
                                isRunning = false
                                isPaused = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
                        ) {
                            Text("Pausar", color = Color.White)
                        }

                        Button(
                            onClick = {
                                isRunning = false
                                isPaused = false
                                tiempoTranscurrido = 0
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                        ) {
                            Text("Detener", color = Color.White)
                        }
                    }

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(plantilla.ejercicios) { ejercicio ->
                            EjercicioItem(ejercicio = ejercicio, plantilla = plantilla, onDeleteConfirm = {}, viewModel = viewModel)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { showEjerciciosDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Cerrar", color = Color.White)
                    }
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { showEjerciciosDialog = true },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9))
    ) {
        Box {
            IconButton(
                onClick = { onEliminarClick(plantilla) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Eliminar plantilla",
                    tint = Color(0xFF1B5E20)
                )
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = plantilla.nombrePlantilla,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = plantilla.descripcionPlantilla,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF388E3C))
                )

                Column {
                    plantilla.ejercicios.forEach { ejercicio ->
                        ejercicio.nombreEjercicio?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF2E7D32))
                            )
                        }
                    }
                }
            }
        }
    }
}