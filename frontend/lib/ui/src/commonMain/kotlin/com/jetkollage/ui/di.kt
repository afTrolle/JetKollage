package com.jetkollage.ui

import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.request.crossfade
import com.jetkollage.ui.repository.ImageRepository
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val nativeUiModule: Module

@OptIn(DelicateCoilApi::class)
val uiModule: Module = module {
    includes(nativeUiModule)

    single(createdAtStart = true) {
        ImageLoader.Builder(get())
            .crossfade(true)
            .build()
            .also {
                SingletonImageLoader.setUnsafe(it)
            }
    }

    single { ImageRepository(get(), get()) }

}
