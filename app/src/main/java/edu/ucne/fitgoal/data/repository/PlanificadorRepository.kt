package edu.ucne.fitgoal.data.repository


import edu.ucne.fitgoal.data.remote.RemoteDataSource
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.PlanificadorDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class PlanificadorRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
){
    fun getPlanificador(): Flow<Resource<List<PlanificadorDto>>> = flow {
        try {
            emit(Resource.Loading())
            val planificador = remoteDataSource.getPlanificador()
            emit(Resource.Success(planificador))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexion $errorMessage"))
        } catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
    }
}