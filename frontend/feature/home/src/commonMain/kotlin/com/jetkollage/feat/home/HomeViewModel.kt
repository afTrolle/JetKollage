package com.jetkollage.feat.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetkollage.feat.home.repository.OverlayRepository
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@Immutable
internal data class HomeState(
    val temp: String = "",
    val overlays: List<OverlayCategory> = emptyList(),
)


@Immutable
data class OverlayCategory(
    val name: String, val items: List<OverlayItem>, val id: Int
)

@Immutable
data class OverlayItem(
    val name: String,
    val url: String,
    val contentDescription: String,
    val id: Int,
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
            overlayRepository.overlayFlow().collect { apiOverlays ->
                val overlays = apiOverlays.map {
                    OverlayCategory(
                        id = it.id,
                        name = it.title,
                        items = it.items.map { item ->
                            OverlayItem(
                                id = it.id,
                                name = item.overlayName,
                                url = item.sourceUrl,
                                contentDescription = item.overlayName
                            )
                        }
                    )
                }
                state.update {
                    it.copy(overlays = overlays)
                }
            }
        }

    }


    fun onEvent(action: HomeEvent) {
        when (action) {
            is HomeEvent.OnImage -> { /* NO-OP */
            }

            HomeEvent.OnOverlay -> { /* NO-OP */
            }

            HomeEvent.OnAbout -> { /* NO-OP */
            }

            HomeEvent.OnLayers -> { /* NO-OP */
            }
        }
    }

}