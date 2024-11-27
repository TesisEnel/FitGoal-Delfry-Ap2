package edu.ucne.fitgoal.presentation.aguadiaria

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.repository.AuthRepository
import edu.ucne.fitgoal.data.repository.HoraioBebidaRepository
import edu.ucne.fitgoal.util.cancelAllReminders
import edu.ucne.fitgoal.util.getTimeInMillis
import edu.ucne.fitgoal.util.scheduleReminder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HorarioBebidaViewModel @Inject constructor(
    private val horarioBebidaRepository: HoraioBebidaRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(usuarioId = authRepository.getCurrentUid()!!)
    }

    fun onEvent(event: HorarioBebidaEvent) {
        when (event) {
            is HorarioBebidaEvent.CantidadChange -> onCantidadChange(event.cantidad)
            HorarioBebidaEvent.CloseModal -> closeModal()
            HorarioBebidaEvent.OpenModal -> openModal()
            is HorarioBebidaEvent.DeleteHorarioBebida -> deleteHorarioBebida(event.id)
            is HorarioBebidaEvent.HoraChange -> onHoraChange(event.hora)
            HorarioBebidaEvent.SaveHorarioBebida -> save()
            is HorarioBebidaEvent.SelectHorariosBebida -> selectHorarioBebida(event.id)
            HorarioBebidaEvent.CloseModalError -> closeModalError()
            is HorarioBebidaEvent.GetHorarioBebidas -> getHorarioBebidas(authRepository.getCurrentUid()!!, event.context)
        }
    }

    private fun getHorarioBebidas(id: String, context: Context) {
        viewModelScope.launch {
            horarioBebidaRepository.getHorarioBebidas(id).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            horarios = result.data ?: emptyList(),
                            error = ""
                        )
                        scheduleAllReminders(context)
                    }

                    is Resource.Error -> {
                        _uiState.value =
                            _uiState.value.copy(isLoading = false, error = result.message ?: "")
                    }
                }
            }
        }
    }

    private fun selectHorarioBebida(horarioBebidaId: Int) {
        viewModelScope.launch {
            horarioBebidaRepository.getHorarioBebida(horarioBebidaId).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            cantidad = result.data?.cantidad ?: 0f,
                            hora = result.data?.hora ?: "",
                            horarioBebidaId = result.data?.horarioBebidaId ?: 0,
                            usuarioId = result.data?.usuarioId ?: "",
                            isModalVisible = true,
                            error = ""
                        )
                    }

                    is Resource.Error -> {
                        _uiState.value =
                            _uiState.value.copy(isLoading = false, error = result.message ?: "")
                    }
                }
            }

        }
    }

    private fun saveHorarioBebida() = viewModelScope.launch {
        val horario = _uiState.value.toDto()
        horarioBebidaRepository.postHorarioBebida(horario).collectLatest { result ->
            when (result) {
                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }

                is Resource.Success -> {
                    _uiState.value =
                        _uiState.value.copy(isModalVisible = false, update = true, error = "")
                }

                is Resource.Error -> {
                    _uiState.value =
                        _uiState.value.copy(isLoading = false, error = result.message ?: "")
                }
            }
        }
    }

    private fun updateHorarioBebida() {
        viewModelScope.launch {
            val horarioBebida = _uiState.value.toDto()
            if (horarioBebida.horarioBebidaId > 0) {
                horarioBebidaRepository.putHorarioBebida(
                    _uiState.value.horarioBebidaId,
                    horarioBebida
                )
                    .collectLatest { result ->
                        when (result) {
                            is Resource.Loading -> {
                                _uiState.value =
                                    _uiState.value.copy(isLoading = true, isModalVisible = false)
                            }

                            is Resource.Success -> {
                                _uiState.value = _uiState.value.copy(error = "", update = true)
                            }

                            is Resource.Error -> {
                                _uiState.value =
                                    _uiState.value.copy(
                                        isLoading = false,
                                        error = result.message ?: ""
                                    )
                            }
                        }
                    }
            }
        }
    }

    private fun save() {
        if (validate()) {
            if (_uiState.value.horarioBebidaId > 0) {
                updateHorarioBebida()
            } else {
                saveHorarioBebida()
            }
            closeModal()
        }
    }

    private fun deleteHorarioBebida(id: Int) {
        viewModelScope.launch {
            horarioBebidaRepository.deleteHorarioBebida(id).let { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }

                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            update = true
                        )

                    }

                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.message ?: ""
                        )
                    }
                }
            }
        }
    }

    private fun closeModal() {
        _uiState.value = _uiState.value.copy(isModalVisible = false)
    }

    private fun closeModalError() {
        _uiState.value = _uiState.value.copy(error = "")
    }

    private fun openModal() {
        _uiState.value = _uiState.value.copy(
            isModalVisible = true,
            cantidad = 0f,
            hora = "",
            horarioBebidaId = 0,
        )
    }

    private fun validate(): Boolean {
        var isValid = true
        if (_uiState.value.cantidad <= 100) {
            _uiState.value =
                _uiState.value.copy(cantidadError = "La cantidad debe ser mayor a 100Ml")
            isValid = false
        }
        if (_uiState.value.hora.isEmpty()) {
            _uiState.value = _uiState.value.copy(horaError = "La hora es requerida")
            isValid = false
        }
        return isValid
    }

    private fun onCantidadChange(cantidad: String) {
        val regex = Regex("[0-9]{0,5}\\.?[0-9]{0,2}")
        val cantidadFloat = cantidad.toFloatOrNull() ?: 0f
        if (regex.matches(cantidad)) {
            _uiState.value = _uiState.value.copy(cantidad = cantidadFloat, cantidadError = "")
        } else {
            _uiState.value =
                _uiState.value.copy(cantidadError = "La cantidad debe ser un intervalo de 100-1000Ml")
        }
    }

    private fun onHoraChange(hora: String) {
        _uiState.value = _uiState.value.copy(hora = hora, horaError = "")
    }

    private fun scheduleAllReminders(context: Context) {
        cancelAllReminders(context)
        _uiState.value.horarios.forEach { horario ->
            val (hour, minute) = horario.hora.split(":").map { it.toIntOrNull() ?: 0 }
            val timeInMillis = getTimeInMillis(hour, minute)
            scheduleReminder(
                context,
                timeInMillis,
                "¡Hora de hidratarte!",
                "Toma ${horario.cantidad.toInt()} ml de agua"
            )
        }

    }


}