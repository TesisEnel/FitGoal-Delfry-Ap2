package edu.ucne.fitgoal.presentation.ejercicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import edu.ucne.fitgoal.R
import edu.ucne.fitgoal.data.local.entities.EjercicioEntity
import edu.ucne.fitgoal.presentation.components.LoadingIndicator
import edu.ucne.fitgoal.presentation.components.ModalError
import edu.ucne.fitgoal.presentation.components.SearchTextField
import edu.ucne.fitgoal.ui.theme.AmarilloVerde
import edu.ucne.fitgoal.ui.theme.DarkGreen
import edu.ucne.fitgoal.ui.theme.Hueso
import edu.ucne.fitgoal.ui.theme.LightGreen
import edu.ucne.fitgoal.ui.theme.RojoClaro
import edu.ucne.fitgoal.ui.theme.VerdeClaro

@Composable
fun EjercicioScreen(
    ejercicioViewModel: EjercicioViewModel = hiltViewModel()
) {
    val uiState by ejercicioViewModel.uiState.collectAsStateWithLifecycle()
    val onEvent = ejercicioViewModel::onEvent

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
                        RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp
                        )
                    )
            ) {
                Image(
                    painter = painterResource(R.drawable.ejercicios),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 12.dp,
                                topEnd = 12.dp
                            )
                        ),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 12.dp,
                            bottomEnd = 12.dp
                        )
                    )
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                SearchTextField(
                    value = uiState.filtro,
                    onValueChange = { onEvent(EjercicioEvent.FilterEjercicios(it)) },
                    onRefreshClick = { onEvent(EjercicioEvent.GetEjercicios) },
                    placeholderText = "Buscar"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(listOf(Color.White,VerdeClaro, AmarilloVerde,
                        DarkGreen)), alpha = 0.1f)
            ) {
                EjerciciosItemsGrid(uiState.ejercicios) {
                    onEvent(EjercicioEvent.SelectEjercicio(it))
                }
            }
        }
        if (uiState.isLoading) {
            LoadingIndicator()
        }
        if (uiState.isModalErrorVisible) {
            ModalError(
                error = uiState.error,
                onclick = { onEvent(EjercicioEvent.CloseErrorModal) }
            )
        }
        if(uiState.isModalDetailVisible){
            ModalEjercicio(uiState.selectedEjercicio!!){
                onEvent(EjercicioEvent.CloseDetailModal)
            }
        }
    }
}

@Composable
fun EjerciciosItemsGrid(
    ejercicios: List<EjercicioEntity>,
    onEjercicioClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 185.dp),
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        items(ejercicios) { ejercicio ->
            Card(
                modifier = Modifier
                    .height(250.dp)
                    .padding(6.dp).clickable { onEjercicioClick(ejercicio.ejercicioId) },
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.6f)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            VerdeClaro, AmarilloVerde
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            SubcomposeAsyncImage(
                                model = ejercicio.foto,
                                contentDescription = "Imagen circular",
                                modifier = Modifier
                                    .size(130.dp)
                                    .clip(CircleShape),
                                loading = {
                                    Box(
                                        modifier = Modifier
                                            .size(130.dp)
                                            .background(Color.Black.copy(alpha = 0.5f))
                                            .clickable(enabled = false) {}
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .size(130.dp),
                                            color = LightGreen,
                                            strokeWidth = 10.dp,
                                        )
                                    }
                                }
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.4f)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = ejercicio.nombreEjercicio ?: "",
                                color = Color.Black,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ModalEjercicio(ejercicio: EjercicioEntity, onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable(enabled = false) {}
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item{
                    ejercicio.foto?.let { fotoUrl ->
                        SubcomposeAsyncImage(
                            model = ejercicio.foto,
                            contentDescription = "Imagen circular",
                            modifier = Modifier
                                .size(180.dp),
                            loading = {
                                Box(
                                    modifier = Modifier
                                        .size(180.dp).clip(CircleShape)
                                        .background(Color.Black.copy(alpha = 0.5f))
                                        .clickable(enabled = false) {}
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .size(130.dp),
                                        color = LightGreen,
                                        strokeWidth = 10.dp,
                                    )
                                }
                            }
                        )
                    } ?: Box(
                        modifier = Modifier
                            .size(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Sin foto", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = ejercicio.nombreEjercicio ?: "Ejercicio desconocido",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .background(Hueso, shape = RoundedCornerShape(16.dp))
                            .padding(4.dp)
                    ) {
                        LazyColumn {
                            item {
                                Text(
                                    text = ejercicio.descripcion ?: "Sin descripción",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                FloatingActionButton(
                    onClick = { onClose() },
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
}