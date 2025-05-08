package com.jetkollage.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.jetkollage.feat.about.aboutFeature
import com.jetkollage.feat.home.homeFeature
import com.jetkollage.nav.Graph
import com.jetkollage.nav.Graph.Companion.fromRoute
import com.jetkollage.nav.Navigator
import com.jetkollage.shared.ext.launchOnStarted
import org.koin.compose.koinInject

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navigator = koinInject<Navigator>()

    val lifecycle = LocalLifecycleOwner.current
    LaunchedEffect(navigator, navController, lifecycle) {

        launchOnStarted(lifecycle) {
            navigator.navigationFlow.collect { (screen, builder) ->
                navController.navigate(screen) {
                    builder.invoke(this, navController)
                }
            }
        }

        launchOnStarted(lifecycle) {
            navigator.popFlow.collect {
                navController.popBackStack()
            }
        }
    }

    NavHost(navController, startDestination = Graph.Home) {
        homeFeature()
        aboutFeature()
    }
}



