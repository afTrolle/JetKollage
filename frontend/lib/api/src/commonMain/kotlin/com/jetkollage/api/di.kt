package com.jetkollage.api

import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val nativeApiModule : Module

val apiModule = module {
    includes(nativeApiModule)

    single { SCRLConfig() }
    single { SCRLClient(get(), get()) }
}