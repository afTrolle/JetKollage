package com.jetkollage.ui.ext

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import coil3.Bitmap
import java.io.ByteArrayOutputStream

actual fun Bitmap.composeBitmap(): ImageBitmap = asImageBitmap()

actual suspend fun ImageBitmap.toBytes(): ByteArray? {
    val outputStream = ByteArrayOutputStream()
    val result = asAndroidBitmap().compress(
        android.graphics.Bitmap.CompressFormat.PNG,
        90,
        outputStream
    )
    if (!result) return null
    return outputStream.toByteArray()
}