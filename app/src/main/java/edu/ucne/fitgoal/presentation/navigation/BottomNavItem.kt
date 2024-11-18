package edu.ucne.fitgoal.presentation.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import edu.ucne.fitgoal.ui.theme.LightGreen

@Composable
fun RowScope.BottomNavItem(
    title: String,
    isSelected: Boolean,
    id: Int,
    navigateTo: (String) -> Unit
) {
    BottomNavigationItem(
        icon = {
            Icon(
                painter = painterResource(id),
                contentDescription = null,
                tint = if(isSelected) LightGreen else Color.Unspecified,
            )
        },
        selected = isSelected,
        onClick = { navigateTo(title) }
    )
}