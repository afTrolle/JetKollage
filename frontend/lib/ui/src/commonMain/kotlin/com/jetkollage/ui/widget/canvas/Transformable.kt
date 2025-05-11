package com.jetkollage.ui.widget.canvas

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import com.jetkollage.ui.widget.canvas.drawable.Drawable
import com.jetkollage.ui.widget.canvas.drawable.DrawableId
import kotlin.jvm.JvmInline


/**
 * Contains all the transformations applied to the drawables
 * TODO setup flatten structure to save compute & sliding window to save memory
 */
data class TransformationsContainer(
    val transformations: List<Pair<DrawableId, TransformationOperation>> = emptyList(),
    val temporaryOffset: Pair<DrawableId, TransformationOperation.Offset>? = null,
) {

    private fun getTemporaryTransformations(drawable: DrawableId) = listOfNotNull(
        temporaryOffset.takeIf { it?.first == drawable }?.second
    )

    private fun getTransformations(drawable: DrawableId): List<TransformationOperation> =
        transformations.filter { it.first == drawable }.map { it.second }

    fun getTransformation(
        drawable: DrawableId,
        parentTransformation: Transformation,
    ): Transformation {
        // Drawable transformations
        val transformations = getTransformations(drawable) + getTemporaryTransformations(drawable)
        val zoom = transformations.getZoom()
        val offset = transformations.getOffset()

        return Transformation(
            zoom = parentTransformation.zoom * zoom,
            centerOffset = parentTransformation.centerOffset + offset
        )
    }

    fun startOffsetTransformation(drawable: DrawableId, offset: Offset) = copy(
        temporaryOffset = drawable to TransformationOperation.Offset(offset)
    )

    fun adjustOffsetTransformation(offset: Offset) = copy(
        temporaryOffset = temporaryOffset?.let { it + offset }
    )

    fun commitOffsetTransformation() = temporaryOffset?.let {
        copy(
            transformations = transformations.plus(it),
            temporaryOffset = null,
        )
    } ?: this

    fun cancelOffsetTransformation() = copy(temporaryOffset = null)

}

operator fun Pair<DrawableId, TransformationOperation.Offset>.plus(other: Offset): Pair<DrawableId, TransformationOperation.Offset> =
    copy(second = second + TransformationOperation.Offset(other))


private fun List<TransformationOperation>.getZoom(): Float =
    filterIsInstance<TransformationOperation.Zoom>()
        .fold(1f) { acc, task -> acc * task.value }

private fun List<TransformationOperation>.getOffset(): Offset =
    filterIsInstance<TransformationOperation.Offset>()
        .fold(Offset.Zero) { acc, task -> acc + task.value }

sealed interface TransformationOperation {

    @JvmInline
    value class Offset(val value: androidx.compose.ui.geometry.Offset) : TransformationOperation {

        operator fun plus(other: Offset): Offset = Offset(value + other.value)
        operator fun minus(other: Offset): Offset = Offset(value - other.value)
    }

    @JvmInline
    value class Zoom(val value: Float) : TransformationOperation {
        init {
            require(value > 0) { "Zoom must be positive" }
        }
    }

}


@Immutable
data class Transformation(
    val zoom: Float = 1f,
    val centerOffset: Offset = Offset.Zero,
) {
    init {
        require(zoom > 0) { "Zoom must be positive" }
    }

    fun getSize(size: Size) = size * zoom

    fun getRect(size: Size) = getSize(size).let {
        val topLeft = centerOffset - it.center
        Rect(topLeft, it)
    }

    companion object {
        val IDENTITY = Transformation(1f, Offset.Zero)
    }
}
