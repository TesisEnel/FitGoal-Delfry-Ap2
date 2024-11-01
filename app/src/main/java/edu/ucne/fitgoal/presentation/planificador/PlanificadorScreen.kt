package edu.ucne.fitgoal.presentation.planificador

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PlanificadorScreen(
    goToPerfil: () -> Unit,
    onDrawer: () -> Unit,
    goToReloj: () -> Unit
) {
    Text(
        text = "Bienvenido al Planificador",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
}

@Preview
@Composable
fun PlanificadorScreenPreview() {
    PlanificadorScreen(
        goToPerfil = TODO(),
        onDrawer = TODO(),
        goToReloj = {
            TODO()
        }
    )
}