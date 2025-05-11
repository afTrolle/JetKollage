package com.jetkollage.ui.widget.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import com.jetkollage.ui.util.detectCombinedTransformAndDragGestures
import com.jetkollage.ui.widget.canvas.CanvasEvent.OnDragGesture
import com.jetkollage.ui.widget.canvas.CanvasEvent.OnDragGestureCancel
import com.jetkollage.ui.widget.canvas.CanvasEvent.OnDragGestureEnd
import com.jetkollage.ui.widget.canvas.CanvasEvent.OnDragGestureStart
import com.jetkollage.ui.widget.canvas.CanvasEvent.OnTap
import com.jetkollage.ui.widget.canvas.CanvasEvent.OnTransformCancel
import com.jetkollage.ui.widget.canvas.CanvasEvent.OnTransformEnd
import com.jetkollage.ui.widget.canvas.CanvasEvent.OnTransformGesture
import com.jetkollage.ui.widget.canvas.CanvasEvent.OnTransformStart
import com.jetkollage.ui.widget.canvas.drawable.ViewportState


sealed interface CanvasEvent {
    data class OnScreenCanvasSize(val screenCanvasSize: Size) : CanvasEvent

    data class OnTap(val screenTopLeftOffset: Offset) : CanvasEvent

    data class OnDragGesture(val dragAmount: Offset) : CanvasEvent
    data object OnDragGestureStart : CanvasEvent
    data object OnDragGestureEnd : CanvasEvent
    data object OnDragGestureCancel : CanvasEvent

    data class OnTransformGesture(val zoomDelta: Float, val rotationDelta: Float) : CanvasEvent
    data object OnTransformStart : CanvasEvent
    data object OnTransformEnd : CanvasEvent
    data object OnTransformCancel : CanvasEvent
}


@Composable
fun JetKollageCanvas(
    canvasState: State<ViewportState>,
    onAction: (CanvasEvent) -> Unit = {},
) {
    val state by canvasState
    var centerOffset by remember { mutableStateOf(Offset.Zero) }
    var screenCanvasSize by remember { mutableStateOf(Size.Zero) }

    LaunchedEffect(screenCanvasSize) {
        onAction(CanvasEvent.OnScreenCanvasSize(screenCanvasSize))
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
            .pointerInput(onAction) {
                detectCombinedTransformAndDragGestures(
                    onTap = {
                        onAction(OnTap(it))
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
                    panZoomLock = true,
                    onTransformGesture = { _, _, zoom, rotation ->
                        onAction(OnTransformGesture(zoom, rotation))
                    },
                    onTransformGestureStart = {
                        onAction(OnTransformStart)
                    },
                    onTransformGestureEnd = {
                        onAction(OnTransformEnd)
                    },
                    onTransformGestureCancel = {
                        onAction(OnTransformCancel)
                    },
                )
            }
    ) {
        centerOffset = center
        screenCanvasSize = size

        with(state) {
            draw()
        }
    }
}

