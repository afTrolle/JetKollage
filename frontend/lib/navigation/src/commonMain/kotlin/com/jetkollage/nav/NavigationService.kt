package com.jetkollage.nav

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.flow.MutableSharedFlow

internal class NavigationServiceImpl() : NavigationService, Navigator {

    override val navigationFlow: MutableSharedFlow<Pair<Graph, NavOptionsBuilder.(NavController) -> Unit>> =
        MutableSharedFlow(extraBufferCapacity = 1)

    override val popFlow: MutableSharedFlow<Unit> = MutableSharedFlow(extraBufferCapacity = 1)


    override suspend fun navigate(
        screen: Graph,
        options: NavOptionsBuilder.(NavController) -> Unit,
    ) {
        navigationFlow.emit(screen to options)
    }

    override suspend fun popBackStack() {
        popFlow.emit(Unit)
    }

}

/**
 * Used to drive navigation
 */
interface Navigator  {

    val navigationFlow: MutableSharedFlow<Pair<Graph, NavOptionsBuilder.(NavController) -> Unit>>

    val popFlow: MutableSharedFlow<Unit>

}

/**
 * Used to tell where to navigate
 */
interface NavigationService {

    suspend fun navigate(screen: Graph, options: NavOptionsBuilder.(NavController) -> Unit = {})

    suspend fun popBackStack()

}

