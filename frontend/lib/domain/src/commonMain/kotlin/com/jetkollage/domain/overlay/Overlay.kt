package com.jetkollage.domain.overlay

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable


@Immutable
@Serializable
data class OverlayCategory(
    val name: String,
    val items: List<OverlayItem>,
)

@Immutable
@Serializable
data class OverlayItem(
    val id: String,
    val name: String,
    val url: String
)