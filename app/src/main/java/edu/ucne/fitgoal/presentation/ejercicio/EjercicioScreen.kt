package edu.ucne.fitgoal.presentation.ejercicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import coil3.compose.AsyncImage
import edu.ucne.fitgoal.R
import edu.ucne.fitgoal.data.local.entities.EjercicioEntity
import edu.ucne.fitgoal.presentation.components.LoadingIndicator
import edu.ucne.fitgoal.presentation.components.ModalError
import edu.ucne.fitgoal.ui.theme.AmarilloVerde
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
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Image(
                    painter = painterResource(R.drawable.ejercicios),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                                .height(250.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    )
                ) {
                    EjerciciosItemLazy(uiState.ejercicios)
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .height(250.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    )
                ) {
                    EjerciciosItemLazy(uiState.ejercicios)
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
    }
}

@Composable
fun EjerciciosItemLazy(ejercicios: List<EjercicioEntity>) {
    LazyRow(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(ejercicios) { ejercicio ->
            Surface(
                modifier = Modifier
                    .width(200.dp)
                    .height(250.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFF969696),
                        shape = RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        VerdeClaro, AmarilloVerde
                                    )
                                )
                            )
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(Color.White),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Text(
                            text = ejercicio.nombreEjercicio ?: "",
                            color = Color.Black,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(top = 30.dp)
                                .padding(horizontal = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ejercicio.foto,
                        contentDescription = "Imagen circular",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}