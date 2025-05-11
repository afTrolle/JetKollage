package com.jetkollage.ui.widget.canvas.drawable

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.jetkollage.ui.widget.canvas.Transformation
import kotlin.jvm.JvmInline


@JvmInline
value class DrawableId(val value: Int) {

    fun next() = DrawableId(value + 1)

    companion object {
        val MainCanvas = DrawableId(0)
    }
}

interface Drawable {

    val id: DrawableId

    fun DrawScope.draw(parentTransformation: Transformation)

    fun getRect(parentTransformation: Transformation): Rect
}

