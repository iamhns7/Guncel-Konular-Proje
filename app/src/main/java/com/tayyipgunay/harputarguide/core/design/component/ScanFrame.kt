package com.tayyipgunay.harputarguide.core.design.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ScanFrame(
    modifier: Modifier = Modifier,
    cornerColor: Color = Color.White,
    strokeWidth: Dp = 3.dp,
    cornerLength: Dp = 28.dp
) {
    Canvas(modifier = modifier) {
        val stroke = strokeWidth.toPx()
        val length = cornerLength.toPx()
        val w = size.width
        val h = size.height

        fun drawCorner(topLeftX: Float, topLeftY: Float, flipX: Int, flipY: Int) {
            drawLine(
                color = cornerColor,
                start = Offset(topLeftX, topLeftY),
                end = Offset(topLeftX + length * flipX, topLeftY),
                strokeWidth = stroke,
                cap = StrokeCap.Round
            )
            drawLine(
                color = cornerColor,
                start = Offset(topLeftX, topLeftY),
                end = Offset(topLeftX, topLeftY + length * flipY),
                strokeWidth = stroke,
                cap = StrokeCap.Round
            )
        }

        drawCorner(0f, 0f, 1, 1)
        drawCorner(w, 0f, -1, 1)
        drawCorner(0f, h, 1, -1)
        drawCorner(w, h, -1, -1)
    }
}
