package com.jetkollage.shared.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun CoroutineScope.launchOnStarted(
    lifecycleOwner: LifecycleOwner,
    block: suspend CoroutineScope.() -> Unit
) = launch(Dispatchers.Main.immediate) {
    lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
}