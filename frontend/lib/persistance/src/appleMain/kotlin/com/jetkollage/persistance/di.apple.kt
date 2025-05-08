package com.jetkollage.persistance

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toFlowSettings
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

@OptIn(ExperimentalSettingsApi::class)
actual val persistenceModule: Module = module {

    single {
         NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)
    } bind Settings::class

    single {
        get<NSUserDefaultsSettings>().toFlowSettings()
    }
}