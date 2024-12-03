package edu.ucne.fitgoal.presentation.auth

import android.content.Context
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.repository.AuthRepository
import edu.ucne.fitgoal.presentation.navigation.Screen
import edu.ucne.fitgoal.util.CountdownWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _emailVerified = MutableLiveData<Boolean>()
    val emailVerified: LiveData<Boolean> = _emailVerified

    init {
        _emailVerified.value = false
        checkTimer()
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.NombreChanged -> nombreChanged(event.nombre)
            is AuthEvent.ApellidoChanged -> apellidoChanged(event.apellido)
            is AuthEvent.EmailChanged -> emailchanged(event.email)
            is AuthEvent.PasswordChanged -> passwordchanged(event.password)
            is AuthEvent.VerifyPasswordChanged -> verifyPasswordChanged(event.verifyPassword)
            is AuthEvent.ResetPassword -> resetPassword(event.email, event.goLogin)
            is AuthEvent.SignInWithGoogle -> signInWithGoogle(event.activityContext, event.goHome)
            AuthEvent.SignIn -> iniciarSesion()
            is AuthEvent.SignUp -> registrarUsuario(event.goLogin)
            is AuthEvent.Logout -> logout(event.context, event.goLogin)
            AuthEvent.SendEmailVerification -> enviarEmailVerificacion()
            AuthEvent.IsEmailVerified -> isEmailVerified()
            AuthEvent.StartEmailVerification -> startEmailVerification()
            AuthEvent.StartDestination -> startDestination()
            AuthEvent.ChangePasswordVisibility -> changePasswordVisibility()
            AuthEvent.CloseErrorModal -> closeErrorModal()
        }
    }

    private fun closeErrorModal() {
        _uiState.value = _uiState.value.copy(isModalErrorVisible = false)
        _uiState.value = _uiState.value.copy(error = "")
    }

    fun startDestination(): Screen {
        return if (authRepository.isUserSignedIn() && authRepository.isEmailVerified()) {
            Screen.MainNavHostScreen
        } else if (authRepository.isUserSignedIn() && !authRepository.isEmailVerified()) {
            Screen.VerifyScreen
        } else {
            Screen.LoginScreen
        }
    }

    private fun signInWithGoogle(activityContext: Context, goHome: () -> Unit) =
        viewModelScope.launch {
            authRepository.signInWithGoogle(activityContext).collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            error = "",
                            isLoading = false
                        )
                        goHome()
                    }

                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            error = resource.message ?: "",
                            isLoading = false,
                            isModalErrorVisible = true
                        )
                    }

                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            error = "",
                            isLoading = true
                        )
                    }
                }
            }
        }

    private fun logout(context: Context, goLogin: () -> Unit) = viewModelScope.launch {
        authRepository.signOut(context).collectLatest { resource ->
            when (resource) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        error = "",
                        isLoading = false
                    )
                    goLogin()
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = resource.message ?: "",
                        isLoading = false,
                        isModalErrorVisible = true
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(
                        error = "",
                        isLoading = true
                    )
                }
            }
        }
    }

    private fun startTimerButtonOff() {
        _uiState.value = _uiState.value.copy(isButtonEnabled = false)

        val endTime = System.currentTimeMillis() + 90 * 1000
        val workRequest = OneTimeWorkRequestBuilder<CountdownWorker>()
            .setInputData(workDataOf("endTime" to endTime))
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)

        val sharedPreferences = context.getSharedPreferences("TimerPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putLong("endTime", endTime).apply()

        viewModelScope.launch {
            while (true) {
                val currentTime = System.currentTimeMillis()
                val tiempoRestante = ((endTime - currentTime) / 1000).toInt()
                if (tiempoRestante <= 0) {
                    _uiState.value = _uiState.value.copy(isButtonEnabled = true, tiempo = 0)
                    break
                }
                _uiState.value = _uiState.value.copy(tiempo = tiempoRestante)
                delay(1000)
            }
        }
    }

    private fun checkTimer(){
        val sharedPreferences = context.getSharedPreferences("TimerPrefs", Context.MODE_PRIVATE)
        val endTime = sharedPreferences.getLong("endTime", 0)
        if (endTime > System.currentTimeMillis()) {
            _uiState.value = _uiState.value.copy(isButtonEnabled = false)

            viewModelScope.launch {
                while (true) {
                    val currentTime = System.currentTimeMillis()
                    val tiempoRestante = ((endTime - currentTime) / 1000).toInt()
                    if (tiempoRestante <= 0) {
                        _uiState.value = _uiState.value.copy(isButtonEnabled = true, tiempo = 0)
                        break
                    }
                    _uiState.value = _uiState.value.copy(tiempo = tiempoRestante)
                    delay(1000)
                }
            }
        } else {
            _uiState.value = _uiState.value.copy(isButtonEnabled = true, tiempo = 0)
        }
    }

    private fun enviarEmailVerificacion() = viewModelScope.launch {
        startTimerButtonOff()
        authRepository.sendEmailVerification().collectLatest { resource ->
            when (resource) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        error = "",
                        isLoading = false
                    )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = resource.message ?: "",
                        isLoading = false,
                        isModalErrorVisible = true
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(
                        error = "",
                        isLoading = true
                    )
                }
            }
        }
    }

    private fun startEmailVerification() = viewModelScope.launch {
        while (!authRepository.isEmailVerified()) {
            actualizarUsuario()
            delay(5000)
        }
    }

    private fun actualizarUsuario() = viewModelScope.launch {
        authRepository.updateUser()
        val isVerified = authRepository.isEmailVerified()
        _emailVerified.value = isVerified
    }

    private fun registrarUsuario(goLogin: () -> Unit) = viewModelScope.launch {
        if (validacion()) {
            authRepository.signUp(_uiState.value.toDto(), _uiState.value.password)
                .collectLatest { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _uiState.value = _uiState.value.copy(
                                error = "",
                                isLoading = false
                            )
                            startTimerButtonOff()
                            goLogin()
                        }

                        is Resource.Error -> {
                            _uiState.value = _uiState.value.copy(
                                error = resource.message ?: "",
                                isLoading = false,
                                isModalErrorVisible = true
                            )
                        }

                        is Resource.Loading -> {
                            _uiState.value = _uiState.value.copy(
                                error = "",
                                isLoading = true
                            )
                        }
                    }
                }
        }
    }

    private fun iniciarSesion() = viewModelScope.launch {
        val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$".toRegex()
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9]+\\.[A-Za-z]+$".toRegex()
        if (passwordRegex.matches(_uiState.value.password) && emailRegex.matches(_uiState.value.email)) {
            authRepository.signIn(_uiState.value.email, _uiState.value.password)
                .collectLatest { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _uiState.value = _uiState.value.copy(isAuthorized = true)
                            _uiState.value =
                                _uiState.value.copy(isEmailVerified = authRepository.isEmailVerified())
                            _uiState.value = _uiState.value.copy(
                                error = "",
                                isLoading = false
                            )
                        }

                        is Resource.Error -> {
                            _uiState.value = _uiState.value.copy(
                                error = resource.message ?: "",
                                isLoading = false,
                                isModalErrorVisible = true
                            )
                        }

                        is Resource.Loading -> {
                            _uiState.value = _uiState.value.copy(
                                error = "",
                                isLoading = true
                            )
                        }
                    }
                }
        }
    }

    private fun resetPassword(email: String, goLogin: () -> Unit) = viewModelScope.launch {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9]+\\.[A-Za-z]+$".toRegex()
        if (email.isEmpty() || !emailRegex.matches(email)) {
            _uiState.value = _uiState.value.copy(
                emailError = "Introduce un email",
                isLoading = false
            )
            return@launch
        }
        authRepository.resetPassword(email).collectLatest { resource ->
            when (resource) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        error = "",
                        isLoading = false
                    )
                    goLogin()
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = resource.message ?: "",
                        isLoading = false
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(
                        error = "",
                        isLoading = true
                    )
                }
            }
        }
    }

    private fun isEmailVerified() = authRepository.isEmailVerified()

    private fun nombreChanged(nombre: String) {
        val nombreRegex = "^[A-Za-z]+\$".toRegex()
        _uiState.update {
            it.copy(
                nombre = nombre,
                nombreError = if (nombreRegex.matches(nombre)) "" else "Introduce un nombre valido"
            )
        }

    }

    private fun apellidoChanged(apellido: String) {
        val apellidoRegex = "^[A-Za-z ]+\$".toRegex()
        _uiState.update {
            it.copy(
                apellido = apellido,
                apellidoError = if (apellidoRegex.matches(apellido)) "" else "Introduce un apellido valido"
            )
        }
    }

    private fun emailchanged(email: String) {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9]+\\.[A-Za-z]+$".toRegex()
        _uiState.update {
            it.copy(
                email = email,
                emailError = if (emailRegex.matches(email)) "" else "Introduce un email valido"
            )
        }
    }

    private fun passwordchanged(password: String) {
        val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$".toRegex()
        _uiState.update {
            it.copy(
                password = password,
                passwordError = if (passwordRegex.matches(password)) "" else
                    "La contraseña debe tener al menos 8 caracteres"
            )
        }
    }

    private fun verifyPasswordChanged(verifyPassword: String) {
        _uiState.update {
            it.copy(
                verifyPassword = verifyPassword,
                verifyPasswordError = if (verifyPassword == _uiState.value.password) "" else
                    "Las contraseñas no coinciden"
            )
        }
    }

    private fun validacion(): Boolean {
        val nombreRegex = "^[A-Za-z]+\$".toRegex()
        val apellidoRegex = "^[A-Za-z ]+\$".toRegex()
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9]+\\.[A-Za-z]+$".toRegex()
        val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$".toRegex()
        _uiState.value.apply {
            return nombreRegex.matches(nombre) &&
                    apellidoRegex.matches(apellido) &&
                    emailRegex.matches(email) &&
                    passwordRegex.matches(password) &&
                    password == verifyPassword
        }
    }

    private fun changePasswordVisibility() {
        _uiState.value = _uiState.value.copy(canSeePassword = !_uiState.value.canSeePassword)
        if (_uiState.value.canSeePassword) {
            _uiState.value = _uiState.value.copy(keyboardType = KeyboardType.Text)
        } else {
            _uiState.value = _uiState.value.copy(keyboardType = KeyboardType.Password)
        }
    }
}