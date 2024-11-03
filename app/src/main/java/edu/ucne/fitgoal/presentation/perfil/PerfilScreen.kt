package edu.ucne.fitgoal.presentation.perfil

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PerfilScreen(
    onDrawer: () -> Unit,
    goToPlanificador: () -> Unit,
    goToReloj: () -> Unit
) {
    Text(
        text = "Bienvenido al Perfil",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
}

@Preview
@Composable
fun PerfilScreenPreview() {
    PerfilScreen(
        onDrawer = TODO(),
        goToPlanificador = TODO(),
        goToReloj = TODO()
    )
}