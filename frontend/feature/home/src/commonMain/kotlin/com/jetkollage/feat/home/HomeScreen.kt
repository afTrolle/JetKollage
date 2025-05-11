package com.jetkollage.feat.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jetkollage.domain.ViewportState
import com.jetkollage.feat.home.widgets.JetKollageTopBar
import com.jetkollage.feat.home.widgets.OverlayPickerBottomSheet
import com.jetkollage.feat.home.widgets.Tool
import com.jetkollage.feat.home.widgets.toolbar
import com.jetkollage.ui.util.windowSizeClass
import com.jetkollage.ui.widget.canvas.CanvasEvent
import com.jetkollage.ui.widget.canvas.JetKollageCanvas
import com.jetkollage.ui.widget.scaffold.JetKollageToolsOverlay
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen() {
    val viewmodel = koinViewModel<HomeViewModel>()
    val state = viewmodel.state.collectAsStateWithLifecycle()
    val canvasState = viewmodel.canvasState.collectAsStateWithLifecycle()

    HomeLayout(
        state = state.value,
        onEvent = viewmodel::onEvent,
        canvasState = canvasState,
        onCanvasEvent = viewmodel::onCanvasEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeLayout(
    state: HomeState,
    canvasState: State<com.jetkollage.ui.widget.canvas.drawable.ViewportState>,
    onEvent: (HomeEvent) -> Unit = {},
    onCanvasEvent: (CanvasEvent) -> Unit = {},
) = Scaffold(
    topBar = { JetKollageTopBar("JetKollage") },
) {
    val windowSizeClass = windowSizeClass()

    val imagePicker = rememberFilePickerLauncher(type = FileKitType.Image) { image ->
        image?.let { onEvent(HomeEvent.OnImage(image)) }
    }

    var showOverlayPickerSheet by remember { mutableStateOf(false) }

    JetKollageToolsOverlay(
        windowWidthSizeClass = windowSizeClass.widthSizeClass,
        navigationSuiteItems = {
            toolbar(
                onTool = {
                    when (it) {
                        Tool.AddImage -> imagePicker.launch()
                        Tool.AddOverlay -> showOverlayPickerSheet = true
                        Tool.Layers -> onEvent(HomeEvent.OnLayers)
                    }
                },
            )
        },
    ) {
        JetKollageCanvas(
            canvasState,
            onCanvasEvent
        )

        if (showOverlayPickerSheet) {
            OverlayPickerBottomSheet(
                categories = state.overlays,
                onOverlaySelected = {
                    onEvent(HomeEvent.OnOverlay(it))
                    showOverlayPickerSheet = false
                },
                onDismissRequest = { showOverlayPickerSheet = false }
            )
        }
    }
}

