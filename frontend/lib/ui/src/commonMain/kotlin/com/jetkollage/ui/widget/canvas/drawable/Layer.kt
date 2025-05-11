package com.jetkollage.ui.widget.canvas.drawable

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.jetkollage.ui.widget.canvas.Transformation

sealed interface Layer : Drawable {
    val isSelected: Boolean

    data class ImageLayer(
        override val isSelected: Boolean = false,
        val image: ImageBitmap,
    ) : Layer {
        val imageSize = Size(image.width.toFloat(), image.height.toFloat())

        override fun DrawScope.draw(parentTransformation: Transformation) {
            val size = parentTransformation.getRect(imageSize)

            drawImage(
                image = image,
                topLeft = size.topLeft,
            )
            if (isSelected) {
                drawRect(
                    Color.Companion.Red.copy(alpha = 0.2f),
                    topLeft = size.topLeft,
                    size = imageSize
                )
            }
        }
    }
}