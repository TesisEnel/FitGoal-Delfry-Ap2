package edu.ucne.fitgoal.presentation.aguadiaria

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import edu.ucne.fitgoal.R
import edu.ucne.fitgoal.data.local.entities.HorarioBebidaEntity
import edu.ucne.fitgoal.presentation.components.LoadingIndicator
import edu.ucne.fitgoal.presentation.components.ModalError
import edu.ucne.fitgoal.ui.theme.AmarilloVerde
import edu.ucne.fitgoal.ui.theme.DarkGreen
import edu.ucne.fitgoal.ui.theme.Error
import edu.ucne.fitgoal.ui.theme.Hueso
import edu.ucne.fitgoal.ui.theme.LightBlue
import edu.ucne.fitgoal.ui.theme.LightGreen
import edu.ucne.fitgoal.ui.theme.RojoClaro
import edu.ucne.fitgoal.ui.theme.VerdeClaro
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.KFunction1
import android.content.Context
import android.text.format.DateFormat
import androidx.compose.animation.animateColorAsState
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

@Composable
fun HorarioScreen(
    horarioViewModel: HorarioBebidaViewModel = hiltViewModel()
) {
    val uiState by horarioViewModel.uiState.collectAsState()
    val onEvent = horarioViewModel::onEvent
    val context = LocalContext.current
    LaunchedEffect(uiState.update) {
        onEvent(HorarioBebidaEvent.GetHorarioBebidas(context))
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(
                        RoundedCornerShape(12.dp)
                    )
            ) {
                Image(
                    painter = painterResource(R.drawable.agua),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .clip(
                            RoundedCornerShape(12.dp)
                        ),
                    contentScale = ContentScale.Crop
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .border(2.dp, LightGreen, shape = RoundedCornerShape(8.dp)),
                elevation = CardDefaults.elevatedCardElevation(8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Agua Diaria Sugerida: 2,500ML",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(listOf(
                            Color.White, VerdeClaro, AmarilloVerde, DarkGreen
                        )), alpha = 0.1f)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    items(uiState.horarios, key = { it.horarioBebidaId }) { horario ->
                        val coroutineScope = rememberCoroutineScope()
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { state ->
                                if (state == SwipeToDismissBoxValue.EndToStart) {
                                    coroutineScope.launch {
                                        delay(0.5.seconds)
                                        onEvent(HorarioBebidaEvent.DeleteHorarioBebida(horario.horarioBebidaId))
                                    }
                                    true
                                } else {
                                    false
                                }
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                val color by animateColorAsState(
                                    when (dismissState.targetValue) {
                                        SwipeToDismissBoxValue.Settled -> Color.Transparent
                                        SwipeToDismissBoxValue.EndToStart -> Color(0xFFCB4238)
                                        SwipeToDismissBoxValue.StartToEnd -> TODO()
                                    }, label = "Changing color"
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color, shape = RoundedCornerShape(8.dp))
                                        .padding(16.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }

                            },
                            modifier = Modifier
                        ){
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                                    .border(2.dp, LightGreen, shape = RoundedCornerShape(8.dp))
                                    .clickable { onEvent(HorarioBebidaEvent.SelectHorariosBebida(horario.horarioBebidaId)) },
                                elevation = CardDefaults.elevatedCardElevation(8.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Cantidad a Tomar: ${horario.cantidad}ML",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Hora: ${parsearHora(horario.hora!!, LocalContext.current)}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = {onEvent(HorarioBebidaEvent.OpenModal)},
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.BottomEnd)
                .border(2.dp, LightGreen, shape = RoundedCornerShape(12.dp)),
            containerColor = Color.White
        ) {
            Icon(
                Icons.Filled.Add,
                "Agregar",
                tint = LightGreen
            )
        }
        if (uiState.isLoading) {
            LoadingIndicator()
        }
        if (uiState.error != "") {
            ModalError(
                error = uiState.error,
                onclick = { onEvent(HorarioBebidaEvent.CloseModalError) }
            )
        }
        if(uiState.isModalVisible && !uiState.isLoading){
            ModalAgregar(uiState, onEvent)
        }
    }
}

@Composable
fun ModalAgregar(uiState: UiState, onEvent: KFunction1<HorarioBebidaEvent, Unit>){
    val timeDialogState = rememberMaterialDialogState()
    val focusManager = LocalFocusManager.current
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Agua a Tomar",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp),
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = uiState.cantidad.toString(),
                    onValueChange = { onEvent(HorarioBebidaEvent.CantidadChange(it)) },
                    label = { Text("Cantidad de Agua (ML)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkGreen,
                        focusedLabelColor = DarkGreen,
                        cursorColor = DarkGreen
                    )
                )
                if(uiState.cantidadError!= ""){
                    Text(
                        text = uiState.cantidadError,
                        style = MaterialTheme.typography.bodySmall,
                        color = Error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = parsearHora(uiState.hora, LocalContext.current),
                        onValueChange = { onEvent(HorarioBebidaEvent.HoraChange(it)) },
                        label = { Text("Hora") },
                        readOnly = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DarkGreen,
                            focusedLabelColor = DarkGreen,
                            cursorColor = DarkGreen
                        )
                    )
                    Button(
                        onClick = { timeDialogState.show() },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkGreen
                        )
                    ) {
                        Text(text = "Establecer Hora")
                    }
                }
                if(uiState.horaError!= ""){
                    Text(
                        text = uiState.horaError,
                        style = MaterialTheme.typography.bodySmall,
                        color = Error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Button(
                    onClick = { onEvent(HorarioBebidaEvent.SaveHorarioBebida) },
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

            FloatingActionButton(
                onClick = { onEvent(HorarioBebidaEvent.CloseModal) },
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

    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton(
                text = "Ok",
                textStyle = TextStyle(color = DarkGreen)
            )
            negativeButton(
                text = "Cancelar",
                textStyle = TextStyle(color = DarkGreen)
            )
        }
    ) {
        timepicker(
            initialTime = runCatching {
                LocalTime.parse(uiState.hora)
            }.getOrElse {
                LocalTime.now()
            },
            title = "Selecciona la Hora",
            colors = TimePickerDefaults.colors(
                activeBackgroundColor = Hueso,
                inactiveBackgroundColor = Hueso,
                activeTextColor = DarkGreen,
                inactiveTextColor = Color.Black.copy(alpha = 0.7f),
                inactivePeriodBackground = Hueso,
                selectorColor = DarkGreen,
                selectorTextColor = Color.White,
                headerTextColor = Color.Black,
                borderColor = Hueso
            )
        ) { pickedTime ->
            onEvent(HorarioBebidaEvent.HoraChange(
                pickedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
            ))
        }
    }
}

fun parsearHora(hora: String, context: Context): String {
    val is24HourFormat = DateFormat.is24HourFormat(context)
    val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val outputFormat = if (is24HourFormat) {
        SimpleDateFormat("HH:mm", Locale.getDefault())
    } else {
        SimpleDateFormat("hh:mm a", Locale.getDefault())
    }
    return try {
        val date = inputFormat.parse(hora)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        "Formato inválido"
    }
}