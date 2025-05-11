package com.jetkollage.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import org.koin.core.KoinApplication

@OptIn(ExperimentalMaterial3Api::class)
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

    CompositionLocalProvider(
        LocalRippleConfiguration provides RippleConfiguration(
            // Custom alpha to make image clicks more present
            rippleAlpha = RippleAlpha(
                pressedAlpha =  0.2f,
                draggedAlpha =  0.16f,
                focusedAlpha =  0.2f,
                hoveredAlpha = 0.08f
            )
        )
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }

}



