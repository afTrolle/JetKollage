package com.jetkollage.shared

import com.jetkollage.api.apiModule
import com.jetkollage.feat.home.homeModule
import com.jetkollage.nav.navigationModule
import com.jetkollage.persistance.persistenceModule
import com.jetkollage.ui.uiModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes
import org.koin.dsl.module

val appModule = module {

}

fun initKoin(config : KoinAppDeclaration? = null){
    startKoin {
        printLogger()
        includes(config)
        modules(
            appModule,
            homeModule,
            persistenceModule,
            navigationModule,
            apiModule,
            uiModule,
        )
    }
}