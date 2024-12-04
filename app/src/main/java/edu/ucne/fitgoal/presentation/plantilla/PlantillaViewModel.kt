package edu.ucne.fitgoal.presentation.plantilla

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.fitgoal.data.local.dao.PlantillaDao
import edu.ucne.fitgoal.data.local.entities.EjercicioEntity

import edu.ucne.fitgoal.data.local.entities.EjerciciosPlantillasEntity
import edu.ucne.fitgoal.data.local.entities.PlantillaEntity
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.EjercicioDto
import edu.ucne.fitgoal.data.remote.dto.PlantillaDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantillaViewModel @Inject constructor(
    private val plantillaDao: PlantillaDao
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<List<PlantillaDto>>>(Resource.Loading())
    val uiState: StateFlow<Resource<List<PlantillaDto>>> = _uiState
    private val _allPlantillas = MutableStateFlow<List<PlantillaDto>>(emptyList())

    init {
        cargarPlantillasConEjercicios()
    }

    private fun cargarPlantillas() {
        viewModelScope.launch {
            _uiState.value = Resource.Loading()
            try {
                val plantillasLocal = plantillaDao.getAllPlantillas()

                if (plantillasLocal.isNotEmpty()) {
                    val plantillasDto = plantillasLocal.map { plantillaEntity ->
                        val ejercicios =
                            plantillaDao.getEjerciciosByPlantillaId(plantillaEntity.plantillaId)
                        plantillaEntity.toDto(ejercicios)
                    }
                    _allPlantillas.value = plantillasDto
                    _uiState.value = Resource.Success(plantillasDto)
                }
            } catch (e: Exception) {
                _uiState.value =
                    Resource.Error("Error al obtener las plantillas locales: ${e.message}")
            }
        }
    }

    private fun cargarPlantillasConEjercicios() {
        viewModelScope.launch {
            _uiState.value = Resource.Loading()
            try {
                val plantillas = plantillaDao.getAllPlantillas()

                val plantillasDto = plantillas.map { plantillaEntity ->
                    val ejercicios =
                        plantillaDao.getEjerciciosByPlantillaId(plantillaEntity.plantillaId)
                    val ejerciciosConPlantillaId =
                        ejercicios.map { it.copy(plantillaId = plantillaEntity.plantillaId) }
                    plantillaEntity.toDto(ejerciciosConPlantillaId)
                }
                _allPlantillas.value = plantillasDto
                _uiState.value = Resource.Success(plantillasDto)
            } catch (e: Exception) {
                _uiState.value = Resource.Error("Error al obtener las plantillas: ${e.message}")
            }
        }
    }

    fun crearPlantilla(
        nombre: String,
        descripcion: String,
        ejercicios: List<EjercicioEntity>,
        duracionTotal: String
    ) {
        viewModelScope.launch {
            try {
                val nuevaPlantilla = PlantillaEntity(
                    nombre = nombre,
                    descripcion = descripcion,
                    ejercicios = ejercicios,
                    duracionTotal = duracionTotal
                )

                val plantillaId = plantillaDao.insertPlantilla(nuevaPlantilla)
                val ejerciciosConPlantillaId = ejercicios.map { ejercicio ->
                    ejercicio.copy(plantillaId = plantillaId.toInt())
                }

                plantillaDao.insertEjercicios(ejerciciosConPlantillaId)
                val relaciones = ejerciciosConPlantillaId.map { ejercicio ->
                    EjerciciosPlantillasEntity(
                        plantillaId = plantillaId.toInt(),
                        ejercicioId = ejercicio.ejercicioId,
                    )
                }
                cargarPlantillas()
                cargarPlantillasConEjercicios()

                plantillaDao.insertEjerciciosPlantillas(relaciones)

            } catch (e: Exception) {
                Log.e("PlantillaViewModel", "Error al crear la plantilla: ${e.message}", e)
            }
        }
    }

    fun eliminarPlantilla(plantillaId: Int) {
        viewModelScope.launch {
            try {
                plantillaDao.deletePlantillaById(plantillaId)
                plantillaDao.deleteEjerciciosFromPlantilla(plantillaId)
                plantillaDao.deleteEjerciciosByPlantillaId(plantillaId)
                cargarPlantillasConEjercicios()
                cargarPlantillas()
                _uiState.value = Resource.Success(_allPlantillas.value)
            } catch (e: Exception) {
                _uiState.value = Resource.Error("Error al eliminar la plantilla: ${e.message}")
            }
        }
    }

    fun formatToMMSS(input: String): Pair<String, Boolean> {
        val sanitizedInput = input.filter { it.isDigit() }

        if (sanitizedInput.length > 4) {
            return Pair(input, true)
        }

        val seconds = sanitizedInput.toIntOrNull() ?: 0
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        val formattedInput = String.format("%02d:%02d", minutes, remainingSeconds)

        return Pair(formattedInput, false)
    }
}

fun PlantillaEntity.toDto(ejercicios: List<EjercicioEntity>): PlantillaDto {
    return PlantillaDto(
        plantillaId = plantillaId,
        nombrePlantilla = nombre,
        descripcionPlantilla = descripcion,
        ejercicios = ejercicios
    )
}

fun EjercicioDto.toEntity(): EjercicioEntity {
    return EjercicioEntity(
        ejercicioId = this.ejercicioId!!,
        nombreEjercicio = this.nombreEjercicio,
        descripcion = this.descripcion,
        foto = this.foto,
        grupoMuscular = this.grupoMuscular,
        plantillaId = this.plantillaId,
        repeticiones = this.repeticiones,
        series = this.series,
        duracionEjercicio = this.duracionEjercicio.toString()

    )
}