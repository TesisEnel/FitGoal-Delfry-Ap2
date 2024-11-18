package edu.ucne.fitgoal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.fitgoal.presentation.navigation.AuthNavHost
import edu.ucne.fitgoal.ui.theme.FitGoalTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitGoalTheme {
                AuthNavHost()
            }
        }
    }
}