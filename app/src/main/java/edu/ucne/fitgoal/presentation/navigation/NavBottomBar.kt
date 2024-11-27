package edu.ucne.fitgoal.presentation.navigation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.captionBarPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.ucne.fitgoal.R
import edu.ucne.fitgoal.ui.theme.DarkGreen
import edu.ucne.fitgoal.ui.theme.LightGreen

@Composable
fun NavBottomBar(
    navHostController: NavHostController,
    selectedItem: MutableState<String>
) {
    fun handleItemClick(destination: Screen, item: String) {
        navHostController.navigate(destination)
        selectedItem.value = item
    }

    BottomNavigation(
        modifier = Modifier.captionBarPadding().systemBarsPadding()
            .padding(horizontal = 15.dp)
            .border(2.dp, LightGreen, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)),
        backgroundColor = DarkGreen,
    ) {
        BottomNavItem(
            title = stringResource(R.string.drawer_planificador),
            isSelected = selectedItem.value == stringResource(R.string.drawer_planificador),
            id = R.drawable.calendar
        ) {
            handleItemClick(Screen.PlanificadorScreen, it)
        }
        BottomNavItem(
            title = stringResource(R.string.drawer_aguadiaria),
            isSelected = selectedItem.value == stringResource(R.string.drawer_aguadiaria),
            id = R.drawable.water
        ) {
            handleItemClick(Screen.AguaDiariaScreen, it)
        }
        BottomNavItem(
            title = stringResource(R.string.drawer_home),
            isSelected = selectedItem.value == stringResource(R.string.drawer_home),
            id = R.drawable.home
        ) {
            handleItemClick(Screen.HomeScreen, it)
        }
        BottomNavItem(
            title = stringResource(R.string.drawer_ejercicios),
            isSelected = selectedItem.value == stringResource(R.string.drawer_ejercicios),
            id = R.drawable.pull_up
        ) {
            handleItemClick(Screen.EjerciciosScreen, it)
        }
        BottomNavItem(
            title = stringResource(R.string.drawer_calculadora),
            isSelected = selectedItem.value == stringResource(R.string.drawer_calculadora),
            id = R.drawable.list,
        ) {
            handleItemClick(Screen.CalculadoraScreen, it)
        }
    }
}
