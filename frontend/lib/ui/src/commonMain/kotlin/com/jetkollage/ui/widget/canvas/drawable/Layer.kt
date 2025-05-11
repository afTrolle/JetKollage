package com.jetkollage.ui.widget.canvas.drawable

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.jetkollage.ui.widget.canvas.Transformation

sealed interface Layer : Drawable {

    val isSelected: Boolean

    fun setSelected(isSelected: Boolean): Layer

    data class ImageLayer(
        override val id: DrawableId,
        override val isSelected: Boolean = false,
        val image: ImageBitmap,
    ) : Layer {
        val imageSize = Size(image.width.toFloat(), image.height.toFloat())

        override fun getRect(parentTransformation: Transformation): Rect =
            parentTransformation.getRect(imageSize)

        override fun setSelected(isSelected: Boolean): Layer = copy(isSelected = isSelected)

        override fun DrawScope.draw(parentTransformation: Transformation) {
            val size = getRect(parentTransformation)
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