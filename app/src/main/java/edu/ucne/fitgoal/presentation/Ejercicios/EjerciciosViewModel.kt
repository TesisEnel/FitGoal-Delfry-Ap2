package edu.ucne.fitgoal.presentation.Ejercicios

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.fitgoal.data.remote.RemoteDataSource
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.EjerciciosDto
import edu.ucne.fitgoal.data.repository.EjerciciosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EjercicioViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val ejerciciosRepository: EjerciciosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<List<EjerciciosDto>>>(Resource.Loading())
    val uiState: StateFlow<Resource<List<EjerciciosDto>>> = _uiState

    private val _selectedEjercicios = mutableStateListOf<EjerciciosDto>()
    val selectedEjercicios: List<EjerciciosDto> get() = _selectedEjercicios

    fun obtenerEjercicios() {
        viewModelScope.launch {
            _uiState.value = Resource.Loading()
            try {
                val ejercicios = remoteDataSource.getEjercicios()
                _uiState.value = Resource.Success(ejercicios)
            } catch (e: Exception) {
                _uiState.value = Resource.Error("No se pudo cargar los ejercicios.")
            }
        }
    }

    fun buscarEjercicios(query: String) {
        val currentData = (_uiState.value as? Resource.Success)?.data ?: emptyList()
        _uiState.value = Resource.Success(
            currentData.filter { it.nombreEjercicio.contains(query, ignoreCase = true) }
        )
    }

    fun filtrarPorParteCuerpo(filtro: String) {
        val currentData = (_uiState.value as? Resource.Success)?.data ?: emptyList()
        _uiState.value = Resource.Success(
            if (filtro == "Parte del cuerpo") currentData
            else currentData.filter { it.nombreEjercicio.contains(filtro, ignoreCase = true) }
        )
    }
    fun toggleEjercicioSeleccionado(ejercicio: EjerciciosDto) {
        val index = _selectedEjercicios.indexOfFirst { it.ejercicioId == ejercicio.ejercicioId }
        if (index != -1) {
            _selectedEjercicios.removeAt(index)
        } else {
            _selectedEjercicios.add(ejercicio)
        }
    }

    fun setRepeticionesYSeries(ejercicio: EjerciciosDto, repeticiones: Int, series: Int) {
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
            val result = ejerciciosRepository.updateRepsAndSeries(ejercicioId, repeticiones, series)
            if (result > 0) {
                obtenerEjercicios()
            }
        }
    }
}