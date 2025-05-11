package com.jetkollage.ui.widget.canvas.drawable

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.util.fastRoundToInt
import com.jetkollage.ui.ext.asBitmap
import com.jetkollage.ui.widget.canvas.Transformation
import com.jetkollage.ui.widget.canvas.TransformationsContainer

/**
 * Virtual Canvas,
 */
data class CanvasState(
    val size: Size = Size(400f, 400f), // Resolution
    val background: Color = Color.Companion.White,
    val layers: List<Drawable> = emptyList(),
    val transformationContainer: TransformationsContainer = TransformationsContainer(),
) : Drawable {

    fun export(): ImageBitmap {
        val drawScope = CanvasDrawScope()
        return drawScope.asBitmap(size) {
            draw(Transformation.Companion.IDENTITY)
        }
    }

    /*
        Draw
     */
    override fun DrawScope.draw(parentTransformation: Transformation) {
        val transformation = transformationContainer.getTransformation(this@CanvasState, parentTransformation)
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
        layers.forEach {
            with(it) {
                val transformation =
                    transformationContainer.getTransformation(it, parentTransformation)
                draw(transformation)
            }
        }

}