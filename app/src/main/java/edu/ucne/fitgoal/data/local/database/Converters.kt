package edu.ucne.fitgoal.data.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.ucne.fitgoal.data.local.entities.EjercicioEntity


class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromEjerciciosList(value: List<EjercicioEntity>): String {
        val gson = Gson()
        val type = object : TypeToken<List<EjercicioEntity>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toEjerciciosList(value: String): List<EjercicioEntity> {
        val gson = Gson()
        val type = object : TypeToken<List<EjercicioEntity>>() {}.type
        return gson.fromJson(value, type)
    }
}