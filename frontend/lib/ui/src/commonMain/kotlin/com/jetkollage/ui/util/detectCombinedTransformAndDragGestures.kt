package com.jetkollage.ui.util

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.positionChanged
import kotlin.math.PI
import kotlin.math.abs

suspend fun PointerInputScope.detectCombinedTransformAndDragGestures(
    panZoomLock: Boolean = false,
    onTransformGesture: (centroid: Offset, panDelta: Offset, zoomDelta: Float, rotationDelta: Float) -> Unit,
    onTransformGestureStart: ((startCentroid: Offset) -> Unit)? = null,
    onTransformGestureEnd: (() -> Unit)? = null,
    onTransformGestureCancel: (() -> Unit)? = null,
    onDragGestureStart: ((startCentroid: Offset) -> Unit)? = null,
    onDragGesture: ((panDelta: Offset) -> Unit)? = null,
    onDragGestureEnd: (() -> Unit)? = null,
    onDragGestureCancel: (() -> Unit)? = null,
    onTap: ((Offset) -> Unit)? = null,
    dragOrientationLock: Orientation? = null
) = awaitEachGesture {
    var gestureRotationAccumulated = 0f
    var gestureZoomAccumulated = 1f
    var gesturePanAccumulated = Offset.Zero

    var pastTouchSlop = false
    val touchSlop = viewConfiguration.touchSlop
    var lockedToPanZoom = false
    var isCurrentlyDragging = false
    var transformGestureStartedThisSession = false

    var wasCancelled = false

    val firstDown = awaitFirstDown(requireUnconsumed = false)
    val firstDownId = firstDown.id
    val firstDownPosition = firstDown.position
    do {
        val event = awaitPointerEvent()
        val canceledByConsumption = event.changes.any { it.isConsumed }

        if (canceledByConsumption) {
            wasCancelled = true
            if (isCurrentlyDragging) {
                isCurrentlyDragging = false
                onDragGestureCancel?.invoke()
            }
            if (transformGestureStartedThisSession) {
                onTransformGestureCancel?.invoke()
            }
            break
        }
        if (!pastTouchSlop && onTap != null) {
            val firstDownChange = event.changes.find { it.id == firstDownId }
            if (firstDownChange != null && !firstDownChange.pressed) {
                val otherPointersPressed = event.changes.any { it.id != firstDownId && it.pressed }

                if (!otherPointersPressed) {
                    firstDownChange.consume()
                    onTap.invoke(firstDownPosition)
                    return@awaitEachGesture
                }
            }
        }

        val eventZoomChange = event.calculateZoom()
        val eventRotationChange = event.calculateRotation()
        var eventPanChange = event.calculatePan()

        if (dragOrientationLock != null && eventPanChange != Offset.Zero) {
            eventPanChange = when (dragOrientationLock) {
                Orientation.Vertical -> Offset(0f, eventPanChange.y)
                Orientation.Horizontal -> Offset(eventPanChange.x, 0f)
            }
        }

        if (!pastTouchSlop) {
            gestureZoomAccumulated *= eventZoomChange
            gestureRotationAccumulated += eventRotationChange
            gesturePanAccumulated += eventPanChange

            val centroidSize = event.calculateCentroidSize(useCurrent = false)
            val zoomMotion = abs(1 - gestureZoomAccumulated) * centroidSize
            val rotationMotion = abs(gestureRotationAccumulated * PI.toFloat() * centroidSize / 180f)
            val panMotion = gesturePanAccumulated.getDistance()

            if (zoomMotion > touchSlop || rotationMotion > touchSlop || panMotion > touchSlop) {
                pastTouchSlop = true
                lockedToPanZoom = panZoomLock && rotationMotion < touchSlop

                val currentCentroid = event.calculateCentroid(useCurrent = false)
                if (!transformGestureStartedThisSession) {
                    transformGestureStartedThisSession = true
                    onTransformGestureStart?.invoke(currentCentroid)
                }

                if (eventPanChange != Offset.Zero && !isCurrentlyDragging) {
                    isCurrentlyDragging = true
                    onDragGestureStart?.invoke(currentCentroid)
                }
            }
        }

        if (pastTouchSlop) {
            val centroid = event.calculateCentroid(useCurrent = false)
            val effectiveEventRotationChange = if (lockedToPanZoom) 0f else eventRotationChange

            if (isCurrentlyDragging && eventPanChange != Offset.Zero) {
                onDragGesture?.invoke(eventPanChange)
            }

            if (effectiveEventRotationChange != 0f || eventZoomChange != 1f || eventPanChange != Offset.Zero) {
                onTransformGesture(centroid, eventPanChange, eventZoomChange, effectiveEventRotationChange)
            }

            event.changes.forEach {
                if (it.positionChanged()) {
                    it.consume()
                }
            }
        }
    } while (!wasCancelled && event.changes.any { it.pressed })

    if (!wasCancelled && isCurrentlyDragging) {
        isCurrentlyDragging = false
        onDragGestureEnd?.invoke()
    }
    if (!wasCancelled && transformGestureStartedThisSession) {
        onTransformGestureEnd?.invoke()
    }
}