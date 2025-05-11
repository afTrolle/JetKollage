package com.jetkollage.ui.widget.canvas.drawable

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.DrawScope
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
    val intrinsicSize: Size
    val draw: DrawScope.(Matrix) -> Unit

}

