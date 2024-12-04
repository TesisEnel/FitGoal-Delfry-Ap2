package edu.ucne.fitgoal.presentation.editarPerfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.UsuarioDto
import edu.ucne.fitgoal.data.repository.PerfilRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditarPerfilViewModel @Inject constructor(
    private val perfilRepository: PerfilRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditarPerfilUiState())
    val uiState: StateFlow<EditarPerfilUiState> = _uiState

    fun fetchUserData(userId: String) {
        viewModelScope.launch {
            perfilRepository.getUsuario(userId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        val usuario = resource.data
                        if (usuario != null) {
                            _uiState.update {
                                it.copy(
                                    usuarioId = usuario.usuarioId,
                                    nombre = usuario.nombre,
                                    apellido = usuario.apellido,
                                    correo = usuario.correo,
                                    edad = usuario.edad,
                                    altura = usuario.altura,
                                    pesoInicial = usuario.pesoInicial,
                                    pesoActual = usuario.pesoActual,
                                    pesoIdeal = usuario.pesoIdeal,
                                    aguaDiaria = usuario.aguaDiaria,
                                    isLoading = false
                                )
                            }
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false, error = resource.message ?: "Error al cargar datos")
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: EditarPerfilEvent) {
        when (event) {
            is EditarPerfilEvent.UpdateEdad -> onEdadChange(event.edad)
            is EditarPerfilEvent.UpdateAltura -> onAlturaChange(event.altura)
            is EditarPerfilEvent.UpdatePesoInicial -> onPesoInicialChange(event.pesoInicial)
            is EditarPerfilEvent.UpdatePesoActual -> onPesoActualChange(event.pesoActual)
            is EditarPerfilEvent.UpdatePesoIdeal -> onPesoIdealChange(event.pesoIdeal)
            is EditarPerfilEvent.UpdateAguaDiaria -> onAguaDiariaChange(event.aguaDiaria)
            is EditarPerfilEvent.GuardarCambios -> saveChanges()
        }
    }

    private fun onEdadChange(age: Int) {
        if (age > 0) {
            _uiState.update { it.copy(edad = age) }
        } else {
            _uiState.update { it.copy(error = "La edad debe ser mayor a 0") }
        }
    }

    private fun onAlturaChange(height: Float) {
        if (height > 0) {
            _uiState.update { it.copy(altura = height) }
        } else {
            _uiState.update { it.copy(error = "La altura debe ser mayor a 0") }
        }
    }

    private fun onPesoInicialChange(initialWeight: Float) {
        if (initialWeight > 0) {
            _uiState.update { it.copy(pesoInicial = initialWeight) }
        } else {
            _uiState.update { it.copy(error = "El peso inicial debe ser mayor a 0") }
        }
    }

    private fun onPesoActualChange(currentWeight: Float) {
        if (currentWeight > 0) {
            _uiState.update { it.copy(pesoActual = currentWeight) }
        } else {
            _uiState.update { it.copy(error = "El peso actual debe ser mayor a 0") }
        }
    }

    private fun onPesoIdealChange(idealWeight: Float) {
        if (idealWeight > 0) {
            _uiState.update { it.copy(pesoIdeal = idealWeight) }
        } else {
            _uiState.update { it.copy(error = "El peso ideal debe ser mayor a 0") }
        }
    }

    private fun onAguaDiariaChange(dailyWater: Float) {
        if (dailyWater > 0) {
            _uiState.update { it.copy(aguaDiaria = dailyWater) }
        } else {
            _uiState.update { it.copy(error = "El consumo diario de agua debe ser mayor a 0") }
        }
    }

    private fun saveChanges() {
        viewModelScope.launch {
            val currentState = uiState.value
            val usuarioDto = UsuarioDto(
                usuarioId = currentState.usuarioId,
                nombre = currentState.nombre,
                apellido = currentState.apellido,
                correo = currentState.correo,
                edad = currentState.edad,
                altura = currentState.altura,
                pesoInicial = currentState.pesoInicial,
                pesoActual = currentState.pesoActual,
                pesoIdeal = currentState.pesoIdeal,
                aguaDiaria = currentState.aguaDiaria
            )

            perfilRepository.updateUsuario(currentState.usuarioId, usuarioDto).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false, error = resource.message ?: "Error al guardar datos")
                        }
                    }
                }
            }
        }
    }
}
