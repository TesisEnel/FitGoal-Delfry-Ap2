package edu.ucne.fitgoal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController

import edu.ucne.fitgoal.presentation.navigation.FitGoalNavHost
import edu.ucne.fitgoal.ui.theme.FitGoalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitGoalTheme {
                val navHost = rememberNavController()
                FitGoalNavHost(navHost)
            }
        }
    }
}