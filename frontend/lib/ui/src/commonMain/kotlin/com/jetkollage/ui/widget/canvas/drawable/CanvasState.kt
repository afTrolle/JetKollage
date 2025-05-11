package com.jetkollage.ui.widget.canvas.drawable

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.util.fastFirstOrNull
import androidx.compose.ui.util.fastForEachReversed
import androidx.compose.ui.util.fastMap
import com.jetkollage.ui.ext.asBitmap
import com.jetkollage.ui.widget.canvas.Transformation
import com.jetkollage.ui.widget.canvas.TransformationsContainer

/**
 * Virtual Canvas,
 */
data class CanvasState(
    override val id: DrawableId = DrawableId.MainCanvas,
    val size: Size = Size(400f, 400f), // Resolution
    val background: Color = Color.Companion.White,
    val layers: List<Layer> = emptyList(),
    val transformations: TransformationsContainer = TransformationsContainer(),
    val nextLayerId: DrawableId = DrawableId.MainCanvas.next(),
) : Drawable {

    init {
        require(layers.count { it.isSelected } <= 1) { "multiple layers selected" }

    }

    // TODO add fit transformation for new added images,
    fun addImage(image: ImageBitmap) = copy(
        layers = listOf(Layer.ImageLayer(id = nextLayerId, image = image)) + layers,
        nextLayerId = nextLayerId.next()
    )

    fun findLayer(offset: Offset): Layer? = layers.fastFirstOrNull {
        val layerTransformation =
            transformations.getTransformation(it.id, Transformation.IDENTITY)
        val layerRect = it.getRect(layerTransformation)
        layerRect.contains(offset)
    }

    fun selectedLayerOrCanvas() = layers.fastFirstOrNull { it.isSelected } ?: this

    fun selectLayer(offset: Offset): CanvasState {
        val layerTransformation = transformations.getTransformation(id, Transformation.IDENTITY)
        val canvasOffset = offset - layerTransformation.centerOffset
        val layer = findLayer(canvasOffset)
        return copy(layers = layers.fastMap { it.setSelected(it == layer) })
    }

    fun startDrag(): CanvasState = copy(
        transformations = transformations.startOffsetTransformation(
            selectedLayerOrCanvas().id,
            Offset.Zero
        )
    )

    fun updateDrag(offset: Offset): CanvasState = copy(
        transformations = transformations.adjustOffsetTransformation(offset)
    )

    fun endDrag(): CanvasState = copy(
        transformations = transformations.commitOffsetTransformation()
    )

    fun cancelDrag(): CanvasState = copy(
        transformations = transformations.cancelOffsetTransformation()
    )

    fun export(): ImageBitmap {
        val drawScope = CanvasDrawScope()
        return drawScope.asBitmap(size) {
            draw(Transformation.Companion.IDENTITY)
        }
    }

    override fun getRect(parentTransformation: Transformation): Rect =
        parentTransformation.getRect(size)

    /*
        Draw
     */

    override fun DrawScope.draw(parentTransformation: Transformation) {
        val transformation =
            transformations.getTransformation(this@CanvasState.id, parentTransformation)
        // Draw background
        drawCanvas(transformation)
        drawLayers(transformation)
    }

    private fun DrawScope.drawCanvas(transformation: Transformation) {
        val rect = transformation.getRect(this@CanvasState.size)

        drawRect(
            background,
            topLeft = rect.topLeft,
            size = rect.size
        )
    }

    private fun DrawScope.drawLayers(parentTransformation: Transformation) =
        layers.fastForEachReversed {
            with(it) {
                val transformation =
                    transformations.getTransformation(it.id, parentTransformation)
                draw(transformation)
            }
        }

}


