package edu.ucne.fitgoal.presentation.reloj

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RelojScreen(
    onDrawer: () -> Unit,
    goToPlanificador: () -> Unit,
    goToPerfil: () -> Unit
) {
    Text(
        text = "Bienvenido al Relojjjj",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
}

@Preview
@Composable
fun RelojScreenPreview() {
    RelojScreen(
        onDrawer = TODO(),
        goToPlanificador = TODO(),
        goToPerfil = TODO()
    )
}