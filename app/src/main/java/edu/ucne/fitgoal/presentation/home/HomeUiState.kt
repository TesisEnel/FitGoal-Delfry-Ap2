package edu.ucne.fitgoal.presentation.home

import edu.ucne.fitgoal.data.local.entities.TipEntity

data class HomeUiState(
    val tips: List<TipEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val isModalErrorVisible: Boolean = false
)