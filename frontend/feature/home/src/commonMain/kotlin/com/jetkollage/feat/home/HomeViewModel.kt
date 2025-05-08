package com.jetkollage.feat.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

internal data class HomeState(
    val temp : String = "",
)

interface HomeEvent {

}

internal class HomeViewModel : ViewModel() {

    val state = MutableStateFlow(HomeState())

    fun onEvent(action: HomeEvent) {
        when (action) {
            else -> Unit
        }
    }

}