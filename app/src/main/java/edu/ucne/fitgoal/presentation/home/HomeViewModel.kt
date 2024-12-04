package edu.ucne.fitgoal.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.repository.TipRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val tipRepository: TipRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        onEvent(HomeEvent.FetchTips)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.FetchTips -> fetchTips()
            is HomeEvent.CloseErrorModal -> closeErrorModal()
        }
    }

    private fun fetchTips() {
        viewModelScope.launch {
            tipRepository.getTips().collect { resource ->
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
                            isLoading = false,
                            error = ""
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
    }

    private fun closeErrorModal() {
        _uiState.value = _uiState.value.copy(isModalErrorVisible = false, error = "")
    }
}