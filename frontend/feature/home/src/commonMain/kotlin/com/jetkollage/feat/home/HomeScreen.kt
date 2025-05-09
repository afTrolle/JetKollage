package com.jetkollage.feat.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jetkollage.feat.home.widgets.JetKollageTopBar
import com.jetkollage.feat.home.widgets.OverlayPickerBottomSheet
import com.jetkollage.feat.home.widgets.Tool
import com.jetkollage.feat.home.widgets.toolbar
import com.jetkollage.ui.util.windowSizeClass
import com.jetkollage.ui.widget.scaffold.JetKollageToolsOverlay
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen() {
    val viewmodel = koinViewModel<HomeViewModel>()
    val state = viewmodel.state.collectAsStateWithLifecycle()

    HomeLayout(
        state = state.value, onEvent = viewmodel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeLayout(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit = {}
) = Scaffold(
    topBar = { JetKollageTopBar("JetKollage") },
) {

    val windowSizeClass = windowSizeClass()

    val imagePicker = rememberFilePickerLauncher(type = FileKitType.Image) { image ->
        image?.let { onEvent(HomeEvent.OnImage(image)) }
    }

    var showOverlayPickerSheet by remember { mutableStateOf(false) }

    JetKollageToolsOverlay(
        modifier = Modifier.padding(it),
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
        KollageCanvas()

        if (showOverlayPickerSheet) {
            OverlayPickerBottomSheet(
                categories = state.overlays,
                onDismissRequest = { showOverlayPickerSheet = false }
            )
        }
    }
}

@Composable
fun KollageCanvas() {
    // TODO setup Canvas
}
