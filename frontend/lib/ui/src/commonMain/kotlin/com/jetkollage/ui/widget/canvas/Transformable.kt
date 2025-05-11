package com.jetkollage.ui.widget.canvas

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import com.jetkollage.ui.widget.canvas.drawable.Drawable
import kotlin.jvm.JvmInline


/**
 * Contains all the transformations applied to the drawables
 * TODO setup flatten structure to save compute & sliding window to save memory
 */
data class TransformationsContainer(
    val transformations: List<Pair<Drawable, TransformationOperation>> = emptyList()
) {

    private fun getTransformations(drawable: Drawable): List<TransformationOperation> =
        transformations.filter { it.first == drawable }.map { it.second }

    fun getTransformation(
        drawable: Drawable,
        parentTransformation: Transformation
    ): Transformation {
        // Drawable transformations
        val transformations = getTransformations(drawable)
        val zoom = transformations.getZoom()
        val offset = transformations.getOffset()

        return Transformation(
            zoom = parentTransformation.zoom * zoom,
            centerOffset = parentTransformation.centerOffset + offset
        )
    }

}

private fun List<TransformationOperation>.getZoom(): Float =
    filterIsInstance<TransformationOperation.Zoom>()
        .fold(1f) { acc, task -> acc * task.value }

private fun List<TransformationOperation>.getOffset(): Offset =
    filterIsInstance<TransformationOperation.Offset>()
        .fold(Offset.Zero) { acc, task -> acc + task.value }

sealed interface TransformationOperation {

    @JvmInline
    value class Offset(val value: androidx.compose.ui.geometry.Offset) : TransformationOperation

    @JvmInline
    value class Zoom(val value: Float) : TransformationOperation

}

@Immutable
data class Transformation(
    val zoom: Float = 1f,
    val centerOffset: Offset = Offset.Zero,
) {

    fun getSize(size: Size) = size * zoom

    fun getRect(size: Size) = getSize(size).let {
        val topLeft = centerOffset - it.center
        Rect(topLeft, it)
    }

    companion object {
        val IDENTITY = Transformation(1f, Offset.Zero)
    }
}
