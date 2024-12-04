package edu.ucne.fitgoal.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.firebase.auth.FirebaseAuth
import edu.ucne.fitgoal.R
import edu.ucne.fitgoal.presentation.components.LoadingIndicator
import edu.ucne.fitgoal.presentation.components.ModalError
import edu.ucne.fitgoal.presentation.components.ShowComponent
import edu.ucne.fitgoal.ui.theme.Blue
import edu.ucne.fitgoal.ui.theme.DarkGreen
import edu.ucne.fitgoal.ui.theme.Error
import edu.ucne.fitgoal.ui.theme.LightBlue
import edu.ucne.fitgoal.ui.theme.Naranja
import edu.ucne.fitgoal.ui.theme.RojoClaro
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.KFunction1

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    goPerfil: () -> Unit = {},
    goProgresos: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userImageUrl = remember { mutableStateOf("") }
    val isImageLoadingFailed = remember { mutableStateOf(false) }
    val onEvent = viewModel::onEvent

    LaunchedEffect(currentUser) {
        currentUser?.let {
            userImageUrl.value = it.photoUrl?.toString() ?: ""
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 50.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item{
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .clickable {
                                goProgresos()
                            },
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.elevatedCardElevation()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = "Progreso hacia tu meta: ${uiState.porcentaje.toInt()}%")
                            LinearProgressIndicator(
                                progress = {uiState.porcentaje / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                trackColor = Color.Gray.copy(alpha = 0.5f),
                                color = when (uiState.porcentaje) {
                                    in 0f..50f -> RojoClaro
                                    in 51f..89f -> Naranja
                                    else -> DarkGreen
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            ShowComponent(
                                value = uiState.porcentaje >= 100f,
                                whenContentIsTrue = {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "¡Felicidades! Has alcanzado tu meta.",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = "Pulsa el botón azul y agregar una nueva meta para comenzar tu proximo reto",
                                            style = MaterialTheme.typography.titleMedium,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                },
                                whenContentIsFalse = {
                                    Text(
                                        text = "Ultimos Registros:",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            ShowComponent(
                                                value = uiState.ultimosProgresos.isNotEmpty(),
                                                whenContentIsTrue = {
                                                    val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
                                                    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                                                    uiState.ultimosProgresos.forEach { progreso ->
                                                        val formattedDate = progreso.fecha?.let {
                                                            LocalDateTime.parse(it, inputFormatter).toLocalDate().format(outputFormatter)
                                                        } ?: "Fecha no disponible"

                                                        Text(
                                                            text = "Peso del\n$formattedDate\n${progreso.peso?.toInt()} lbs",
                                                            textAlign = TextAlign.Center,
                                                            style = MaterialTheme.typography.labelMedium,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Peso Inicial\n${uiState.pesoInicial} lbs",
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Peso Actual\n${uiState.pesoActual} lbs",
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Peso Ideal\n${uiState.pesoIdeal} lbs",
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            onEvent(HomeEvent.OpenNuevaMeta)
                        },
                        colors = ButtonDefaults.buttonColors(Blue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(12.dp),
                        enabled = !uiState.isLoading
                    ) {
                        Text(
                            text = "Nueva Meta",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    ShowComponent(
                        value = true /*uiState.canPutProgress*/,
                        whenContentIsTrue = {
                            Button(
                                onClick = {
                                    onEvent(HomeEvent.OpenRegistrarProgreso)
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
                                    text = "Registrar Progreso",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }

                items(uiState.tips) { tip ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.elevatedCardElevation()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.TipsAndUpdates,
                                    contentDescription = "",
                                    modifier = Modifier.size(24.dp),
                                    tint = DarkGreen
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = tip.nombre ?: "Sin nombre",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = tip.descripcion ?: "Sin descripción",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
        IconButton(
            onClick = goPerfil,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            ShowComponent(
                value = !isImageLoadingFailed.value && userImageUrl.value.isNotEmpty(),
                whenContentIsTrue = {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(userImageUrl.value)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Imagen de perfil",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        onError = { isImageLoadingFailed.value = true }
                    )
                },
                whenContentIsFalse = {
                    Image(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Ícono de perfil",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                }
            )
        }
    }
    ShowComponent(
        value = uiState.isModalVisible,
        whenContentIsTrue = {
            ModalAgregar(
                uiState = uiState,
                onEvent = viewModel::onEvent
            )
        }
    )

    ShowComponent(
        value = uiState.isModalErrorVisible && uiState.error.isNotEmpty(),
        whenContentIsTrue = {
            ModalError(
                uiState.error
            ){
                viewModel.onEvent(HomeEvent.CloseErrorModal)
            }
        }
    )
    ShowComponent(
        value = uiState.isLoading,
        whenContentIsTrue = {
            LoadingIndicator()
        }
    )
}

@Composable
fun ModalAgregar(uiState: HomeUiState, onEvent: KFunction1<HomeEvent, Unit>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable(enabled = false) {}
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(12.dp)
                .width(500.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            ShowComponent(
                value = uiState.esNuevaMeta,
                whenContentIsTrue = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Nueva Meta",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 16.dp),
                            fontWeight = FontWeight.Bold
                        )
                        TextFiel(
                            texto = "Peso Inicial",
                            valor = uiState.pesoInicial.toString(),
                            onValueChange = { onEvent(HomeEvent.PesoInicialChanged(it)) },
                            error = uiState.pesoInicialError.isNotEmpty(),
                            errorMessage = uiState.pesoInicialError
                        )
                        TextFiel(
                            texto = "Peso Ideal",
                            valor = uiState.pesoIdeal.toString(),
                            imeAction = ImeAction.Done,
                            onValueChange = { onEvent(HomeEvent.PesoIdealChanged(it)) },
                            error = uiState.pesoIdealError.isNotEmpty(),
                            errorMessage = uiState.pesoIdealError
                        )
                        TextFiel(
                            texto = "Peso Actual",
                            valor = uiState.pesoActual.toString(),
                            error = false,
                            errorMessage = "",
                            readOnly = true
                        )
                        Button(
                            onClick = { onEvent(HomeEvent.SaveProgreso) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LightBlue
                            )
                        ) {
                            Text(
                                text = "Guardar",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            )
            ShowComponent(
                value = uiState.esRegistrarProgreso,
                whenContentIsTrue = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Registrar Progreso",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 16.dp),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Solo puedes registrar tu progreso una vez a la semana",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        TextFiel(
                            texto = "Peso Actual",
                            valor = uiState.peso.toString(),
                            imeAction = ImeAction.Done,
                            onValueChange = { onEvent(HomeEvent.PesoChanged(it)) },
                            error = uiState.pesoError.isNotEmpty(),
                            errorMessage = uiState.pesoError
                        )
                        Button(
                            onClick = { onEvent(HomeEvent.SaveProgreso) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LightBlue
                            )
                        ) {
                            Text(
                                text = "Guardar",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            )

            FloatingActionButton(
                onClick = { onEvent(HomeEvent.CloseModal) },
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.TopEnd)
                    .size(40.dp),
                containerColor = RojoClaro
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Cerrar",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun TextFiel(
    texto: String,
    valor: String,
    imeAction: ImeAction = ImeAction.Next,
    error: Boolean = false,
    errorMessage: String = "",
    onValueChange: (String) -> Unit = {},
    readOnly: Boolean = false
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        readOnly = readOnly,
        value = valor,
        onValueChange = { onValueChange(it) },
        label = { Text(texto) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) },
            onDone = { focusManager.clearFocus() }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DarkGreen,
            focusedLabelColor = DarkGreen,
            cursorColor = DarkGreen
        )
    )
    ShowComponent(
        value = error,
        whenContentIsTrue = {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = Error,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    )
}