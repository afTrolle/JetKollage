package com.jetkollage.nav

import org.koin.dsl.binds
import org.koin.dsl.module

val navigationModule = module {
    single { NavigationServiceImpl() } binds arrayOf(
        NavigationService::class,
        Navigator::class
    )
}