package edu.ucne.fitgoal.data.remote.dto

import edu.ucne.fitgoal.data.local.entities.UsuarioEntity

data class UsuarioDto(
    val usuarioId: String?,
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val edad: Int = 0,
    val altura: Float = 0.0f,
    val pesoInicial: Float = 0.0f,
    val pesoActual: Float = 0.0f,
    val pesoIdeal: Float = 0.0f,
    val aguaDiaria: Float = 0.0f
)

fun UsuarioDto.toEntity(): UsuarioEntity {
    return UsuarioEntity(
        usuarioId = this.usuarioId!!,
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
}
