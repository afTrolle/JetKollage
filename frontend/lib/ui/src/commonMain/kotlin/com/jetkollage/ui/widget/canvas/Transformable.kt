package com.jetkollage.ui.widget.canvas

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import com.jetkollage.ui.widget.canvas.drawable.DrawableId
import kotlin.jvm.JvmInline


/**
 * Contains all the transformations applied to the drawables
 * TODO setup flatten structure to save compute & sliding window to save memory
 */
data class TransformationsContainer(
    val transformations: List<Pair<DrawableId, TransformationOperation>> = emptyList(),
    val temporaryOffset: Pair<DrawableId, TransformationOperation.Offset>? = null,
    val temporaryZoom: Pair<DrawableId, TransformationOperation.Zoom>? = null,
) {

    private val temporaryTransformations = listOfNotNull(temporaryOffset, temporaryZoom)
    private val allTransforms = transformations + temporaryTransformations

    private fun getTransformations(drawable: DrawableId): List<TransformationOperation> =
        allTransforms.filter { it.first == drawable }.map { it.second }

    fun getTransformation(drawable: DrawableId): Transformation =
        getTransformations(drawable).fold(Transformation.IDENTITY) { acc, operation ->
            when (operation) {
                is TransformationOperation.Offset -> acc.copy(centerOffset = acc.centerOffset + operation.value)
                is TransformationOperation.Zoom -> acc.copy(zoom = acc.zoom * operation.value)
            }
        }

    fun transformationsInProgress(drawableId: DrawableId) = temporaryTransformations.find { it.first == drawableId } != null

    /**
     * offset transformation
     */

    fun startOffsetTransformation(drawable: DrawableId?, offset: Offset) =
        if (drawable == null) this else copy(
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

    /*
     * Zoom transformation
     */

    fun commitZoomTransformation() =
        temporaryZoom?.let {
            copy(
                transformations = transformations.plus(temporaryZoom),
                temporaryZoom = null
            )
        } ?: this

    fun cancelZoomTransformation() = copy(temporaryZoom = null)

    fun adjustZoomTransformation(zoom: Float) = temporaryZoom?.let {
        copy(temporaryZoom = it.first to TransformationOperation.Zoom(it.second.value * zoom))
    } ?: this

    fun startZoomTransformation(id: DrawableId?): TransformationsContainer =
        if (id == null) this else copy(
            temporaryZoom = id to TransformationOperation.Zoom(1f)
        )

}

operator fun Pair<DrawableId, TransformationOperation.Offset>.plus(other: Offset): Pair<DrawableId, TransformationOperation.Offset> =
    copy(second = second + TransformationOperation.Offset(other))


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

    fun getMatrix(
        intrinsicSize: Size,
    ) = Matrix().apply {
        scale(
            x = zoom,
            y = zoom,
        )
        translate(
            x = centerOffset.x - intrinsicSize.width / 2,
            y = centerOffset.y - intrinsicSize.height / 2,
        )
    }

    companion object {
        val IDENTITY = Transformation(1f, Offset.Zero)
    }
}
