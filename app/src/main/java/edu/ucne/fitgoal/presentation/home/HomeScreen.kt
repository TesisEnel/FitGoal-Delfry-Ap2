package edu.ucne.fitgoal.presentation.home

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.fitgoal.R
import edu.ucne.fitgoal.presentation.auth.AuthEvent
import edu.ucne.fitgoal.presentation.auth.AuthViewModel
import edu.ucne.fitgoal.ui.theme.ExDarkGreen

@Composable
fun HomeScreen(
    goLogin: () -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val onEvent = authViewModel::onEvent
    val activity = LocalContext.current as? Activity

    Text(
        text = "Bienvenido a Home",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 60.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { activity?.let { onEvent(AuthEvent.Logout(it, goLogin)) } },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logout),
                    contentDescription = null,
                    tint = ExDarkGreen,
                    modifier = Modifier.size(50.dp)
                )
            }
            Text(
                text = "Cerrar Sesión",
                color = ExDarkGreen,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(top = 3.dp)
            )
        }
    }
}