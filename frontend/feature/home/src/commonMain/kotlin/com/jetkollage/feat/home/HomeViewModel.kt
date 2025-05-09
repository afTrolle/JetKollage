package com.jetkollage.feat.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetkollage.domain.overlay.OverlayCategory
import com.jetkollage.feat.home.repository.OverlayRepository
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Immutable
internal data class HomeState(
    val overlays: List<OverlayCategory> = emptyList(),
)


sealed interface HomeEvent {
    data class OnImage(val platformImage: PlatformFile?) : HomeEvent

    data object OnOverlay : HomeEvent

    data object OnLayers : HomeEvent

    data object OnAbout : HomeEvent
}

internal class HomeViewModel(
    private val overlayRepository: OverlayRepository
) : ViewModel() {

    val state = MutableStateFlow(HomeState())

    init {
        viewModelScope.launch(Dispatchers.Default) {
            overlayRepository
                .overlayFlow()
                .collect { overlays ->
                    state.update { it.copy(overlays = overlays) }
                }
        }
    }


    fun onEvent(action: HomeEvent) {
        when (action) {
            is HomeEvent.OnImage -> {
                // TODO
            }

            HomeEvent.OnOverlay -> {
                // TODO
            }

            HomeEvent.OnAbout -> { /* NO-OP */
            }

            HomeEvent.OnLayers -> { /* NO-OP */
            }
        }
    }

}