package com.jetkollage

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform