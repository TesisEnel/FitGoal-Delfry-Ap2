package edu.ucne.fitgoal.presentation.ejercicio

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.fitgoal.data.remote.RemoteDataSource
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.EjercicioDto
import edu.ucne.fitgoal.data.repository.EjercicioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow


@HiltViewModel
class EjercicioViewModel @Inject constructor(
    private val ejercicioRepository: EjercicioRepository,
    private val remoteDataSource: RemoteDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _estado = MutableStateFlow<Resource<List<EjercicioDto>>>(Resource.Loading())
    val estado: StateFlow<Resource<List<EjercicioDto>>> = _estado

    private val _selectedEjercicios = mutableStateListOf<EjercicioDto>()
    val selectedEjercicios: List<EjercicioDto> get() = _selectedEjercicios

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

    fun getEjercicios() {
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
                            ejercicios = result.data ?: emptyList(),
                            filtro = ""
                        )
                        filterEjercicios("")
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


    fun obtenerEjercicios() {
        viewModelScope.launch {
            _estado.value = Resource.Loading()
            try {
                val ejercicios = remoteDataSource.getEjercicios()
                _estado.value = Resource.Success(ejercicios)
            } catch (e: Exception) {
                _estado.value = Resource.Error("No se pudo cargar los ejercicios.")
            }
        }
    }

    fun buscarEjercicios(query: String) {
        val currentData = (_estado.value as? Resource.Success)?.data ?: emptyList()
        _estado.value = Resource.Success(
            currentData.filter { it.nombreEjercicio.contains(query, ignoreCase = true) }
        )
    }

    fun filtrarPorParteCuerpo(filtro: String) {
        val currentData = (_estado.value as? Resource.Success)?.data ?: emptyList()
        _estado.value = Resource.Success(
            if (filtro == "Parte del cuerpo") currentData
            else currentData.filter { it.nombreEjercicio.contains(filtro, ignoreCase = true) }
        )
    }
    fun toggleEjercicioSeleccionado(ejercicio: EjercicioDto) {
        val index = _selectedEjercicios.indexOfFirst { it.ejercicioId == ejercicio.ejercicioId }
        if (index != -1) {
            _selectedEjercicios.removeAt(index)
        } else {
            _selectedEjercicios.add(ejercicio)
        }
    }

    fun setRepeticionesYSeries(ejercicio: EjercicioDto, repeticiones: Int, series: Int) {
        val index = _selectedEjercicios.indexOfFirst { it.ejercicioId == ejercicio.ejercicioId }
        if (index != -1) {
            _selectedEjercicios[index] = _selectedEjercicios[index].copy(
                repeticiones = repeticiones,
                series = series
            )
        } else {
            _selectedEjercicios.add(ejercicio.copy(repeticiones = repeticiones, series = series))
        }
    }

    fun saveRepsAndSeries(ejercicioId: Int, repeticiones: Int, series: Int) {
        viewModelScope.launch {
            val result = ejercicioRepository.updateRepsAndSeries(ejercicioId, repeticiones, series)
            if (result > 0) {
                getEjercicios()
            }
        }
    }

    fun filtrarEjercicios(filtro: String) {
        _uiState.value = _uiState.value.copy(filtro = filtro)
        viewModelScope.launch {
            val ejercicios = ejercicioRepository.getEjerciciosLocales()
            ejercicios.collect {
                _uiState.value = _uiState.value.copy(
                    ejercicios = if (filtro == "Parte del cuerpo" || filtro.isEmpty()) it else it.filter { ejercicio ->
                        ejercicio.grupoMuscular?.lowercase()?.contains(filtro.lowercase()) ?: false
                    }
                )
            }
        }
    }
}


