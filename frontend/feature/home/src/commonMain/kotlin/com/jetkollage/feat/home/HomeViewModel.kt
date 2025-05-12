package com.jetkollage.feat.home

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetkollage.domain.overlay.OverlayCategory
import com.jetkollage.domain.overlay.OverlayItem
import com.jetkollage.feat.home.repository.OverlayRepository
import com.jetkollage.ui.repository.ImageRepository
import com.jetkollage.ui.widget.canvas.CanvasEvent
import com.jetkollage.ui.widget.canvas.drawable.ViewportState
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
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

    data object OnAbout : HomeEvent
    object OnExport : HomeEvent
}

internal class HomeViewModel(
    private val overlayRepository: OverlayRepository,
    private val imageRepository: ImageRepository,
) : ViewModel() {

    val state = MutableStateFlow(HomeState())
    val exportFlow  = MutableSharedFlow<Unit>(replay = 0)

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
                is HomeEvent.OnImage -> addImage(event.platformImage)
                is HomeEvent.OnOverlay -> addImage(event.overlay.url)

                HomeEvent.OnAbout -> { /* NO-OP */ }

                HomeEvent.OnExport -> {
                    exportFlow.emit(Unit)
                }
            }
        }
    }

    val canvasState = MutableStateFlow(ViewportState())

    private suspend fun addImage(data: Any?) {
        if (data == null) return
        val image = imageRepository.fetchImage(data)
        if (image != null) {
            canvasState.update { it.addImage(image) }
        } else {
            // TODO: handle error
        }
    }


    fun onCanvasEvent(event: CanvasEvent) {
        canvasState.update {
            it.onEvent(event)

        }
    }

}
