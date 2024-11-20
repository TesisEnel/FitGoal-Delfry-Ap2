package edu.ucne.fitgoal.presentation.ejercicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.fitgoal.data.local.entities.EjercicioEntity
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

    fun onEvent(event: EjercicioEvent){
        when(event){
            is EjercicioEvent.GetEjercicios -> TODO()
            is EjercicioEvent.CloseErrorModal -> closeErrorModal()
        }
    }

    private fun getEjercicios() {
        viewModelScope.launch {
            ejercicioRepository.getEjercicios().collectLatest{ result ->
                when(result){
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

    private fun closeErrorModal() {
        _uiState.value = _uiState.value.copy(isModalErrorVisible = false)
        _uiState.value = _uiState.value.copy(error = "")
    }
}

