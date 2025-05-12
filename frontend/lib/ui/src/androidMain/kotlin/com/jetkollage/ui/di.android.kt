package com.jetkollage.ui

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.allowHardware
import coil3.request.crossfade
import io.github.vinceglb.filekit.coil.addPlatformFileSupport
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val nativeUiModule: Module = module {

    single<PlatformContext> {
        androidApplication() as PlatformContext
    }

    single(createdAtStart = true) {
        ImageLoader.Builder(get())
            .crossfade(true)
            .allowHardware(false)
            .components {
                addPlatformFileSupport()
            }
            .build()
            .also {
                SingletonImageLoader.setUnsafe(it)
            }
    }

}