package com.jetkollage.ui.widget.canvas.drawable

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.util.fastFirstOrNull
import androidx.compose.ui.util.fastForEachReversed
import com.jetkollage.ui.ext.asBitmap
import com.jetkollage.ui.widget.canvas.CanvasEvent
import com.jetkollage.ui.widget.canvas.Transformation
import com.jetkollage.ui.widget.canvas.TransformationsContainer


data class ViewportState(
    val zoom: Float = 1f,
    val pan: Offset = Offset.Zero,
    val intrinsicSize: Size = Size(800f, 800f),
    val background: Color = Color.White,
    val snapColor: Color = Color.Yellow,
    val screenSize: Size = Size.Unspecified,
    val snapThreshold: Float = 10f,

    val transformations: TransformationsContainer = TransformationsContainer(),
    val layers: List<Layer> = emptyList(),
    val viewId: DrawableId = DrawableId.MainCanvas,
) {
    val screenCenter: Offset = screenSize.center
    val worldCenter: Offset = intrinsicSize.center

    val selectedLayer: Layer? = layers.firstOrNull { it.isSelected }

    /* From World space to Screen/Canvas space. */
    val worldToScreenMatrix = Matrix().apply {
        resetToPivotedTransform(
            pivotX = worldCenter.x,
            pivotY = worldCenter.y,
            translationX = pan.x + screenCenter.x,
            translationY = pan.y + screenCenter.y,
            scaleX = zoom,
            scaleY = zoom,
        )
    }

    val screenToWorldMatrix = Matrix().apply {
        setFrom(worldToScreenMatrix)
        invert()
    }

    fun getDrawableMatrix(drawable: Drawable): Matrix {
        val transformation = transformations.getTransformation(drawable.id)
        return transformation.getMatrix(drawable.intrinsicSize)
    }

    private fun getLayerWorldRect(layer: Layer, customTransform: Transformation? = null): Rect {
        val transformToUse = customTransform ?: transformations.getTransformation(layer.id)
        val matrix = transformToUse.getMatrix(layer.intrinsicSize)
        return matrix.map(Rect(Offset.Zero, layer.intrinsicSize))
    }

    fun screenToWorld(screenOffset: Offset): Offset = screenToWorldMatrix.map(screenOffset)

    fun DrawScope.draw(matrix: Matrix = worldToScreenMatrix) {
        withTransform(
            transformBlock = {
                transform(matrix)
            }
        ) {
            // Background
            drawBackground()
            // Layers
            layers.fastForEachReversed { layer ->
                val matrix = getDrawableMatrix(layer)
                layer.draw.invoke(this, matrix)
            }
            // Snap Highlight
            activeSnapGuides.forEach { guide ->
                drawLine(
                    color = snapColor,
                    start = guide.start,
                    end = guide.end,
                    strokeWidth = snapThreshold,
                )
            }
        }
    }

    private fun DrawScope.drawBackground() {
        drawRect(
            color = background,
            topLeft = -intrinsicSize.center,
            size = intrinsicSize
        )
    }

    fun onEvent(event: CanvasEvent): ViewportState = when (event) {
        is CanvasEvent.OnDragGesture -> updateDrag(event.dragAmount)
        CanvasEvent.OnDragGestureCancel -> cancelDrag()
        CanvasEvent.OnDragGestureEnd -> endDrag()
        is CanvasEvent.OnDragGestureStart -> startDrag()
        is CanvasEvent.OnTap -> selectLayer(event.screenTopLeftOffset)
        is CanvasEvent.OnTransformGesture -> updateZoom(event.zoomDelta)
        CanvasEvent.OnTransformCancel -> cancelZoom()
        CanvasEvent.OnTransformEnd -> endZoom()
        CanvasEvent.OnTransformStart -> startZoom()
        is CanvasEvent.OnScreenCanvasSize -> updateCanvasSize(event.screenCanvasSize)
    }

    private fun updateCanvasSize(screenCanvasSize: Size) = copy(screenSize = screenCanvasSize)

    fun addImage(imageBitmap: ImageBitmap) =
        copy(
            viewId = viewId.next(),
            layers = layers + listOf(
                Layer.ImageLayer(
                    id = viewId.next(),
                    image = imageBitmap,
                )
            )
        )

    private fun selectLayer(offset: Offset): ViewportState {
        val worldOffset = screenToWorld(offset)
        val layer = layers.fastFirstOrNull {
            val box = getLayerWorldRect(it)
            box.contains(worldOffset)
        }
        return copy(layers = layers.map { it.setSelected(it == layer) })
    }

    /*
    Zoom
    */

    fun startZoom(): ViewportState = copy(
        transformations = transformations.startZoomTransformation(selectedLayer?.id)
    )

    fun endZoom(): ViewportState = copy(
        transformations = transformations.commitZoomTransformation()
    )

    private fun cancelZoom(): ViewportState = copy(
        transformations = transformations.cancelZoomTransformation()
    )

    fun updateZoom(zoomDelta: Float): ViewportState = if (selectedLayer == null) {
        copy(zoom = zoomDelta * zoom)
    } else {
        copy(transformations = transformations.adjustZoomTransformation(zoomDelta))
    }

    /*
    Pan
    */

    fun startDrag(): ViewportState = copy(
        transformations = transformations.startOffsetTransformation(selectedLayer?.id, Offset.Zero)
    )

    fun endDrag(): ViewportState = copy(
        transformations = activeSnapGuides.fold(transformations) { acc, snapGuide ->
            acc.adjustOffsetTransformation(snapGuide.correction)
        }.commitOffsetTransformation()
    )

    private fun cancelDrag(): ViewportState = copy(
        transformations = transformations.cancelOffsetTransformation()
    )

    fun updateDrag(dragAmount: Offset): ViewportState = if (selectedLayer == null) {
        copy(pan = pan + dragAmount)
    } else {
        val currentLayerFullTransformation = transformations.getTransformation(selectedLayer.id)
        val totalZoom = currentLayerFullTransformation.zoom * zoom
        val worldDelta = dragAmount / totalZoom
        copy(transformations = transformations.adjustOffsetTransformation(worldDelta))
    }


    /*
     SnapGuides
     */

    val activeSnapGuides =
        if (selectedLayer != null && transformations.transformationsInProgress(selectedLayer.id)) {
            val layerTransform = transformations.getTransformation(selectedLayer.id)
            val selectedLayerWorldRect = getLayerWorldRect(selectedLayer, layerTransform)

            var bestSnapCorrectionX = 0f
            var minAbsDiffX = snapThreshold
            var edgeToSnapToX = 0f

            var bestSnapCorrectionY = 0f
            var minAbsDiffY = snapThreshold
            var edgeToSnapToY = 0f

            val activeEdgesX = listOf(
                selectedLayerWorldRect.left,
                selectedLayerWorldRect.center.x,
                selectedLayerWorldRect.right
            )
            val activeEdgesY = listOf(
                selectedLayerWorldRect.top,
                selectedLayerWorldRect.center.y,
                selectedLayerWorldRect.bottom
            )
            layers
                .filter { it.id != selectedLayer.id }
                .forEach { otherLayer ->
                    val otherLayerWorldRect = getLayerWorldRect(otherLayer)
                    val otherLayerTargetsX =
                        listOf(otherLayerWorldRect.left, otherLayerWorldRect.right)

                    for (activeEdge in activeEdgesX) {
                        for (targetEdge in otherLayerTargetsX) {
                            val diff = targetEdge - activeEdge
                            if (kotlin.math.abs(diff) < minAbsDiffX) {
                                minAbsDiffX = kotlin.math.abs(diff)
                                bestSnapCorrectionX = diff
                                edgeToSnapToX = targetEdge
                            }
                        }
                    }

                    // Vertical snapping checks
                    val otherLayerTargetsY = listOf(
                        otherLayerWorldRect.top,
                        otherLayerWorldRect.center.y,
                        otherLayerWorldRect.bottom
                    )
                    for (activeEdge in activeEdgesY) {
                        for (targetEdge in otherLayerTargetsY) {
                            val diff = targetEdge - activeEdge
                            if (kotlin.math.abs(diff) < minAbsDiffY) {
                                minAbsDiffY = kotlin.math.abs(diff)
                                bestSnapCorrectionY = diff
                                edgeToSnapToY = targetEdge
                            }
                        }
                    }
                }

            val vpWorldLeft = -intrinsicSize.width / 2f
            val vpWorldCenterX = 0f
            val vpWorldRight = intrinsicSize.width / 2f
            val vpWorldTop = -intrinsicSize.height / 2f
            val vpWorldCenterY = 0f
            val vpWorldBottom = intrinsicSize.height / 2f

            val viewportTargetsX = listOf(vpWorldLeft, vpWorldCenterX, vpWorldRight)
            val viewportTargetsY = listOf(vpWorldTop, vpWorldCenterY, vpWorldBottom)

            for (activeEdgeX in activeEdgesX) {
                for (targetEdgeX in viewportTargetsX) {
                    val diffX = targetEdgeX - activeEdgeX
                    if (kotlin.math.abs(diffX) < minAbsDiffX) {
                        minAbsDiffX = kotlin.math.abs(diffX)
                        bestSnapCorrectionX = diffX
                        edgeToSnapToX = targetEdgeX
                    }
                }
            }
            for (activeEdgeY in activeEdgesY) {
                for (targetEdgeY in viewportTargetsY) {
                    val diffY = targetEdgeY - activeEdgeY
                    if (kotlin.math.abs(diffY) < minAbsDiffY) {
                        minAbsDiffY = kotlin.math.abs(diffY)
                        bestSnapCorrectionY = diffY
                        edgeToSnapToY = targetEdgeY
                    }
                }
            }

            val worldScreenTopLeft = screenToWorld(Offset.Zero)
            val worldScreenBottomRight = screenToWorld(Offset(screenSize.width, screenSize.height))

            listOfNotNull(
                SnapGuide(
                    correction = Offset(x = bestSnapCorrectionX / layerTransform.zoom, y = 0f),
                    start = Offset(edgeToSnapToX, worldScreenTopLeft.y),
                    end = Offset(edgeToSnapToX, worldScreenBottomRight.y),
                ).takeIf { bestSnapCorrectionX != 0f },
                SnapGuide(
                    correction = Offset(x = 0f, y = bestSnapCorrectionY / layerTransform.zoom),
                    start = Offset(worldScreenTopLeft.x, edgeToSnapToY),
                    end = Offset(worldScreenBottomRight.x, edgeToSnapToY),
                ).takeIf { bestSnapCorrectionY != 0f }
            )
        } else {
            emptyList()
        }

    fun export(): ImageBitmap {
        val drawScope = CanvasDrawScope()
        val matrix = Matrix().apply {
            resetToPivotedTransform(
                pivotX = intrinsicSize.width / 2f,
                pivotY = intrinsicSize.height / 2f,
                translationX = intrinsicSize.width / 2f,
                translationY = intrinsicSize.height / 2f,
                scaleX = 1f,
                scaleY = 1f,
            )
        }
        return drawScope.asBitmap(intrinsicSize) {
            draw(matrix)
        }
    }

}


