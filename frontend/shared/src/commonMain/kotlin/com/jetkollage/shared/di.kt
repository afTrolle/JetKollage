package com.jetkollage.shared

import com.jetkollage.api.apiModule
import com.jetkollage.feat.home.homeModule
import com.jetkollage.nav.navigationModule
import com.jetkollage.persistance.persistenceModule
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module

val koinConfiguration = koinConfiguration {
    modules(
        appModule,
        homeModule,
        persistenceModule,
        navigationModule,
        apiModule,
    )
}

val appModule = module {

}