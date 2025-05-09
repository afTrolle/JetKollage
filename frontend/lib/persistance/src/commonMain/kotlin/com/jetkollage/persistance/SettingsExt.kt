package com.jetkollage.persistance

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.serialization.decodeValueOrNull
import com.russhwolf.settings.serialization.encodeValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.cancellation.CancellationException


@OptIn(ExperimentalSettingsApi::class)
inline fun <reified T> Settings.naiveCache(
    key: String,
    defaultValue: T,
    noinline fetch: suspend () -> T
): Flow<T> = flow {
    val storedValue = decodeValueOrNull<T>(key = key) ?: defaultValue

    storedValue?.let {
        emit(it)
    }

    runCatching {
        fetch()
    }.onSuccess {
        emit(it)
        encodeValue<T>(key = key, value = it)
    }.onFailure {
        if (it is CancellationException) throw it
    }
}