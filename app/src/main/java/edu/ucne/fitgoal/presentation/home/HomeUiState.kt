package edu.ucne.fitgoal.presentation.home

import edu.ucne.fitgoal.data.local.entities.ProgresoUsuarioEntity
import edu.ucne.fitgoal.data.local.entities.TipEntity
import edu.ucne.fitgoal.data.local.entities.UsuarioEntity
import edu.ucne.fitgoal.data.remote.dto.ProgresoUsuarioDto
import edu.ucne.fitgoal.data.remote.dto.UsuarioDto
import java.time.LocalDate

data class HomeUiState(
    val tips: List<TipEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val update: Boolean = false,
    val canPutProgress: Boolean = false,
    val esNuevaMeta: Boolean = false,
    val esRegistrarProgreso: Boolean = false,
    val isModalErrorVisible: Boolean = false,
    val isModalVisible: Boolean = false,
    val progresos: List<ProgresoUsuarioEntity> = emptyList(),
    val ultimosProgresos: List<ProgresoUsuarioEntity> = emptyList(),
    val usuario: UsuarioEntity? = null,
    val usuarioId: String = "",
    val fecha : String = LocalDate.now().toString(),
    val peso: Float = 0f,
    val pesoError: String = "",
    val pesoIdeal: Float = 0f,
    val pesoIdealError: String = "",
    val pesoActual: Float = 0f,
    val pesoInicial: Float = 0f,
    val pesoInicialError: String = "",
    val porcentaje: Float = 0f
)

fun HomeUiState.toDto() = ProgresoUsuarioDto(
    usuarioId = this.usuarioId,
    fecha = this.fecha,
    peso = this.peso
)

fun UsuarioEntity.toDto() = UsuarioDto(
    usuarioId = this.usuarioId,
    nombre = this.nombre,
    apellido = this.apellido,
    correo = this.correo,
    edad = this.edad,
    altura = this.altura,
    pesoInicial = this.pesoInicial,
    pesoActual = this.pesoActual,
    pesoIdeal = this.pesoIdeal,
    aguaDiaria = this.aguaDiaria
)