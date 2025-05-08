package com.jetkollage.shared

import androidx.compose.runtime.Composable
import com.jetkollage.ui.JetKollageTheme
import org.koin.compose.KoinMultiplatformApplication
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App() = KoinMultiplatformApplication(
    config = koinConfiguration
) {
    JetKollageTheme {
        Navigation()
    }
}

