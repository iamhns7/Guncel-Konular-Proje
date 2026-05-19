package com.tayyipgunay.harputarguide.core.design.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun ArFocusFrame(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    cornerLength: Float = 36f,
    strokeWidth: Float = 3f
) {
    Box(modifier = modifier.size(200.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            fun drawCorner(topLeft: Offset, horizontalRight: Boolean, verticalDown: Boolean) {
                val hx = if (horizontalRight) cornerLength else -cornerLength
                val vy = if (verticalDown) cornerLength else -cornerLength
                drawLine(color, topLeft, Offset(topLeft.x + hx, topLeft.y), strokeWidth, StrokeCap.Round)
                drawLine(color, topLeft, Offset(topLeft.x, topLeft.y + vy), strokeWidth, StrokeCap.Round)
            }

            drawCorner(Offset(0f, 0f), horizontalRight = true, verticalDown = true)
            drawCorner(Offset(w, 0f), horizontalRight = false, verticalDown = true)
            drawCorner(Offset(0f, h), horizontalRight = true, verticalDown = false)
            drawCorner(Offset(w, h), horizontalRight = false, verticalDown = false)
        }
    }
}
