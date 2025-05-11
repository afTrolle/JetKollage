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
import com.jetkollage.ui.widget.canvas.drawable.DrawableId
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.coil.coilModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

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


    private suspend fun addImage(data: Any?) {
        if (data == null) return
        val image = imageRepository.fetchImage(data)
        if (image != null) {
            canvasState.update { it.addImage( image) }
        } else {
            // TODO: handle error
        }
    }


    fun onCanvasEvent(event: CanvasEvent) {
        when (event) {
            is CanvasEvent.OnDragGesture -> canvasState.update {
                it.updateDrag(event.offset)
            }

            CanvasEvent.OnDragGestureCancel -> canvasState.update {
                it.cancelDrag()
            }

            CanvasEvent.OnDragGestureEnd -> canvasState.update {
                it.endDrag()
            }

            is CanvasEvent.OnDragGestureStart -> canvasState.update {
                it.startDrag()
            }

            is CanvasEvent.OnTap -> canvasState.update {
                it.selectLayer(event.centerOffset)
            }

            is CanvasEvent.OnTransformGesture -> {
                /* NO-OP */
            }
        }
    }

}
