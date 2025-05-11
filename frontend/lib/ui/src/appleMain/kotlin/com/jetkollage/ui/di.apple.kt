package com.jetkollage.ui

import coil3.PlatformContext
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val nativeUiModule: Module = module {

    single {
        PlatformContext.INSTANCE
    }

}