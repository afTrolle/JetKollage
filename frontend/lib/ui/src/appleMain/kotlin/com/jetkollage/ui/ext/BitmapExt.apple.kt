package com.jetkollage.ui.ext

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import coil3.Bitmap

actual fun Bitmap.composeBitmap(): ImageBitmap  = asComposeImageBitmap()
