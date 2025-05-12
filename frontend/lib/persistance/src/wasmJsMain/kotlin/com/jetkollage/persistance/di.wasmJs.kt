package com.jetkollage.persistance

import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

actual val persistenceModule: Module = module {

    single {
        Settings()
    } bind Settings::class

}
