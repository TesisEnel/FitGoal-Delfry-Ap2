package edu.ucne.fitgoal.data.repository


import edu.ucne.fitgoal.data.local.dao.PlantillaDao
import edu.ucne.fitgoal.data.remote.RemoteDataSource
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.PlantillaDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class PlantillaRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val plantillaDao: PlantillaDao
) {
    fun getPlantillasConEjercicios(): Flow<Resource<List<PlantillaDto>>> = flow {
        try {
            emit(Resource.Loading())
            val plantillas = remoteDataSource.getPlantillas()
            emit(Resource.Success(plantillas))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexion $errorMessage"))
        } catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
    }

    suspend fun eliminarPlantillaById(plantillaId: Int) {
        plantillaDao.deletePlantillaById(plantillaId)
    }
}