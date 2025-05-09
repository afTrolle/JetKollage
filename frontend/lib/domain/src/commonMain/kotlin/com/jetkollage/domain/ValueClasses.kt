package com.jetkollage.domain

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline


@Immutable
@Serializable
data class Coordinates(val x: Float, val y: Float) {
    operator fun plus(other: Coordinates) = Coordinates(x + other.x, y + other.y)
    operator fun minus(other: Coordinates) = Coordinates(x - other.x, y - other.y)
    operator fun times(scalar: Float) = Coordinates(x * scalar, y * scalar)
}

@Immutable
@Serializable
data class Dimensions(val width: Float, val height: Float) {
    init {
        require(width >= 0f) { "Width cannot be negative" }
        require(height >= 0f) { "Height cannot be negative" }
    }
}

@Immutable
@Serializable
@JvmInline
value class Angle(val degrees: Float) {
    operator fun plus(other: Angle) = Angle(degrees + other.degrees)
    operator fun minus(other: Angle) = Angle(degrees - other.degrees)
}

@JvmInline
value class ScaleFactor(val factor: Float) {
    init {
        require(factor > 0f) { "Scale factor must be positive" }
    }

    operator fun times(other: ScaleFactor) = ScaleFactor(factor * other.factor)
}

@Immutable
@Serializable
sealed class Transform() {
    data class Position(val coordinates: Coordinates) : Transform()
    data class Scale(val scale: ScaleFactor) : Transform()
    data class Rotation(val rotation: Angle) : Transform()
}

@Immutable
@Serializable
data class DrawableState(
    val coordinates: Coordinates,
    val rotation: Angle,
    val scale: ScaleFactor,
    val dimensions: Dimensions,
    val source: String,
    val transformations: List<Transform>
) {

    val transformedDimensions = transformations.fold(dimensions) { acc, transform ->
        when (transform) {
            is Transform.Scale -> Dimensions(
                acc.width * transform.scale.factor,
                acc.height * transform.scale.factor
            )

            else -> acc
        }
    }

}

@Immutable
@Serializable
data class Bounds(
    val coordinates: Coordinates,
    val dimensions: Dimensions,
)

@Immutable
@Serializable
data class ViewportState(
    val zoom: Float = 1.0f,
    val panOffset: Coordinates = Coordinates(0f, 0f) // Offset of canvas origin from viewport origin
)


@Immutable
@Serializable
data class CanvasBoundary(
    val topLeft: Coordinates,
    val dimensions: Dimensions
) {
    val center: Coordinates
        get() = Coordinates(
            topLeft.x + dimensions.width / 2,
            topLeft.y + dimensions.height / 2
        )
}