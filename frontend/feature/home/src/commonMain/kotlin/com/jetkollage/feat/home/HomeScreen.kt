package com.jetkollage.feat.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen() {
    val viewmodel = koinViewModel<HomeViewModel>()
    val state = viewmodel.state.collectAsStateWithLifecycle()

    HomeLayout(
        state = state.value,
        onEvent = viewmodel::onEvent
    )
}

@Composable
internal fun HomeLayout(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit = {}
) {

    Text(text = "Home")

}

