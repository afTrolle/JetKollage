package com.jetkollage.ui.widget.canvas.drawable

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform

sealed interface Layer : Drawable {

    val isSelected: Boolean

    fun setSelected(isSelected: Boolean): Layer


    data class ImageLayer(
        override val id: DrawableId,
        override val isSelected: Boolean = false,
        val image: ImageBitmap,
    ) : Layer {

        override val intrinsicSize: Size = Size(image.width.toFloat(), image.height.toFloat())

        override fun setSelected(isSelected: Boolean): Layer = copy(isSelected = isSelected)

        override val draw: DrawScope.(Matrix) -> Unit get() =  { matrix ->
            withTransform({
                transform(matrix)
            }) {
                drawImage(image = image)
                if (isSelected) {
                    drawRect(
                        color = Color.Red.copy(alpha = 0.2f),
                        size = intrinsicSize // Original size, it will be scaled by the transform
                    )
                }
            }
        }
    }
}
