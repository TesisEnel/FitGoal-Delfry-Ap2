package edu.ucne.fitgoal.presentation.calculadora

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CalculadoraScreen(
    onDrawer: () -> Unit,
    goToPlanificador: () -> Unit,
    goToReloj: () -> Unit,
    goToPerfil: () -> Unit
) {
    Text(
        text = "Bienvenido a la calculadora",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
}

@Preview
@Composable
fun CalculadoraScreenPreview() {
    CalculadoraScreen(
        onDrawer = TODO(),
        goToPlanificador = TODO(),
        goToReloj = TODO(),
        goToPerfil = TODO()
    )
}
