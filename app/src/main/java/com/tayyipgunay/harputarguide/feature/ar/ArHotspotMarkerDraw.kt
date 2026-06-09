package com.tayyipgunay.harputarguide.feature.ar

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

internal object ArHotspotMarkerDraw {

    fun drawMarker(
        scope: DrawScope,
        center: Offset,
        accent: Color,
        accentDim: Color,
        isSelected: Boolean,
        ringRadius: Float,
        ripplePhase: Float,
        orbitDegrees: Float,
        trackingLocked: Boolean
    ) {
        val baseRadius = ringRadius * if (isSelected) 1.08f else 1f

        scope.drawOval(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.Black.copy(alpha = if (isSelected) 0.45f else 0.28f),
                    Color.Transparent
                ),
                center = Offset(center.x, center.y + baseRadius * 0.55f),
                radius = baseRadius * 1.1f
            ),
            topLeft = Offset(center.x - baseRadius * 0.9f, center.y + baseRadius * 0.15f),
            size = androidx.compose.ui.geometry.Size(baseRadius * 1.8f, baseRadius * 0.55f)
        )

        repeat(3) { wave ->
            val phase = ((ripplePhase + wave * 0.28f) % 1f)
            val waveRadius = baseRadius * (1.05f + phase * 2.2f)
            val alpha = (1f - phase) * if (isSelected) 0.42f else 0.28f
            scope.drawCircle(
                color = accent.copy(alpha = alpha),
                radius = waveRadius,
                center = center,
                style = Stroke(width = if (isSelected) 2.2f else 1.6f)
            )
        }

        scope.rotate(orbitDegrees, center) {
            scope.drawCircle(
                color = accent.copy(alpha = if (trackingLocked) 0.55f else 0.35f),
                radius = baseRadius * 1.55f,
                center = center,
                style = Stroke(
                    width = 1.4f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 14f), 0f)
                )
            )
            val tickCount = 8
            repeat(tickCount) { index ->
                val angle = (360f / tickCount) * index
                val rad = angle * PI.toFloat() / 180f
                val inner = baseRadius * 1.38f
                val outer = baseRadius * 1.52f
                val start = Offset(
                    center.x + cos(rad) * inner,
                    center.y + sin(rad) * inner
                )
                val end = Offset(
                    center.x + cos(rad) * outer,
                    center.y + sin(rad) * outer
                )
                scope.drawLine(
                    color = accent.copy(alpha = 0.75f),
                    start = start,
                    end = end,
                    strokeWidth = 2f,
                    cap = StrokeCap.Round
                )
            }
        }

        scope.drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    accent.copy(alpha = if (isSelected) 0.38f else 0.22f),
                    Color.Transparent
                ),
                center = center,
                radius = baseRadius * 1.25f
            ),
            radius = baseRadius * 1.25f,
            center = center
        )

        scope.drawCircle(
            color = accent,
            radius = baseRadius,
            center = center,
            style = Stroke(width = if (isSelected) 3.6f else 2.6f)
        )
        scope.drawCircle(
            color = accentDim.copy(alpha = 0.9f),
            radius = baseRadius * 0.68f,
            center = center,
            style = Stroke(width = 1.6f)
        )

        val reticle = baseRadius * 0.42f
        scope.drawLine(
            color = Color.White.copy(alpha = 0.92f),
            start = Offset(center.x - reticle, center.y),
            end = Offset(center.x + reticle, center.y),
            strokeWidth = if (isSelected) 2.4f else 1.8f,
            cap = StrokeCap.Round
        )
        scope.drawLine(
            color = Color.White.copy(alpha = 0.92f),
            start = Offset(center.x, center.y - reticle),
            end = Offset(center.x, center.y + reticle),
            strokeWidth = if (isSelected) 2.4f else 1.8f,
            cap = StrokeCap.Round
        )
        scope.drawCircle(
            color = Color.White,
            radius = if (isSelected) 4f else 3f,
            center = center
        )
        scope.drawCircle(
            color = accent,
            radius = if (isSelected) 2.2f else 1.6f,
            center = center
        )

        if (isSelected) {
            val bracket = baseRadius * 0.95f
            val bracketLen = baseRadius * 0.38f
            drawCornerBracket(scope, Offset(center.x - bracket, center.y - bracket), bracketLen, accent, right = true, down = true)
            drawCornerBracket(scope, Offset(center.x + bracket, center.y - bracket), bracketLen, accent, right = false, down = true)
            drawCornerBracket(scope, Offset(center.x - bracket, center.y + bracket), bracketLen, accent, right = true, down = false)
            drawCornerBracket(scope, Offset(center.x + bracket, center.y + bracket), bracketLen, accent, right = false, down = false)
        }
    }

    fun drawLeaderLine(
        scope: DrawScope,
        start: Offset,
        end: Offset,
        accent: Color,
        isSelected: Boolean,
        flowPhase: Float
    ) {
        scope.drawLine(
            color = Color.Black.copy(alpha = 0.35f),
            start = start,
            end = end,
            strokeWidth = if (isSelected) 4f else 3f,
            cap = StrokeCap.Round
        )
        scope.drawLine(
            brush = Brush.linearGradient(
                colors = listOf(
                    accent.copy(alpha = 0.95f),
                    accent.copy(alpha = 0.35f + flowPhase * 0.25f),
                    Color.White.copy(alpha = 0.55f)
                ),
                start = start,
                end = end
            ),
            start = start,
            end = end,
            strokeWidth = if (isSelected) 2.4f else 1.8f,
            cap = StrokeCap.Round
        )
        scope.drawCircle(
            color = accent,
            radius = if (isSelected) 5f else 4f,
            center = start
        )
        scope.drawCircle(
            color = Color.White.copy(alpha = 0.85f),
            radius = 2f,
            center = end
        )
    }

    private fun drawCornerBracket(
        scope: DrawScope,
        origin: Offset,
        length: Float,
        color: Color,
        right: Boolean,
        down: Boolean
    ) {
        val hx = if (right) length else -length
        val vy = if (down) length else -length
        scope.drawLine(color, origin, Offset(origin.x + hx, origin.y), 2.8f, StrokeCap.Round)
        scope.drawLine(color, origin, Offset(origin.x, origin.y + vy), 2.8f, StrokeCap.Round)
    }
}
