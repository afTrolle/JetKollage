package com.jetkollage.ui

import coil3.annotation.DelicateCoilApi
import com.jetkollage.ui.repository.ImageRepository
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val nativeUiModule: Module

@OptIn(DelicateCoilApi::class)
val uiModule: Module = module {
    includes(nativeUiModule)

    single { ImageRepository(get(), get()) }

}
