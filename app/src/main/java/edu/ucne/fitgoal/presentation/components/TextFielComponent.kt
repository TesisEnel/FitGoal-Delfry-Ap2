package edu.ucne.fitgoal.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.ucne.fitgoal.ui.theme.Error
import edu.ucne.fitgoal.ui.theme.LightGray

@Composable
fun TextFielComponent(
    texto: String,
    valor: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction = ImeAction.Next,
    error: Boolean = false,
    errorMessage: String = "",
    readOnly: Boolean = false,
    onValueChange: (String) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    TextField(
        readOnly = readOnly,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        value = valor,
        onValueChange = {onValueChange(it)},
        placeholder = { Text(texto) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = LightGray,
            unfocusedContainerColor = LightGray
        ),
        shape = RoundedCornerShape(16.dp),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) },
            onDone = { focusManager.clearFocus() }
        )
    )
    Spacer(modifier = Modifier.height(12.dp))
    if(error) {
        Text(
            text = errorMessage,
            color = Error,
            modifier = Modifier
                .padding(horizontal = 20.dp).background(Color.Transparent),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}