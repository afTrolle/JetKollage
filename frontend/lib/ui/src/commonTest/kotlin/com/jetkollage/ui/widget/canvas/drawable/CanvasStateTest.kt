package com.jetkollage.ui.widget.canvas.drawable

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.jetkollage.ui.widget.canvas.Transformation
import kotlin.test.Test
import kotlin.test.assertEquals


class CanvasStateTest {

    companion object {
        val screenSize = Size(40f, 40f)
        val screenRect = Rect(Offset.Zero, screenSize)
        val screenTransformation = Transformation(centerOffset = screenRect.center)

        val canvasResolution = Size(20f, 20f)
        val emptyCanvas = ViewportState(intrinsicSize = canvasResolution)

        const val ZOOM = 2f
        const val MOVE = 20f

        val expectedWidthInitialLeftPlacementInScreen =
            (screenSize.width - canvasResolution.width) / 2f
        val expectedHeightInitialLeftPlacementInScreen =
            (screenSize.height - canvasResolution.height) / 2f
    }


    @Test
    fun initialCanvasPlacement() {
        val screenRect = emptyCanvas.getRect(screenTransformation)
        assertEquals(
            expected = expectedWidthInitialLeftPlacementInScreen,
            actual = screenRect.left
        )
        assertEquals(
            expected = expectedHeightInitialLeftPlacementInScreen,
            actual = screenRect.top
        )
    }

    @Test
    fun scaleCanvas2x() {
        val state = emptyCanvas.zoom(ZOOM)

        val screenRect = state.getRect(screenTransformation)

        assertEquals(
            expected = canvasResolution * ZOOM,
            actual = screenRect.size
        )
    }

    @Test
    fun moveCanvas() {
        val state = emptyCanvas
            .move(Offset(-MOVE, -MOVE))

        val screenRect = state.getRect(screenTransformation)

        assertEquals(
            expected = expectedHeightInitialLeftPlacementInScreen - MOVE,
            actual = screenRect.top
        )
        assertEquals(
            expected = expectedWidthInitialLeftPlacementInScreen - MOVE,
            actual = screenRect.top
        )
    }

    @Test
    fun scaleAndMoveCanvas() {
        // Apply zoom then pan
        val state = emptyCanvas
            .zoom(ZOOM)
            .move(Offset(MOVE, MOVE))

        // Project to screen
        val rect = state.getRect(screenTransformation)

        // Expected scaled size
        val expectedSize = canvasResolution * ZOOM
        assertEquals(expectedSize, rect.size, "Canvas should be scaled by $ZOOM")

        // Compute expected center after move
        val expectedCenter = screenTransformation.centerOffset + Offset(MOVE, MOVE)
        val halfWidth = expectedSize.width / 2f
        val halfHeight = expectedSize.height / 2f

        // Verify each edge
        assertEquals(expectedCenter.x - halfWidth, rect.left, "Left edge after pan")
        assertEquals(expectedCenter.y - halfHeight, rect.top, "Top edge after pan")
        assertEquals(expectedCenter.x + halfWidth, rect.right, "Right edge after pan")
        assertEquals(expectedCenter.y + halfHeight, rect.bottom, "Bottom edge after pan")
    }

    private fun ViewportState.zoom(zoom: Float): ViewportState =
        startZoom()
            .updateZoom(zoom)
            .endZoom()

    private fun ViewportState.move(move: Offset): ViewportState =
        startDrag()
            .updateDrag(move)
            .endDrag()

    // Helper
    private fun ViewportState.getRect(transformation: Transformation): Rect {
        val width = intrinsicSize.width * zoom
        val height = intrinsicSize.height * zoom
        val center = transformation.centerOffset
        val left = center.x + pan.x - width * 0.5f
        val top = center.y + pan.y - height * 0.5f
        return Rect(Offset(left, top), Size(width, height))
    }
}