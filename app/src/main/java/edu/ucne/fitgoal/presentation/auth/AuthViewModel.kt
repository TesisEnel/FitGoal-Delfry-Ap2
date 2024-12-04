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
import edu.ucne.fitgoal.data.remote.dto.ProgresoUsuarioDto
import edu.ucne.fitgoal.data.repository.AuthRepository
import edu.ucne.fitgoal.data.repository.ProgresoUsuarioRepository
import edu.ucne.fitgoal.presentation.navigation.Screen
import edu.ucne.fitgoal.util.CountdownWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val progresoUsuarioRepository: ProgresoUsuarioRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _emailVerified = MutableLiveData<Boolean>()
    val emailVerified: LiveData<Boolean> = _emailVerified

    init {
        _emailVerified.value = false
        if (authRepository.isUserSignedIn()) {
            getUsuario()
        }
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
            AuthEvent.GetUsuario -> getUsuario()
            AuthEvent.UpdateUsuario -> updateUsuario()
            is AuthEvent.AlturaChanged -> alturaChanged(event.altura)
            is AuthEvent.EdadChanged -> edadChanged(event.edad)
            is AuthEvent.PesoIdealChanged -> onPesoIdealChange(event.pesoIdeal)
            is AuthEvent.PesoInicialChanged -> onPesoInicialChange(event.pesoInicial)
        }
    }

    private fun closeErrorModal() {
        _uiState.value = _uiState.value.copy(isModalErrorVisible = false)
        _uiState.value = _uiState.value.copy(error = "")
    }

    fun homeDestination() : Boolean{
        if (_uiState.value.edad > 0) {
            _uiState.value = _uiState.value.copy(isDatosLLenos = true)
            return true
        } else {
            _uiState.value = _uiState.value.copy(
                isDatosLLenos = false,
                esNuevo = true
            )
            return false
        }
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

    private fun signInWithGoogle(activityContext: Context, goHome: () -> Unit = {}) =
        viewModelScope.launch {
            authRepository.signInWithGoogle(activityContext).collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            error = ""
                        )
                        getUsuario(goHome)
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
                                error = ""
                            )
                            getUsuario()
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

    private fun getUsuario(goHome: () -> Unit={}) = viewModelScope.launch {
        authRepository.getUsarioFlow(authRepository.getCurrentUid()!!).collectLatest { resource ->
            when (resource) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        nombre = resource.data?.nombre ?: "",
                        apellido = resource.data?.apellido ?: "",
                        email = resource.data?.correo ?: "",
                        edad = resource.data?.edad ?: 0,
                        altura = resource.data?.altura ?: 0f,
                        pesoInicial = resource.data?.pesoInicial ?: 0f,
                        pesoActual = resource.data?.pesoActual ?: 0f,
                        pesoIdeal = resource.data?.pesoIdeal ?: 0f,
                        update = true,
                        isLoading = false
                    )
                    if (resource.data != null) {
                        val edad = resource.data.edad
                        if (edad > 0) {
                            _uiState.value = _uiState.value.copy(isDatosLLenos = true)
                        } else {
                            _uiState.value = _uiState.value.copy(
                                isDatosLLenos = false,
                                esNuevo = true
                            )
                        }
                    }
                    homeDestination()
                    goHome()
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

    private fun updateUsuario() = viewModelScope.launch {
        if (validateForm()) {
            var usuario = _uiState.value.toUsuarioDto()
            usuario = usuario.copy(usuarioId = authRepository.getCurrentUid()!!)
            authRepository.updateUsuario(usuario).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            error = ""
                        )
                    }

                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            error = ""
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

    private suspend fun createNuevaMeta() {
        val progresoUsuario = ProgresoUsuarioDto(
            usuarioId = authRepository.getCurrentUid()!!,
            fecha = LocalDate.now().toString(),
            peso = _uiState.value.pesoInicial
        )
        progresoUsuarioRepository
            .createProgresoUsuario(progresoUsuario).collect { resource ->
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
                            isLoading = false,
                            isDatosLLenos = true
                        )
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

    private fun edadChanged(edadStr: String) {
        val edad = edadStr.toIntOrNull() ?: 0
        _uiState.value = _uiState.value.copy(
            edad = edad,
            edadError = ""
        )
        if (edad < 5) {
            _uiState.value = _uiState.value.copy(
                edadError = "La edad debe ser mayor a 5 años"
            )
        }
    }

    private fun alturaChanged(alturaStr: String) {
        val altura = alturaStr.toFloatOrNull() ?: 0f
        _uiState.value = _uiState.value.copy(
            altura = altura,
            alturaError = ""
        )
        if (altura !in 4f..8f) {
            _uiState.value =
                _uiState.value.copy(alturaError = "La altura debe ser entre 4 y 8 pies (ft)")
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

    private fun validateForm(): Boolean {
        var isValid = true

        val nombreRegex = "^[A-Za-z]+\$".toRegex()
        if (!nombreRegex.matches(_uiState.value.nombre)) {
            _uiState.value = _uiState.value.copy(nombreError = "Introduce un nombre válido")
            isValid = false
        } else {
            _uiState.value = _uiState.value.copy(nombreError = "")
        }

        val apellidoRegex = "^[A-Za-z ]+\$".toRegex()
        if (!apellidoRegex.matches(_uiState.value.apellido)) {
            _uiState.value = _uiState.value.copy(apellidoError = "Introduce un apellido válido")
            isValid = false
        } else {
            _uiState.value = _uiState.value.copy(apellidoError = "")
        }

        if (_uiState.value.edad < 5) {
            _uiState.value = _uiState.value.copy(edadError = "La edad debe ser mayor a 5 años")
            isValid = false
        } else {
            _uiState.value = _uiState.value.copy(edadError = "")
        }

        if (_uiState.value.altura !in 4f..8f) {
            _uiState.value =
                _uiState.value.copy(alturaError = "La altura debe ser entre 4 y 8 pies (ft)")
            isValid = false
        } else {
            _uiState.value = _uiState.value.copy(alturaError = "")
        }

        if (_uiState.value.pesoInicial !in 60.0..1500.0) {
            _uiState.value =
                _uiState.value.copy(pesoInicialError = "El peso inicial debe ser entre 60 y 1500")
            isValid = false
        } else if (_uiState.value.pesoInicial == _uiState.value.pesoIdeal) {
            _uiState.value =
                _uiState.value.copy(pesoInicialError = "El peso inicial debe ser diferente al peso ideal")
            isValid = false
        } else {
            _uiState.value = _uiState.value.copy(pesoInicialError = "")
        }

        if (_uiState.value.pesoIdeal !in 60.0..1500.0) {
            _uiState.value =
                _uiState.value.copy(pesoIdealError = "El peso ideal debe ser entre 60 y 1500")
            isValid = false
        } else if (_uiState.value.pesoIdeal == _uiState.value.pesoInicial) {
            _uiState.value =
                _uiState.value.copy(pesoIdealError = "El peso ideal debe ser diferente al peso inicial")
            isValid = false
        } else {
            _uiState.value = _uiState.value.copy(pesoIdealError = "")
        }

        return isValid
    }
}