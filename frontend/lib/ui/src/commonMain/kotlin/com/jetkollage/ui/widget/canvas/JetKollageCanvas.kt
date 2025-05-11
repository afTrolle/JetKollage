package com.jetkollage.ui.widget.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import com.jetkollage.ui.util.detectCombinedTransformAndDragGestures
import com.jetkollage.ui.widget.canvas.CanvasEvent.OnDragGesture
import com.jetkollage.ui.widget.canvas.CanvasEvent.OnDragGestureCancel
import com.jetkollage.ui.widget.canvas.CanvasEvent.OnDragGestureEnd
import com.jetkollage.ui.widget.canvas.CanvasEvent.OnDragGestureStart
import com.jetkollage.ui.widget.canvas.CanvasEvent.OnTap
import com.jetkollage.ui.widget.canvas.CanvasEvent.OnTransformGesture
import com.jetkollage.ui.widget.canvas.drawable.CanvasState


sealed interface CanvasEvent {

    data class OnTap(val centerOffset: Offset) : CanvasEvent

    data object OnDragGestureStart : CanvasEvent

    data object OnDragGestureEnd : CanvasEvent

    data object OnDragGestureCancel : CanvasEvent

    data class OnDragGesture(val offset: Offset) : CanvasEvent

    data class OnTransformGesture(val zoomDelta: Float, val rotationDelta: Float) : CanvasEvent

}


@Composable
fun JetKollageCanvas(
    canvasState: State<CanvasState>,
    onAction: (CanvasEvent) -> Unit = {},
) {
    var centerOffset by remember { mutableStateOf(Offset.Zero) }
    val state by canvasState

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
            .pointerInput(onAction) {
                detectCombinedTransformAndDragGestures(
                    onTap = {
                        val centerOffset = it - centerOffset
                        onAction(OnTap(centerOffset))
                    },
                    onDragGestureStart = {
                        onAction(OnDragGestureStart)
                    },
                    onDragGestureEnd = {
                        onAction(OnDragGestureEnd)
                    },
                    onDragGestureCancel = {
                        onAction(OnDragGestureCancel)
                    },
                    onDragGesture = { dragAmount ->
                        onAction(OnDragGesture(dragAmount))
                    },
                    panZoomLock = false,
                    onTransformGesture = { _, _, zoom, rotation ->
                        onAction(OnTransformGesture(zoom, rotation))
                    },
                )
            }
    ) {
        centerOffset = center

        with(state) {
            draw(Transformation(centerOffset = centerOffset))
        }
    }
}

