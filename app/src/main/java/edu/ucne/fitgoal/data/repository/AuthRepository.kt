package edu.ucne.fitgoal.data.repository

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import edu.ucne.fitgoal.data.local.dao.UsuarioDao
import edu.ucne.fitgoal.data.local.entities.UsuarioEntity
import edu.ucne.fitgoal.data.remote.RemoteDataSource
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.UsuarioDto
import edu.ucne.fitgoal.data.remote.dto.toEntity
import edu.ucne.fitgoal.util.Constants.ID_WEB_CLIENT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val remoteDataSource: RemoteDataSource,
    private val usuarioDao: UsuarioDao
) {
    private lateinit var credentialManager: CredentialManager

    fun signIn(email: String, password: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error("Error al iniciar sesión"))
            }
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            emit(Resource.Error("Las credenciales son incorrectas"))
        }
    }

    fun signUp(usuarioDto: UsuarioDto, password: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val result =
                firebaseAuth.createUserWithEmailAndPassword(usuarioDto.correo, password).await()
            val user = result.user
            if (user != null) {
                val updatedUsuarioDto = usuarioDto.copy(usuarioId = user.uid)
                val usuario = remoteDataSource.postUsuario(updatedUsuarioDto)
                usuarioDao.save(usuario.toEntity())
                sendEmailVerification().last()
                firebaseAuth.signOut()
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error("Error al crear usuario"))
            }
        } catch (e: FirebaseAuthUserCollisionException) {
            emit(Resource.Error("Ya existe una cuenta con este correo"))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexion $errorMessage"))
        } catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
    }

    fun signInWithGoogle(activityContext: Context): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            credentialManager = CredentialManager.create(activityContext)
            val result = buildCredentialRequest(activityContext)
            val success = handleSignIn(result)
            if (success) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error("Error al iniciar sesión con Google"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }

    private suspend fun handleSignIn(result: GetCredentialResponse): Boolean {
        val credential = result.credential

        if (
            credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            try {
                val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val authCredential = GoogleAuthProvider.getCredential(
                    tokenCredential.idToken, null
                )
                val authResult = firebaseAuth.signInWithCredential(authCredential).await()
                val userDb = getUsuario(authResult.user?.uid!!)
                if (userDb.usuarioId.isEmpty()) {
                    val usuarioDto = UsuarioDto(
                        usuarioId = authResult.user?.uid,
                        nombre = authResult.user?.displayName ?: "",
                        correo = authResult.user?.email ?: ""
                    )
                    remoteDataSource.postUsuario(usuarioDto)
                    usuarioDao.save(usuarioDto.toEntity())
                }
                return authResult.user != null
            } catch (e: GoogleIdTokenParsingException) {
                return false
            }
        } else {
            return false
        }
    }

    private suspend fun buildCredentialRequest(activityContext: Context): GetCredentialResponse {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(ID_WEB_CLIENT)
                    .setAutoSelectEnabled(false)
                    .build()
            ).build()
        return credentialManager.getCredential(
            request = request, context = activityContext
        )
    }

    fun signOut(activityContext: Context): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            credentialManager = CredentialManager.create(activityContext)
            credentialManager.clearCredentialState(
                ClearCredentialStateRequest()
            )
            firebaseAuth.signOut()
            emit(Resource.Success(true))
        } catch (e: CancellationException) {
            emit(Resource.Error("Error al cerrar sesión"))
        }
    }

    fun sendEmailVerification(): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            firebaseAuth.currentUser?.sendEmailVerification()?.await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }

    suspend fun updateUser() {
        try {
            firebaseAuth.currentUser?.reload()?.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun resetPassword(email: String): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            firebaseAuth.sendPasswordResetEmail(email).await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }

    suspend fun getUsuario(id: String): UsuarioEntity {
        try {
            val usuario = remoteDataSource.getUsuario(id)
            usuarioDao.save(usuario.toEntity())
            return usuarioDao.find(id)!!
        } catch (e: Exception) {
            e.printStackTrace()
            return UsuarioEntity()
        }
    }

    fun isEmailVerified(): Boolean = firebaseAuth.currentUser?.isEmailVerified ?: false

    fun isUserSignedIn() = firebaseAuth.currentUser != null

    fun getCurrentUid() = firebaseAuth.currentUser?.uid
}