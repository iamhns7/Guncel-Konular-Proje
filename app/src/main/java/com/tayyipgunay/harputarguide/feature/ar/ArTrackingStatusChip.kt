package com.tayyipgunay.harputarguide.feature.ar

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.R

@Composable
fun ArTrackingStatusChip(
    trackingState: ArTrackingState,
    hotspotsRevealed: Boolean = true,
    useAssistedPlacement: Boolean = false,
    modifier: Modifier = Modifier
) {
    val infinite = rememberInfiniteTransition(label = "status_chip")
    val pulse by infinite.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "chip_pulse"
    )

    val (accent, icon, messageRes) = when {
        hotspotsRevealed && useAssistedPlacement -> Triple(
            Color(0xFFB8F5FF),
            Icons.Filled.CenterFocusStrong,
            R.string.ar_tracking_assisted
        )
        hotspotsRevealed && trackingState.isAlignedEnough() -> Triple(
            Color(0xFF7BC67E),
            Icons.Filled.GpsFixed,
            if (trackingState == ArTrackingState.TRACKING) {
                R.string.ar_tracking_locked
            } else {
                R.string.ar_tracking_aligned
            }
        )
        trackingState.isAlignedEnough() -> Triple(
            Color(0xFF9AE6B0),
            Icons.Filled.CenterFocusStrong,
            R.string.ar_hotspots_preparing
        )
        trackingState == ArTrackingState.LOST -> Triple(
            Color(0xFFE8A04C),
            Icons.Filled.CenterFocusStrong,
            R.string.ar_tracking_lost
        )
        else -> Triple(
            Color(0xFFF5EBDD),
            Icons.Filled.Search,
            R.string.ar_tracking_searching
        )
    }

    val isStable = hotspotsRevealed && (useAssistedPlacement || trackingState.isAlignedEnough())

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF1A1008).copy(alpha = 0.82f))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .scale(if (isStable) 1f else pulse)
                .clip(CircleShape)
                .background(accent)
        )
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accent,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = stringResource(messageRes),
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 16.sp
        )
    }
}
