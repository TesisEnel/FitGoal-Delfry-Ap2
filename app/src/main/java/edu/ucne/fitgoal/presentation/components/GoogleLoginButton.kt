package edu.ucne.fitgoal.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.ucne.fitgoal.R
import edu.ucne.fitgoal.ui.theme.DarkGreen
import edu.ucne.fitgoal.ui.theme.LightGray
import edu.ucne.fitgoal.ui.theme.LightGreen

@Composable
fun GoogleLoginButton(enabled:Boolean, onClick: () -> Unit) {
    Button(
        onClick = {onClick()},
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .border(
                BorderStroke(3.dp, Brush.horizontalGradient(
                    colors = if(enabled) listOf(DarkGreen, LightGreen) else listOf(LightGray , LightGray)
                )),shape = RoundedCornerShape(18.dp)
            ),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.google),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "INICIA SESIÓN CON GOOGLE",
            color = Color.Black,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}