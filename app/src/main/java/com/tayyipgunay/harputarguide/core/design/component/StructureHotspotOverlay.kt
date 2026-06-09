package com.tayyipgunay.harputarguide.core.design.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.data.asset.StructureHotspot
import com.tayyipgunay.harputarguide.feature.ar.ArHotspotMarkerDraw
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun StructureHotspotOverlay(
    hotspots: List<StructureHotspot>,
    selectedHotspotId: String?,
    onHotspotClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    trackingLocked: Boolean = true
) {
    val accent = if (trackingLocked) Color(0xFF5CFF8A) else Color(0xFFFFC857)
    val accentDim = if (trackingLocked) Color(0xFF1FA84A) else Color(0xFFD68A20)

    val infinite = rememberInfiniteTransition(label = "ar_hotspot_fx")
    val ripplePhase by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple"
    )
    val orbitDegrees by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit"
    )
    val floatBob by infinite.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bob"
    )
    val leaderFlow by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "leader_flow"
    )

    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val textMeasurer = rememberTextMeasurer()
        val labelTextStyle = TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 15.sp
        )
        val horizontalPaddingPx = with(density) { 28.dp.toPx() }
        val verticalPaddingPx = with(density) { 8.dp.toPx() }
        val maxTextWidthPx = constraints.maxWidth * 0.5f
        val bobOffsetPx = floatBob * with(density) { 3.dp.toPx() }

        val labelMetrics = remember(hotspots, constraints.maxWidth, labelTextStyle) {
            hotspots.associate { hotspot ->
                val maxLines = if (hotspot.name.length > 12 || hotspot.name.contains('/')) 2 else 1
                val measured = textMeasurer.measure(
                    text = hotspot.name,
                    style = labelTextStyle,
                    constraints = Constraints(maxWidth = maxTextWidthPx.roundToInt()),
                    maxLines = maxLines,
                    softWrap = true
                )
                hotspot.id to HotspotLabelMetrics(
                    widthPx = measured.size.width.toFloat() + horizontalPaddingPx,
                    heightPx = measured.size.height.toFloat() + verticalPaddingPx,
                    maxLines = maxLines
                )
            }
        }

        val layouts = remember(
            hotspots,
            constraints.maxWidth,
            constraints.maxHeight,
            selectedHotspotId,
            labelMetrics
        ) {
            HotspotLayoutResolver.resolve(
                hotspots = hotspots,
                containerWidthPx = constraints.maxWidth,
                containerHeightPx = constraints.maxHeight,
                density = density,
                selectedHotspotId = selectedHotspotId,
                labelMetrics = labelMetrics
            )
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            layouts.forEachIndexed { index, layout ->
                val isSelected = layout.hotspot.id == selectedHotspotId
                val ringRadius = layout.ringRadiusPx
                val anchor = Offset(layout.anchorX, layout.anchorY + bobOffsetPx)
                val labelCenterX = layout.labelLeft + layout.labelWidth / 2f
                val labelCenterY = layout.labelTop + layout.labelHeight / 2f
                val angle = atan2(labelCenterY - anchor.y, labelCenterX - anchor.x)
                val start = Offset(
                    anchor.x + cos(angle) * ringRadius,
                    anchor.y + sin(angle) * ringRadius
                )
                val end = Offset(
                    labelCenterX - cos(angle) * (layout.labelWidth / 2f),
                    labelCenterY - sin(angle) * (layout.labelHeight / 2f)
                )

                ArHotspotMarkerDraw.drawLeaderLine(
                    scope = this,
                    start = start,
                    end = end,
                    accent = accent,
                    isSelected = isSelected,
                    flowPhase = leaderFlow
                )
                ArHotspotMarkerDraw.drawMarker(
                    scope = this,
                    center = anchor,
                    accent = accent,
                    accentDim = accentDim,
                    isSelected = isSelected,
                    ringRadius = ringRadius,
                    ripplePhase = (ripplePhase + index * 0.19f) % 1f,
                    orbitDegrees = orbitDegrees + index * 18f,
                    trackingLocked = trackingLocked
                )
            }
        }

        layouts.forEach { layout ->
            val isSelected = layout.hotspot.id == selectedHotspotId
            val ringSizePx = layout.ringRadiusPx * 2.6f
            val hitPaddingPx = with(density) { 12.dp.toPx() }

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (layout.anchorX - ringSizePx / 2f - hitPaddingPx).roundToInt(),
                            (layout.anchorY - ringSizePx / 2f - hitPaddingPx + bobOffsetPx).roundToInt()
                        )
                    }
                    .width(with(density) { (ringSizePx + hitPaddingPx * 2).toDp() })
                    .height(with(density) { (ringSizePx + hitPaddingPx * 2).toDp() })
                    .clickable(
                        interactionSource = remember(layout.hotspot.id) { MutableInteractionSource() },
                        indication = null
                    ) { onHotspotClick(layout.hotspot.id) }
            )

            ArHotspotHudLabel(
                text = layout.hotspot.name,
                maxLines = layout.maxLines,
                isSelected = isSelected,
                accentColor = accent,
                textStyle = labelTextStyle,
                onClick = { onHotspotClick(layout.hotspot.id) },
                modifier = Modifier
                    .offset {
                        IntOffset(layout.labelLeft.roundToInt(), layout.labelTop.roundToInt())
                    }
                    .width(with(density) { layout.labelWidth.toDp() })
                    .height(with(density) { layout.labelHeight.toDp() })
            )
        }
    }
}
