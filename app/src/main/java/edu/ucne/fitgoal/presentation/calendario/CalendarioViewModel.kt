package edu.ucne.fitgoal.presentation.calendario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.fitgoal.data.local.dao.CalendarioDao
import edu.ucne.fitgoal.data.local.entities.CalendarioEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class CalendarioViewModel @Inject constructor(
    private val calendarioDao: CalendarioDao
) : ViewModel() {

    private val _selectedDays = MutableStateFlow<Set<Calendar>>(emptySet())
    val selectedDays: StateFlow<Set<Calendar>> get() = _selectedDays

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    init {
        loadSelectedDays()
    }

    private fun loadSelectedDays() {
        viewModelScope.launch {
            val calendarioEntity = calendarioDao.getFirst()
            calendarioEntity?.let {
                val calendarList = it.selectedDates.mapNotNull { date ->
                    try {
                        val calendar = Calendar.getInstance()
                        calendar.time = dateFormat.parse(date)
                        calendar
                    } catch (e: Exception) {
                        Log.e("CalendarioViewModel", "Date parsing error", e)
                        null
                    }
                }
                _selectedDays.value = calendarList.toSet()
            }
        }
    }

    fun toggleDay(calendar: Calendar) {
        val newSelectedDays = _selectedDays.value.toMutableSet()
        if (newSelectedDays.contains(calendar)) {
            newSelectedDays.remove(calendar)
        } else {
            newSelectedDays.add(calendar)
        }
        _selectedDays.value = newSelectedDays
    }

    fun saveSelectedDays() {
        val selectedDates = _selectedDays.value.map { dateFormat.format(it.time) }

        val calendarioEntity = CalendarioEntity(
            id = 1,
            selectedDates = selectedDates,
            usuarioId = 1,
        )

        viewModelScope.launch {
            calendarioDao.update(calendarioEntity)
        }
    }

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> get() = _toastMessage

    fun clearToastMessage() {
        _toastMessage.value = null
    }
}