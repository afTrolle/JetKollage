package com.jetkollage.ui

import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.crossfade
import org.koin.dsl.module

val uiModule = module {

    single {
    //    SingletonImageLoader.get(PlatformContext(get()))
       // ImageLoader.Builder(LocalContext.current)
    }

}