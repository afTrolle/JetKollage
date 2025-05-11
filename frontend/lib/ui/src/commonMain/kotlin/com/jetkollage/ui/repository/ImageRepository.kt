package com.jetkollage.ui.repository

import androidx.compose.ui.graphics.ImageBitmap
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.request.ImageRequest
import coil3.toBitmap
import com.jetkollage.ui.ext.composeBitmap
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.concurrent.Volatile

class ImageRepository(
    private val platformContext: PlatformContext,
    private val imageLoader: ImageLoader,
) {

    // TODO Improve cache handling, WeakValues?
    @Volatile
    var inMemoryCache = mapOf<Any, ImageBitmap>()
    val mutex = Mutex()

    suspend fun fetchImage(
        data: Any? = "https://media.istockphoto.com/id/1316134499/photo/a-concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-example-zoom-inside-the.jpg?s=612x612&w=0&k=20&c=sZM5HlZvHFYnzjrhaStRpex43URlxg6wwJXff3BE9VA="
    ): ImageBitmap? {
        val cached = inMemoryCache[data]
        if (cached != null) return cached

        val request = ImageRequest
            .Builder(platformContext)
            .data(data)
            .build()

        return imageLoader.execute(request)
            .image
            ?.toBitmap()
            ?.composeBitmap()
            ?.also {
                if (data != null) {
                    mutex.withLock {
                        inMemoryCache = inMemoryCache.plus(data to it)
                    }
                }
            }
    }

}