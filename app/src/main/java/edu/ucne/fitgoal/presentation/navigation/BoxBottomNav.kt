package edu.ucne.fitgoal.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import edu.ucne.fitgoal.R
import edu.ucne.fitgoal.presentation.components.BackGroundImage

@Composable
fun  BoxBottomNav(
    navHostController: NavHostController,
    selectedItem: MutableState<String>,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                NavBottomBar(
                    navHostController = navHostController,
                    selectedItem = selectedItem
                )
            }
        ) {
            BackGroundImage(id = R.drawable.background)
            Column(
                modifier = Modifier.padding(it)
            ) {
                content()
            }
        }
    }
}