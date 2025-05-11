package com.jetkollage.feat.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetkollage.domain.overlay.OverlayCategory
import com.jetkollage.domain.overlay.OverlayItem
import com.jetkollage.feat.home.repository.OverlayRepository
import com.jetkollage.ui.repository.ImageRepository
import com.jetkollage.ui.widget.canvas.CanvasEvent
import com.jetkollage.ui.widget.canvas.drawable.CanvasState
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.coil.coilModel
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

    data class OnOverlay(val overlay: OverlayItem) : HomeEvent

    data object OnLayers : HomeEvent

    data object OnAbout : HomeEvent
}

internal class HomeViewModel(
    private val overlayRepository: OverlayRepository,
    private val imageRepository: ImageRepository,
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

    fun onEvent(event: HomeEvent) {
        viewModelScope.launch {
            when (event) {
                is HomeEvent.OnImage -> addImage(event.platformImage?.coilModel)
                is HomeEvent.OnOverlay -> addImage(event.overlay.url)

                HomeEvent.OnAbout -> { /* NO-OP */
                }

                HomeEvent.OnLayers -> { /* NO-OP */
                }
            }
        }
    }

    val canvasState = MutableStateFlow(CanvasState())

    private suspend fun addImage(image: Any?) {
        val image = imageRepository.fetchImage(image)
        if (image != null) {
            canvasState.update { it.addImage(image) }
        } else {
            // TODO: handle error
        }
    }


    fun onCanvasEvent(event: CanvasEvent) {
        when (event) {
            is CanvasEvent.OnDragGesture -> {
                /* NO-OP */
            }

            CanvasEvent.OnDragGestureCancel -> {
                /* NO-OP */
            }

            CanvasEvent.OnDragGestureEnd -> {
                /* NO-OP */
            }

            is CanvasEvent.OnDragGestureStart -> {
                /* NO-OP */
            }

            is CanvasEvent.OnTap -> {
                /* NO-OP */
            }

            is CanvasEvent.OnTransformGesture -> {
                /* NO-OP */
            }
        }
    }

}