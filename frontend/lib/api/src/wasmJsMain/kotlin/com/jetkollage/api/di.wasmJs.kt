package com.jetkollage.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.module.Module
import org.koin.dsl.module

actual val nativeApiModule: Module = module {

    single {
        HttpClient(Js) {

            install(ContentNegotiation) {
                json()
            }
        }
    }
}