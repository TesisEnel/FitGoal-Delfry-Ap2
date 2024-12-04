package edu.ucne.fitgoal.presentation.components

import androidx.compose.runtime.Composable

@Composable
fun ShowComponent(
    value: Boolean,
    whenContentIsTrue: @Composable () -> Unit = {},
    whenContentIsFalse: @Composable () -> Unit = {}
) {
    if (value) {
        whenContentIsTrue()
    } else {
        whenContentIsFalse()
    }
}