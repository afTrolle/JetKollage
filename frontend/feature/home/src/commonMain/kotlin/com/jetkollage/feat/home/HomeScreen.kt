package com.jetkollage.feat.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jetkollage.feat.home.widgets.JetKollageTopBar
import com.jetkollage.feat.home.widgets.OverlayPickerBottomSheet
import com.jetkollage.feat.home.widgets.Tool
import com.jetkollage.feat.home.widgets.toolbar
import com.jetkollage.ui.ext.toBytes
import com.jetkollage.ui.util.windowSizeClass
import com.jetkollage.ui.widget.canvas.CanvasEvent
import com.jetkollage.ui.widget.canvas.JetKollageCanvas
import com.jetkollage.ui.widget.scaffold.JetKollageToolsOverlay
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen() {
    val viewmodel = koinViewModel<HomeViewModel>()
    val state = viewmodel.state.collectAsStateWithLifecycle()
    val canvasState = viewmodel.canvasState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val launcher = rememberFileSaverLauncher { file ->
        if (file != null) {
            scope.launch {
                val bitmap = canvasState.value.export()
                bitmap.toBytes()?.let {
                    file.write(it)
                }
            }
        }
    }
    LaunchedEffect(viewmodel) {
        viewmodel.exportFlow.collect {
            launcher.launch(
                suggestedName = "JetKollage",
                extension = "png",
            )
        }
    }

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
    containerColor = MaterialTheme.colorScheme.surfaceDim
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
                        Tool.Export -> onEvent(HomeEvent.OnExport)
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

