package com.tayyipgunay.harputarguide.feature.ar

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.R

@Composable
fun ArCameraHudOverlay(
    trackingState: ArTrackingState,
    modifier: Modifier = Modifier
) {
    val trackingLocked = trackingState.isAlignedEnough()
    val accent = if (trackingLocked) Color(0xFF5CFF8A) else Color(0xFFB8F5FF)

    val infinite = rememberInfiniteTransition(label = "camera_hud")
    val recPulse by infinite.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rec_pulse"
    )
    val crosshairPulse by infinite.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "crosshair"
    )

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val margin = 18f
            val corner = 42f
            val stroke = 2.2f
            val hudColor = accent.copy(alpha = 0.75f)

            fun cornerAt(origin: Offset, right: Boolean, down: Boolean) {
                val hx = if (right) corner else -corner
                val vy = if (down) corner else -corner
                drawLine(hudColor, origin, Offset(origin.x + hx, origin.y), stroke, StrokeCap.Round)
                drawLine(hudColor, origin, Offset(origin.x, origin.y + vy), stroke, StrokeCap.Round)
            }

            cornerAt(Offset(margin, margin), right = true, down = true)
            cornerAt(Offset(size.width - margin, margin), right = false, down = true)
            cornerAt(Offset(margin, size.height - margin), right = true, down = false)
            cornerAt(Offset(size.width - margin, size.height - margin), right = false, down = false)

            if (!trackingLocked) {
                val cx = size.width / 2f
                val cy = size.height / 2f
                val arm = 26f * crosshairPulse
                val alpha = 0.35f + crosshairPulse * 0.35f
                drawLine(
                    color = accent.copy(alpha = alpha),
                    start = Offset(cx - arm, cy),
                    end = Offset(cx + arm, cy),
                    strokeWidth = 1.8f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = accent.copy(alpha = alpha),
                    start = Offset(cx, cy - arm),
                    end = Offset(cx, cy + arm),
                    strokeWidth = 1.8f,
                    cap = StrokeCap.Round
                )
                drawCircle(
                    color = accent.copy(alpha = alpha * 0.7f),
                    radius = 5f,
                    center = Offset(cx, cy),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.6f)
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 16.dp, top = 58.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xAA0A120E))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(9.dp)
                    .clip(CircleShape)
                    .background(Color.Red.copy(alpha = recPulse))
            )
            Text(
                text = stringResource(R.string.ar_live_badge),
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(start = 7.dp)
            )
        }

        if (trackingLocked) {
            Text(
                text = stringResource(R.string.ar_hud_locked),
                color = accent,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.8.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(top = 58.dp, end = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xAA0A120E))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}
