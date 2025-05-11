package com.jetkollage.ui.widget.canvas.drawable

import androidx.compose.ui.geometry.Offset

data class SnapGuide(
    val start: Offset,
    val end: Offset,
    val correction: Offset,
) {
    val stroke = (correction).getDistance()
}