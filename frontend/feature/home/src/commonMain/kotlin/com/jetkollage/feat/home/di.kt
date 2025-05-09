package com.jetkollage.feat.home

import com.jetkollage.feat.home.repository.OverlayRepository
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    single { OverlayRepository(get(), get()) }
    viewModel { HomeViewModel(get()) }
}