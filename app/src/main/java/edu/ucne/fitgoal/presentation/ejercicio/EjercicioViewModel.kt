package edu.ucne.fitgoal.presentation.ejercicio

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.repository.EjercicioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EjercicioViewModel @Inject constructor(
    private val ejercicioRepository: EjercicioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getEjercicios()
    }

    fun onEvent(event: EjercicioEvent) {
        when (event) {
            is EjercicioEvent.GetEjercicios -> getEjercicios()
            is EjercicioEvent.CloseErrorModal -> closeErrorModal()
            is EjercicioEvent.CloseDetailModal -> closeDetailModal()
            is EjercicioEvent.FilterEjercicios -> filterEjercicios(event.filtro)
            is EjercicioEvent.SelectEjercicio -> selectEjercicio(event.ejercicioId)
        }
    }

    private fun getEjercicios() {
        viewModelScope.launch {
            ejercicioRepository.getEjercicios().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            error = "",
                            isLoading = true
                        )
                    }

                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            error = "",
                            isLoading = false,
                            ejercicios = result.data ?: emptyList()
                        )
                    }

                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            error = result.message ?: "Error",
                            isLoading = false,
                            isModalErrorVisible = true
                        )
                    }
                }
            }
        }
    }

    private fun filterEjercicios(filtro: String) {
        _uiState.value = _uiState.value.copy(filtro = filtro)
        viewModelScope.launch {
            val ejercicios = ejercicioRepository.getEjerciciosLocales()
            ejercicios.collect {
                _uiState.value = _uiState.value.copy(
                    ejercicios = if (filtro.isEmpty()) it else it.filter { ejercicio ->
                        ejercicio.nombreEjercicio?.lowercase()
                            ?.contains(filtro, ignoreCase = true) ?: false
                    }
                )
            }
        }
    }

    private fun closeErrorModal() {
        _uiState.value = _uiState.value.copy(isModalErrorVisible = false)
        _uiState.value = _uiState.value.copy(error = "")
    }

    private fun closeDetailModal() {
        _uiState.value = _uiState.value.copy(isModalDetailVisible = false)
    }

    private fun selectEjercicio(ejercicioId: Int) {
       Log.d("EjercicioViewModel", "Ejercicio seleccionado: $ejercicioId")
        _uiState.value = _uiState.value.copy(
            isModalDetailVisible = true,
            selectedEjercicio = _uiState.value.ejercicios.find { it.ejercicioId == ejercicioId }
        )
    }
}

