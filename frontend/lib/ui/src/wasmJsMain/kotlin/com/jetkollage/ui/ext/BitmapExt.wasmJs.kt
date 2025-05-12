package com.jetkollage.ui.ext

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import coil3.Bitmap
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image

actual fun Bitmap.composeBitmap(): ImageBitmap  = asComposeImageBitmap()

actual suspend fun ImageBitmap.toBytes(): ByteArray? =
    Image.makeFromBitmap(asSkiaBitmap()).encodeToData(EncodedImageFormat.PNG, 90)?.bytes
