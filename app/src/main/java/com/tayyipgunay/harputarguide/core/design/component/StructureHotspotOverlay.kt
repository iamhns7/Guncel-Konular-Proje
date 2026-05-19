package com.tayyipgunay.harputarguide.core.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.data.asset.StructureHotspot
import kotlin.math.roundToInt

@Composable
fun StructureHotspotOverlay(
    hotspots: List<StructureHotspot>,
    selectedHotspotId: String?,
    onHotspotClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val glowGreen = Color(0xFF7BC67E)
    val dotColor = Color.White
    val labelBackground = Color.Black.copy(alpha = 0.65f)
    val labelColor = Color.White

    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val maxWidthPx = constraints.maxWidth
        val maxHeightPx = constraints.maxHeight

        hotspots.forEach { hotspot ->
            val isSelected = hotspot.id == selectedHotspotId
            val centerX = hotspot.x * maxWidthPx
            val centerY = hotspot.y * maxHeightPx

            val dotSize = if (isSelected) 14.dp else 10.dp
            val glowSize = if (isSelected) 28.dp else 22.dp

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (centerX - with(density) { dotSize.toPx() } / 2).roundToInt(),
                            (centerY - with(density) { dotSize.toPx() } / 2).roundToInt()
                        )
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(glowSize)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(glowGreen.copy(alpha = if (isSelected) 0.45f else 0.3f))
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = glowGreen.copy(alpha = 0.8f),
                            shape = CircleShape
                        )
                )
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(dotColor)
                        .clickable { onHotspotClick(hotspot.id) }
                )
            }

            val labelText = buildString {
                append(hotspot.name)
                hotspot.subtitle?.let { append("\n").append(it) }
            }

            val labelOnLeft = hotspot.x > 0.55f

            Box(
                modifier = Modifier
                    .offset {
                        val labelWidthEstimate = with(density) { 100.dp.toPx() }
                        val labelOffsetX = if (labelOnLeft) {
                            centerX - labelWidthEstimate - with(density) { 8.dp.toPx() }
                        } else {
                            centerX + with(density) { 18.dp.toPx() }
                        }
                        val labelOffsetY = centerY - with(density) { 12.dp.toPx() }
                        IntOffset(labelOffsetX.roundToInt(), labelOffsetY.roundToInt())
                    }
                    .clip(RoundedCornerShape(8.dp))
                    .background(labelBackground)
                    .clickable { onHotspotClick(hotspot.id) }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = labelText,
                    color = labelColor,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    lineHeight = 13.sp
                )
            }
        }
    }
}
