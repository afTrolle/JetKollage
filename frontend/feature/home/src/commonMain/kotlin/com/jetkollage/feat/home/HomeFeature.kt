package com.jetkollage.feat.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jetkollage.nav.Graph


fun NavGraphBuilder.homeFeature() {
    composable<Graph.Home> {
        HomeScreen()
    }
}