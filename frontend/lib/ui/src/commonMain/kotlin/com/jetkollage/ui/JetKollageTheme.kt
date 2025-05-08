package com.jetkollage.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable


@Composable
fun JetKollageTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // TODO add settings toggle
    content: @Composable () -> Unit
) {

    val dynamicColorScheme = if (dynamicColor) {
        getDynamicColorScheme(darkTheme)
    } else {
        null
    }

    val colorScheme = when {
        dynamicColorScheme != null -> dynamicColorScheme
        darkTheme -> darkScheme
        else -> lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}


