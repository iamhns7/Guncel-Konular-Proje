package com.tayyipgunay.harputarguide.feature.ar

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate

@Composable
fun ArTrackingVisualOverlay(
    trackingState: ArTrackingState,
    modifier: Modifier = Modifier
) {
    val infinite = rememberInfiniteTransition(label = "ar_tracking_visual")
    val scanProgress by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scan_line"
    )
    val pulse by infinite.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "corner_pulse"
    )
    val lockFlash by infinite.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lock_flash"
    )
    val gridShift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 28f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "grid_shift"
    )

    val (frameColor, cornerAlpha, showScanLine, borderAlpha) = when (trackingState) {
        ArTrackingState.TRACKING -> Quad(
            Color(0xFF5CFF8A),
            lockFlash,
            false,
            0.6f * lockFlash
        )
        ArTrackingState.ALIGNED -> Quad(
            Color(0xFF9AE6B0),
            lockFlash * 0.9f,
            false,
            0.45f * lockFlash
        )
        ArTrackingState.LOST -> Quad(
            Color(0xFFFFC857),
            pulse * 0.85f,
            false,
            0.3f * pulse
        )
        else -> Quad(
            Color(0xFFB8F5FF),
            pulse,
            true,
            0.22f * pulse
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val inset = 8f
        val rect = Rect(inset, inset, size.width - inset, size.height - inset)
        val cornerLen = size.minDimension * 0.16f

        val gridStep = 28f
        var x = -gridShift
        while (x < size.width) {
            drawLine(
                color = frameColor.copy(alpha = 0.06f),
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 1f
            )
            x += gridStep
        }
        var y = -gridShift
        while (y < size.height) {
            drawLine(
                color = frameColor.copy(alpha = 0.06f),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f
            )
            y += gridStep
        }

        drawRoundRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    frameColor.copy(alpha = borderAlpha),
                    frameColor.copy(alpha = borderAlpha * 0.35f)
                )
            ),
            topLeft = rect.topLeft,
            size = rect.size,
            cornerRadius = CornerRadius(12f, 12f),
            style = Stroke(
                width = if (trackingState == ArTrackingState.TRACKING) 2.6f else 2f,
                pathEffect = if (trackingState == ArTrackingState.SEARCHING) {
                    PathEffect.dashPathEffect(floatArrayOf(16f, 10f), scanProgress * 40f)
                } else {
                    null
                }
            )
        )

        fun drawCorner(origin: Offset, right: Boolean, down: Boolean) {
            val hx = if (right) cornerLen else -cornerLen
            val vy = if (down) cornerLen else -cornerLen
            val color = frameColor.copy(alpha = cornerAlpha)
            drawLine(color, origin, Offset(origin.x + hx, origin.y), 3.5f, StrokeCap.Round)
            drawLine(color, origin, Offset(origin.x, origin.y + vy), 3.5f, StrokeCap.Round)
        }

        drawCorner(rect.topLeft, right = true, down = true)
        drawCorner(Offset(rect.right, rect.top), right = false, down = true)
        drawCorner(Offset(rect.left, rect.bottom), right = true, down = false)
        drawCorner(Offset(rect.right, rect.bottom), right = false, down = false)

        if (showScanLine) {
            val yLine = rect.top + rect.height * scanProgress
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        frameColor.copy(alpha = 0.9f),
                        Color.White.copy(alpha = 0.55f),
                        frameColor.copy(alpha = 0.9f),
                        Color.Transparent
                    ),
                    startX = rect.left,
                    endX = rect.right
                ),
                start = Offset(rect.left, yLine),
                end = Offset(rect.right, yLine),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = frameColor.copy(alpha = 0.25f),
                start = Offset(rect.left, yLine - 8f),
                end = Offset(rect.right, yLine - 8f),
                strokeWidth = 12f,
                cap = StrokeCap.Round
            )
        }

        if (trackingState == ArTrackingState.TRACKING || trackingState == ArTrackingState.ALIGNED) {
            rotate(degrees = scanProgress * 360f, pivot = rect.center) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            frameColor.copy(alpha = 0.18f),
                            Color.Transparent
                        ),
                        center = rect.center,
                        radius = rect.maxDimension * 0.58f
                    ),
                    radius = rect.maxDimension * 0.58f,
                    center = rect.center
                )
            }
            drawCircle(
                color = frameColor.copy(alpha = 0.12f * lockFlash),
                radius = rect.maxDimension * 0.42f,
                center = rect.center,
                style = Stroke(width = 1.5f)
            )
        }
    }
}

private data class Quad(
    val color: Color,
    val cornerAlpha: Float,
    val showScanLine: Boolean,
    val borderAlpha: Float
)
