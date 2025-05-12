package com.jetkollage.ui.ext

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import coil3.Bitmap

expect fun Bitmap.composeBitmap(): ImageBitmap

fun CanvasDrawScope.asBitmap(size: Size, onDraw: DrawScope.() -> Unit): ImageBitmap {
    val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
    draw(
        Density(1f),
        LayoutDirection.Ltr,
        androidx.compose.ui.graphics.Canvas(bitmap),
        size
    ) { onDraw() }
    return bitmap
}

expect suspend fun ImageBitmap.toBytes() : ByteArray?

