package edu.ucne.fitgoal.presentation.calendario

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import edu.ucne.fitgoal.R
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarioScreen(
    viewModel: CalendarioViewModel = hiltViewModel(),
    goToPlanificador: () -> Unit
) {
    val context = LocalContext.current
    val selectedDays = viewModel.selectedDays.collectAsState().value
    val toastMessage = viewModel.toastMessage.collectAsState().value

    toastMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        viewModel.clearToastMessage()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calendario") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF388E3C),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            val calendarView = remember { com.applandeo.materialcalendarview.CalendarView(context) }

            AndroidView(
                factory = {
                    calendarView.apply {
                        setDate(Calendar.getInstance())
                        setOnDayClickListener(object : OnDayClickListener {
                            override fun onDayClick(eventDay: EventDay) {
                                val calendar = eventDay.calendar
                                viewModel.toggleDay(calendar)
                            }
                        })
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
            )
            SaveButton(
                onSave = {
                    viewModel.saveSelectedDays()
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            )

            LaunchedEffect(selectedDays) {
                updateEvents(calendarView, selectedDays)
            }
        }
    }
}

@Composable
fun SaveButton(onSave: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onSave,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF81C784)
        ),
        modifier = modifier
            .fillMaxWidth(0.6f)
    ) {
        Text("Guardar")
    }
}

private fun updateEvents(calendarView: com.applandeo.materialcalendarview.CalendarView, selectedDays: Set<Calendar>) {
    val eventDays = selectedDays.map { selectedDay ->
        EventDay(selectedDay, R.drawable.circle)
    }
    calendarView.setEvents(eventDays)
}