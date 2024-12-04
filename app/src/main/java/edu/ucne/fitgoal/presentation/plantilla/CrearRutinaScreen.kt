package edu.ucne.fitgoal.presentation.plantilla

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.EjerciciosDto
import edu.ucne.fitgoal.presentation.Ejercicios.EjercicioViewModel

@Composable
fun CrearRutinaScreen(
    viewModel: EjercicioViewModel = hiltViewModel(),
    plantillaViewModel: PlantillaViewModel = hiltViewModel(),
    goToPlanificador: () -> Unit,
    goToPerfil: () -> Unit,
    goToReloj: () -> Unit,
    goToCalculadora: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedEjercicios = viewModel.selectedEjercicios

    LaunchedEffect(Unit) {
        viewModel.obtenerEjercicios()
    }

    CrearRutinaContent(
        uiState = uiState,
        selectedEjercicios = selectedEjercicios,
        onBuscar = viewModel::buscarEjercicios,
        onFiltrar = viewModel::filtrarPorParteCuerpo,
        onToggleEjercicioSeleccionado = viewModel::toggleEjercicioSeleccionado,
        onSetRepeticionesYSeries = viewModel::setRepeticionesYSeries,
        onSaveRutina = { nombre, descripcion, tiempo ->
            val timeInSeconds = convertTimeToSeconds(tiempo)
            plantillaViewModel.crearPlantilla(
                nombre = nombre,
                descripcion = descripcion,
                ejercicios = selectedEjercicios.map { it.toEntity() },
                duracionTotal = timeInSeconds.toString()
            )
            goToPlanificador()
        },
        onSaveRepsAndSeries = viewModel::saveRepsAndSeries
    )
}
fun convertTimeToSeconds(time: String): Int {

    val parts = time.split(":")
    val minutes = if (parts.size > 0) parts[0].toIntOrNull() ?: 0 else 0
    val seconds = if (parts.size > 1) parts[1].toIntOrNull() ?: 0 else 0
    return (minutes * 60) + seconds
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearRutinaContent(
    uiState: Resource<List<EjerciciosDto>>,
    selectedEjercicios: List<EjerciciosDto>,
    onBuscar: (String) -> Unit,
    onFiltrar: (String) -> Unit,
    onToggleEjercicioSeleccionado: (EjerciciosDto) -> Unit,
    onSaveRutina: (String, String, String) -> Unit,
    onSetRepeticionesYSeries: (EjerciciosDto, Int, Int) -> Unit,
    onSaveRepsAndSeries: (Int, Int, Int) -> Unit
) {
    var busqueda by remember { mutableStateOf("") }
    var filtroParteCuerpo by remember { mutableStateOf("Parte del cuerpo") }
    var showDialog by remember { mutableStateOf(false) }
    var nombrePlantilla by remember { mutableStateOf("") }
    var descripcionPlantilla by remember { mutableStateOf("") }
    var timeInput by remember { mutableStateOf("00:00") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear rutina") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF388E3C),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { value ->
                    busqueda = value
                    onBuscar(value)
                },
                label = { Text("Buscar ejercicios") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DropdownMenuFiltro(
                    filtroActual = filtroParteCuerpo,
                    onFiltroSeleccionado = { filtro ->
                        filtroParteCuerpo = filtro
                        onFiltrar(filtro)
                    }
                )
                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66BB6A))
                ) {
                    Text("Listo")
                }
            }

            when (uiState) {
                is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                is Resource.Success -> {
                    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                        items(uiState.data.orEmpty()) { ejercicio ->
                            ItemEjercicio(
                                ejercicio = ejercicio,
                                onToggleSeleccionado = onToggleEjercicioSeleccionado,
                                isSelected = selectedEjercicios.contains(ejercicio),
                                onSetRepeticionesYSeries = onSetRepeticionesYSeries,
                                onSaveRepsAndSeries = onSaveRepsAndSeries
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    Text(
                        text = "Error: ${uiState.message.orEmpty()}",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(onClick = {
                        onSaveRutina(nombrePlantilla, descripcionPlantilla, timeInput)
                        showDialog = false
                    }) {
                        Text("Guardar rutina")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancelar")
                    }
                },
                title = { Text("Crear nueva rutina") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = nombrePlantilla,
                            onValueChange = { nombrePlantilla = it },
                            label = { Text("Nombre de la plantilla") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = descripcionPlantilla,
                            onValueChange = { descripcionPlantilla = it },
                            label = { Text("Descripción de la plantilla") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Ejercicios seleccionados:",
                            fontWeight = FontWeight.Bold
                        )
                        selectedEjercicios.forEach { ejercicio ->
                            Text("- ${ejercicio.nombreEjercicio}")
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = timeInput,
                            onValueChange = { newTime ->
                                if (newTime.matches(Regex("\\d{0,2}:\\d{0,2}"))) {
                                    timeInput = newTime
                                }
                            },
                            label = { Text("Duración total (MM:SS)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun ItemEjercicio(
    ejercicio: EjerciciosDto,
    onToggleSeleccionado: (EjerciciosDto) -> Unit,
    isSelected: Boolean,
    onSetRepeticionesYSeries: (EjerciciosDto, Int, Int) -> Unit,
    onSaveRepsAndSeries: (Int, Int, Int) -> Unit
) {
    var showModal by remember { mutableStateOf(false) }
    var repeticiones by remember { mutableStateOf(1) }
    var series by remember { mutableStateOf(1) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                onToggleSeleccionado(ejercicio)
            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    showModal = true
                }
        ) {
            Image(
                painter = rememberImagePainter(data = ejercicio.foto),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 8.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ejercicio.nombreEjercicio,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Icon(
                imageVector = if (isSelected) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                contentDescription = null
            )
        }
    }

    if (showModal) {
        Dialog(onDismissRequest = { showModal = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Configurar ejercicio", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(onClick = { if (repeticiones > 1) repeticiones-- }) {
                            Text("-")
                        }
                        Text(text = "Reps: $repeticiones", modifier = Modifier.padding(horizontal = 16.dp))
                        Button(onClick = { repeticiones++ }) {
                            Text("+")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(onClick = { if (series > 1) series-- }) {
                            Text("-")
                        }
                        Text(text = "Series: $series", modifier = Modifier.padding(horizontal = 16.dp))
                        Button(onClick = { series++ }) {
                            Text("+")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = { showModal = false }) {
                            Text("Cancelar")
                        }
                        Button(onClick = {
                            onSetRepeticionesYSeries(ejercicio, repeticiones, series)
                            onSaveRepsAndSeries(ejercicio.ejercicioId, repeticiones, series)
                            showModal = false
                        }) {
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownMenuFiltro(
    filtroActual: String,
    onFiltroSeleccionado: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            onClick = { expanded = !expanded },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784))
        ) {
            Text(filtroActual, fontSize = 16.sp, color = Color.White)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOf(
                "Parte del cuerpo", "spines", "abs", "glutes", "delts",
                "calves", "traps", "biceps", "triceps", "pectorals",
                "quads", "forearms", "upper back", "lats", "hamstrings"
            ).forEach { filtro ->
                DropdownMenuItem(
                    onClick = {
                        onFiltroSeleccionado(filtro)
                        expanded = false
                    },
                    text = { Text(filtro) }
                )
            }
        }
    }
}