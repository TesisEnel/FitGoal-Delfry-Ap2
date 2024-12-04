package edu.ucne.fitgoal.presentation.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.repository.PerfilRepository
import edu.ucne.fitgoal.presentation.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val perfilRepository: PerfilRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState

    init {
        fetchUserData()
    }

    private fun fetchUserData() {
        val currentUser: FirebaseUser? = firebaseAuth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val email = currentUser.email ?: ""
            val displayName = currentUser.displayName ?: ""
            val photoUrl = currentUser.photoUrl

            _uiState.update { state ->
                state.copy(
                    nombre = displayName,
                    correo = email,
                    photoUrl = photoUrl.toString()
                )
            }

            getUsuario(userId)
        } else {
            _uiState.update { state ->
                state.copy(error = "Usuario no autenticado")
            }
        }
    }

    private fun getUsuario(userId: String) {
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
                                    nombre = usuario.nombre,
                                    correo = usuario.correo,
                                    edad = usuario.edad,
                                    altura = usuario.altura,
                                    pesoInicial = usuario.pesoInicial,
                                    pesoActual = usuario.pesoActual,
                                    pesoIdeal = usuario.pesoIdeal,
                                    aguaDiaria = usuario.aguaDiaria,
                                    isLoading = false,
                                    error = ""
                                )
                            }
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = resource.message ?: "Error desconocido"
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: PerfilEvent, navController: NavController) {
        when (event) {
            is PerfilEvent.GetPerfil -> fetchUserData()
            is PerfilEvent.NavigateToEditarPerfil -> navigateToEditarPerfil(navController)
            is PerfilEvent.NavigateToProgresoSemanal -> {}
            is PerfilEvent.Logout -> logout()
            is PerfilEvent.CloseErrorModal -> closeErrorModal()
        }
    }

    private fun navigateToEditarPerfil(navController: NavController) {
        navController.navigate(Screen.EditarPerfilScreen)
    }

    private fun logout() {
        firebaseAuth.signOut()
    }

    private fun closeErrorModal() {
        _uiState.update { it.copy(isModalErrorVisible = false, error = "") }
    }
}