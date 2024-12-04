package edu.ucne.fitgoal.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.repository.AuthRepository
import edu.ucne.fitgoal.data.repository.ProgresoUsuarioRepository
import edu.ucne.fitgoal.data.repository.TipRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val tipRepository: TipRepository,
    private val progresoUsuarioRepository: ProgresoUsuarioRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchTips()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.UpdateProgreso -> updateProgreso()
            is HomeEvent.CloseErrorModal -> closeErrorModal()
            HomeEvent.OpenNuevaMeta -> OpenNuevaMeta()
            HomeEvent.OpenRegistrarProgreso -> OpenRegistrarProgreso()
            is HomeEvent.PesoChanged -> onPesoChange(event.peso)
            is HomeEvent.PesoIdealChanged -> onPesoIdealChange(event.pesoIdeal)
            is HomeEvent.PesoInicialChanged -> onPesoInicialChange(event.pesoInicial)
            HomeEvent.SaveProgreso -> save()
            HomeEvent.CloseModal -> closeModal()
            HomeEvent.Update -> update()
        }
    }

    private fun save() {
        if (_uiState.value.esRegistrarProgreso) {
            _uiState.value = _uiState.value.copy(
                usuario = _uiState.value.usuario!!.copy(
                    pesoActual = _uiState.value.peso
                )
            )
            createProgresoUsuario()
        } else if (_uiState.value.esNuevaMeta) {
            _uiState.value = _uiState.value.copy(
                usuario = _uiState.value.usuario!!.copy(
                    pesoActual = _uiState.value.pesoInicial,
                    pesoInicial = _uiState.value.pesoInicial,
                    pesoIdeal = _uiState.value.pesoIdeal,
                ),
                peso = _uiState.value.pesoInicial
            )
            deleteProgresoUsuario()
        }
    }

    private fun update() {
        viewModelScope.launch {
            getProgresoUsuario()
            getUsuario()
        }
    }

    private fun OpenNuevaMeta() {
        _uiState.value = _uiState.value.copy(
            pesoInicial = 0f,
            pesoIdeal = 0f,
            pesoActual = 0f,
            esNuevaMeta = true,
            isModalVisible = true
        )
    }

    private fun OpenRegistrarProgreso() {
        _uiState.value = _uiState.value.copy(
            esRegistrarProgreso = true,
            isModalVisible = true
        )
    }

    private fun closeModal() {
        updateProgreso()
        _uiState.value = _uiState.value.copy(
            isModalVisible = false,
            esRegistrarProgreso = false,
            esNuevaMeta = false
        )
    }

    private fun updateProgreso() {
        _uiState.value = _uiState.value.copy(
            pesoInicial = _uiState.value.usuario?.pesoInicial ?: 0f,
            pesoActual = _uiState.value.usuario?.pesoActual ?: 0f,
            pesoIdeal = _uiState.value.usuario?.pesoIdeal ?: 0f,
            peso = _uiState.value.ultimosProgresos.lastOrNull()?.peso ?: 0f
        )
        _uiState.value = _uiState.value.copy(
            porcentaje = calcularPorcentajeProgreso(
                pesoInicial = _uiState.value.pesoInicial,
                pesoActual = _uiState.value.ultimosProgresos.lastOrNull()?.peso ?: 0f,
                pesoIdeal = _uiState.value.pesoIdeal
            )
        )
    }

    private fun fetchTips() = viewModelScope.launch {
        tipRepository.getTips().collectLatest { resource ->
            when (resource) {
                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }

                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        tips = resource.data ?: emptyList(),
                        error = "",
                        isLoading = false,
                        isModalErrorVisible = false
                    )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = resource.message ?: "Error desconocido",
                        isModalErrorVisible = true
                    )
                }
            }
        }
    }

    private fun closeErrorModal() {
        _uiState.value = _uiState.value.copy(isModalErrorVisible = false, error = "")
    }

    private fun getUsuario() = viewModelScope.launch {
        authRepository.getUsarioFlow(authRepository.getCurrentUid()!!).collectLatest { resource ->
            when (resource) {
                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }

                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        usuario = resource.data,
                        error = "",
                        pesoInicial = resource.data?.pesoInicial ?: 0f,
                        pesoActual = resource.data?.pesoActual ?: 0f,
                        pesoIdeal = resource.data?.pesoIdeal ?: 0f,
                        usuarioId = resource.data?.usuarioId ?: "",
                        isLoading = false,
                        isModalErrorVisible = false
                    )
                    updateProgreso()
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = resource.message ?: "",
                        isModalErrorVisible = true
                    )
                }
            }
        }
    }

    private fun getProgresoUsuario() = viewModelScope.launch {
        progresoUsuarioRepository.getProgresosUsuario(authRepository.getCurrentUid()!!)
            .collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            error = ""
                        )
                    }

                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            progresos = resource.data ?: emptyList(),
                            error = "",
                            peso = resource.data?.last()?.peso ?: 0f,
                            ultimosProgresos = resource.data?.takeLast(4) ?: emptyList(),
                            canPutProgress = hanPasadoSieteDias(resource.data?.last()?.fecha!!),
                            isLoading = false
                        )
                        updateProgreso()
                    }

                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = resource.message ?: "",
                            isModalErrorVisible = true
                        )
                    }
                }
            }
    }

    private fun deleteProgresoUsuario() = viewModelScope.launch {
        if (validateNuevaMeta()) {
            progresoUsuarioRepository.deleteProgresosUsuario(authRepository.getCurrentUid()!!)
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = true,
                                error = "",
                                isModalVisible = false,
                                esNuevaMeta = false
                            )
                        }

                        is Resource.Success -> {
                            _uiState.value = _uiState.value.copy(
                                error = "",
                                isModalErrorVisible = false
                            )
                            createNuevaMeta()
                        }

                        is Resource.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = resource.message ?: "",
                                isModalErrorVisible = true
                            )
                        }
                    }
                }
        }
    }

    private fun createProgresoUsuario() = viewModelScope.launch {
        if (validateRegistrarProgreso()) {
            progresoUsuarioRepository
                .createProgresoUsuario(_uiState.value.toDto()).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = true,
                                error = "",
                                isModalVisible = false,
                                esRegistrarProgreso = false
                            )
                        }

                        is Resource.Success -> {
                            _uiState.value = _uiState.value.copy(
                                error = "",
                                isModalErrorVisible = false
                            )
                            updateUsuario()
                        }

                        is Resource.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = resource.message ?: "",
                                isModalErrorVisible = true
                            )
                        }
                    }
                }
        }
    }

    private suspend fun createNuevaMeta() {
        progresoUsuarioRepository
            .createProgresoUsuario(_uiState.value.toDto()).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            error = ""
                        )
                    }

                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            error = "",
                            isModalErrorVisible = false
                        )
                        updateUsuario()
                    }

                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = resource.message ?: "",
                            isModalErrorVisible = true
                        )
                    }
                }
            }
    }

    private suspend fun updateUsuario() {
        authRepository.updateUsuario(_uiState.value.usuario!!.toDto()).collect { resource ->
            when (resource) {
                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = true,
                        error = ""
                    )
                }

                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        error = "",
                        isModalErrorVisible = false
                    )
                    update()
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = resource.message ?: "",
                        isModalErrorVisible = true
                    )
                }
            }
        }
    }

    private fun calcularPorcentajeProgreso(
        pesoInicial: Float,
        pesoActual: Float,
        pesoIdeal: Float
    ): Float {
        val distanciaTotal = pesoIdeal - pesoInicial
        val progresoActual = pesoActual - pesoInicial

        if (distanciaTotal == 0f) return 100f
        return (progresoActual / distanciaTotal * 100).coerceIn(0f, 100f)
    }

    private fun onPesoChange(pesoStr: String) {
        val peso = pesoStr.toFloatOrNull() ?: 0f
        _uiState.value = _uiState.value.copy(peso = peso, pesoError = "")
        if (peso !in 60.0..1500.0) {
            _uiState.value = _uiState.value.copy(pesoError = "El peso debe ser entre 60 y 1500")
        }

    }

    private fun onPesoIdealChange(pesoStr: String) {
        val pesoIdeal = pesoStr.toFloatOrNull() ?: 0f
        _uiState.value =
            _uiState.value.copy(pesoIdeal = pesoIdeal, pesoIdealError = "", pesoInicialError = "")
        if (pesoIdeal == _uiState.value.pesoInicial) {
            _uiState.value =
                _uiState.value.copy(pesoIdealError = "El peso ideal debe ser diferente al peso actual")
        } else if (pesoIdeal !in 60.0..1500.0) {
            _uiState.value =
                _uiState.value.copy(pesoIdealError = "El peso ideal debe ser entre 60 y 1500")
        }
    }

    private fun onPesoInicialChange(pesoStr: String) {
        val pesoInicial = pesoStr.toFloatOrNull() ?: 0f
        _uiState.value = _uiState.value.copy(
            pesoInicial = pesoInicial,
            pesoActual = pesoInicial,
            pesoInicialError = "",
            pesoIdealError = ""
        )
        if (pesoInicial == _uiState.value.pesoIdeal) {
            _uiState.value =
                _uiState.value.copy(pesoInicialError = "El peso inicial debe ser diferente al peso ideal")
        } else if (pesoInicial !in 60.0..1500.0) {
            _uiState.value =
                _uiState.value.copy(pesoInicialError = "El peso inicial debe ser entre 60 y 1500")
        }
    }

    private fun validateRegistrarProgreso(): Boolean {
        var isValid = true
        if (_uiState.value.peso < 60 || _uiState.value.peso > 1500) {
            _uiState.value = _uiState.value.copy(pesoError = "El peso debe ser entre 60 y 1500")
            isValid = false
        }
        return isValid
    }

    private fun validateNuevaMeta(): Boolean {
        var isValid = true
        if (_uiState.value.pesoInicial < 60 || _uiState.value.pesoInicial > 1500) {
            _uiState.value =
                _uiState.value.copy(pesoInicialError = "El peso inicial debe ser entre 60 y 1500")
            isValid = false
        }
        if (_uiState.value.pesoIdeal < 60 || _uiState.value.pesoIdeal > 1500) {
            _uiState.value =
                _uiState.value.copy(pesoIdealError = "El peso ideal debe ser entre 60 y 1500")
            isValid = false
        }
        if (_uiState.value.pesoIdeal == _uiState.value.pesoInicial) {
            _uiState.value =
                _uiState.value.copy(pesoIdealError = "El peso ideal debe ser diferente al peso inicial")
            isValid = false
        }
        return isValid
    }
   private fun hanPasadoSieteDias(fechaString: String): Boolean {
       val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
       val fecha = LocalDate.parse(fechaString, formatter)
       val fechaActual = LocalDate.now()
       val diasDiferencia = ChronoUnit.DAYS.between(fecha, fechaActual)
       return diasDiferencia >= 7
   }
}