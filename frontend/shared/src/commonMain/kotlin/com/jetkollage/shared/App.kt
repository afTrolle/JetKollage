package com.jetkollage.shared

import androidx.compose.runtime.Composable
import com.jetkollage.ui.JetKollageTheme
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App() = JetKollageTheme {
    Navigation()
}

