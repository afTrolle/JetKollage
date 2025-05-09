package com.jetkollage.feat.home.repository

import com.jetkollage.api.NetworkResult
import com.jetkollage.api.SCRLClient
import com.jetkollage.api.model.SCRLOverlayCategory
import com.jetkollage.domain.overlay.OverlayCategory
import com.jetkollage.domain.overlay.OverlayItem
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
                response.data.toCategory()
            } else {
                error("Failed to fetch overlays")
            }
        }

    companion object {
        const val KEY_OVERLAY = "overlay"
    }
}

fun List<SCRLOverlayCategory>.toCategory() = map { category ->
    OverlayCategory(
        name = category.title,
        items = category.items.map { item ->
            OverlayItem(
                id = "${category.id}-${item.id}-${item.overlayName}",
                name = item.overlayName,
                url = item.sourceUrl,
            )
        }
    )
}

