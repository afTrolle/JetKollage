package com.jetkollage.ui.ext

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import coil3.Bitmap

actual fun Bitmap.composeBitmap(): ImageBitmap = asImageBitmap()