package com.jetkollage.api.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OverlayItems(
    @SerialName("id")
    val id: Int,
    @SerialName("items")
    val items: List<OverlayItem>,
    @SerialName("thumbnail_url")
    val thumbnailUrl: String,
    @SerialName("title")
    val title: String
)

@Serializable
data class OverlayItem(
    @SerialName("category_id")
    val categoryId: Int,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("id")
    val id: Int,
    @SerialName("includes_scrl_branding")
    val includesScrlBranding: Boolean,
    @SerialName("is_premium")
    val isPremium: Boolean,
    @SerialName("max_canvas_size")
    val maxCanvasSize: Int,
    @SerialName("overlay_name")
    val overlayName: String,
    @SerialName("premium_uses_last_48hrs")
    val premiumUsesLast48hrs: Int,
    @SerialName("source_url")
    val sourceUrl: String
)