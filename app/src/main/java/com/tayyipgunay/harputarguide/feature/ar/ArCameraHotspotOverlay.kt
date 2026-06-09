package com.tayyipgunay.harputarguide.feature.ar

import android.graphics.Bitmap
import android.graphics.RectF
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.tayyipgunay.harputarguide.core.design.component.StructureHotspotOverlay
import com.tayyipgunay.harputarguide.data.asset.StructureHotspot
import kotlin.math.roundToInt

@Composable
fun ArCameraHotspotOverlay(
    hotspots: List<StructureHotspot>,
    referenceBitmap: Bitmap?,
    trackingState: ArTrackingState,
    trackedRect: RectF?,
    selectedHotspotId: String?,
    onHotspotClick: (String) -> Unit,
    showHotspots: Boolean,
    useAssistedPlacement: Boolean = false,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .zIndex(10f)
    ) {
        val isAligned = trackingState.isAlignedEnough()
        ArHudAmbientOverlay(
            trackingLocked = showHotspots && (isAligned || useAssistedPlacement),
            modifier = Modifier.fillMaxSize()
        )
        val density = LocalDensity.current
        val containerWidthPx = constraints.maxWidth.toFloat()
        val containerHeightPx = constraints.maxHeight.toFloat()

        val targetBounds = remember(
            trackingState,
            trackedRect,
            referenceBitmap,
            containerWidthPx,
            containerHeightPx,
            useAssistedPlacement
        ) {
            resolveTargetBounds(
                trackingState = trackingState,
                trackedRect = trackedRect,
                referenceBitmap = referenceBitmap,
                containerWidthPx = containerWidthPx,
                containerHeightPx = containerHeightPx,
                useAssistedPlacement = useAssistedPlacement
            )
        }

        val springSpec = spring<Float>(stiffness = Spring.StiffnessMediumLow, dampingRatio = 0.78f)
        val animatedLeft by animateFloatAsState(targetBounds.left, springSpec, label = "bounds_left")
        val animatedTop by animateFloatAsState(targetBounds.top, springSpec, label = "bounds_top")
        val animatedWidth by animateFloatAsState(targetBounds.width, springSpec, label = "bounds_width")
        val animatedHeight by animateFloatAsState(targetBounds.height, springSpec, label = "bounds_height")

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(animatedLeft.roundToInt(), animatedTop.roundToInt())
                }
                .size(
                    width = with(density) { animatedWidth.toDp().coerceAtLeast(48.dp) },
                    height = with(density) { animatedHeight.toDp().coerceAtLeast(48.dp) }
                )
        ) {
            ArTrackingVisualOverlay(
                trackingState = if (useAssistedPlacement && !isAligned) {
                    ArTrackingState.ALIGNED
                } else {
                    trackingState
                },
                modifier = Modifier.fillMaxSize()
            )
            AnimatedVisibility(
                visible = showHotspots,
                enter = fadeIn(spring(stiffness = Spring.StiffnessMedium, dampingRatio = 0.78f)) +
                    scaleIn(
                        initialScale = 0.78f,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = 0.72f)
                    ),
                exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) +
                    scaleOut(targetScale = 0.88f)
            ) {
                StructureHotspotOverlay(
                    hotspots = hotspots,
                    selectedHotspotId = selectedHotspotId,
                    onHotspotClick = onHotspotClick,
                    trackingLocked = isAligned || useAssistedPlacement,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

private fun resolveTargetBounds(
    trackingState: ArTrackingState,
    trackedRect: RectF?,
    referenceBitmap: Bitmap?,
    containerWidthPx: Float,
    containerHeightPx: Float,
    useAssistedPlacement: Boolean
): ArImageDisplayBounds {
    val useTrackedRect = !useAssistedPlacement &&
        trackingState.isAlignedEnough() &&
        trackedRect != null

    if (useTrackedRect) {
        return ArImageDisplayBounds(
            left = trackedRect.left,
            top = trackedRect.top,
            width = trackedRect.width().coerceAtLeast(1f),
            height = trackedRect.height().coerceAtLeast(1f)
        )
    }
    if (referenceBitmap != null) {
        return ArImageLayout.computeFitBounds(
            imageWidthPx = referenceBitmap.width.toFloat(),
            imageHeightPx = referenceBitmap.height.toFloat(),
            containerWidthPx = containerWidthPx,
            containerHeightPx = containerHeightPx
        )
    }
    return ArImageDisplayBounds(0f, 0f, containerWidthPx, containerHeightPx)
}
