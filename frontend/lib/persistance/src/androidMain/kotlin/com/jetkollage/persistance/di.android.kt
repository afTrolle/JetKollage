package com.jetkollage.persistance

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toBlockingSettings
import com.russhwolf.settings.datastore.DataStoreSettings
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class, ExperimentalSettingsImplementation::class)
actual val persistenceModule: Module = module {

    single {
        DataStoreSettings(androidContext().dataStore)
    } bind FlowSettings::class

    single {
        get<FlowSettings>().toBlockingSettings()
    } bind Settings::class

}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
