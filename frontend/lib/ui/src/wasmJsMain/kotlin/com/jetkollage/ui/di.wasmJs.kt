package com.jetkollage.ui

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.request.crossfade
import io.github.vinceglb.filekit.coil.addPlatformFileSupport
import org.koin.core.module.Module
import org.koin.dsl.module

@OptIn(DelicateCoilApi::class)
actual val nativeUiModule: Module = module {

    single {
        PlatformContext.INSTANCE
    }

    single(createdAtStart = true) {
        ImageLoader.Builder(get())
            .crossfade(true)
            .components {
                addPlatformFileSupport()
            }
            .build()
            .also {
                SingletonImageLoader.setUnsafe(it)
            }
    }

}