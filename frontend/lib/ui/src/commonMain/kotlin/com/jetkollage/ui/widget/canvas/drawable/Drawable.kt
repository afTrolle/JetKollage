package com.jetkollage.ui.widget.canvas.drawable

import androidx.compose.ui.graphics.drawscope.DrawScope
import com.jetkollage.ui.widget.canvas.Transformation

interface Drawable {
    fun DrawScope.draw(parentTransformation: Transformation)
}

