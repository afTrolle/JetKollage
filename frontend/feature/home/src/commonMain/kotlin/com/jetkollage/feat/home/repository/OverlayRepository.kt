package com.jetkollage.feat.home.repository

import com.jetkollage.api.NetworkResult
import com.jetkollage.api.SCRLClient
import com.jetkollage.persistance.naiveCache
import com.russhwolf.settings.Settings

class OverlayRepository(
    val settings: Settings,
    val client: SCRLClient,
) {

    fun overlayFlow() =
        settings.naiveCache(KEY_OVERLAY, defaultValue = emptyList()) {
            val response = client.getOverlays()
            if (response is NetworkResult.Success) {
                response.data
            } else {
                error("Failed to fetch overlays")
            }
        }

    companion object {
        const val KEY_OVERLAY = "overlay"
    }
}

