package edu.ucne.fitgoal.presentation.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.ucne.fitgoal.R
import kotlinx.coroutines.launch

@Composable
fun Drawer(
    drawerState: DrawerState,
    navHostController: NavHostController,
    content: @Composable () -> Unit
) {
    val selectedItem = remember { mutableStateOf("FitGoal") }
    val scope = rememberCoroutineScope()

    fun handleItemClick(destination: Screen, item: String) {
        navHostController.navigate(destination)
        selectedItem.value = item
        scope.launch { drawerState.close() }
    }
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "FitGoal App",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    item {
                        DrawerItem(
                            title = stringResource(R.string.drawer_planificador),
                            icon = Icons.Filled.Home,
                            isSelected = selectedItem.value == stringResource(R.string.drawer_planificador)
                        ) {
                            handleItemClick(Screen.PlanificadorScreen, it)
                        }
                        DrawerItem(
                            title = stringResource(R.string.drawer_perfil),
                            icon = Icons.Filled.AccountCircle,
                            isSelected = selectedItem.value == stringResource(R.string.drawer_perfil)
                        ) {
                            handleItemClick(Screen.PerfilScreen, it)
                        }
                        DrawerItem(
                            title = stringResource(R.string.drawer_reloj),
                            icon = Icons.Filled.WatchLater,
                            isSelected = selectedItem.value == stringResource(R.string.drawer_reloj)
                        ) {
                            handleItemClick(Screen.RelojScreen, it)
                        }
                        DrawerItem(
                            title = stringResource(R.string.drawer_calculadora),
                            icon = Icons.Filled.Calculate,
                            isSelected = selectedItem.value == stringResource(R.string.drawer_calculadora)
                        ) {
                            handleItemClick(Screen.CalculadoraScreen, it)
                        }
                    }
                }
            }
        },
        drawerState = drawerState
    ) {
        content()
    }
}