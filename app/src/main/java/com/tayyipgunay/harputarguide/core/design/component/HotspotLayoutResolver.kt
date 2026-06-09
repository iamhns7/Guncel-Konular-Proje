package com.tayyipgunay.harputarguide.core.design.component

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.tayyipgunay.harputarguide.data.asset.StructureHotspot
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min

internal data class HotspotLabelMetrics(
    val widthPx: Float,
    val heightPx: Float,
    val maxLines: Int
)

internal data class HotspotLayout(
    val hotspot: StructureHotspot,
    val anchorX: Float,
    val anchorY: Float,
    val ringRadiusPx: Float,
    val labelLeft: Float,
    val labelTop: Float,
    val labelWidth: Float,
    val labelHeight: Float,
    val maxLines: Int
)

internal object HotspotLayoutResolver {

    fun resolve(
        hotspots: List<StructureHotspot>,
        containerWidthPx: Int,
        containerHeightPx: Int,
        density: Density,
        selectedHotspotId: String?,
        labelMetrics: Map<String, HotspotLabelMetrics>
    ): List<HotspotLayout> {
        if (hotspots.isEmpty() || containerWidthPx <= 0 || containerHeightPx <= 0) return emptyList()

        val baseRingRadiusPx = with(density) { 13.dp.toPx() }
        val selectedRingRadiusPx = with(density) { 16.dp.toPx() }
        val minRingRadiusPx = with(density) { 10.dp.toPx() }
        val ringShrinkStepPx = with(density) { 1.5.dp.toPx() }
        val labelGapPx = with(density) { 10.dp.toPx() }
        val collisionMarginPx = with(density) { 6.dp.toPx() }

        val ordered = hotspots.sortedWith(
            compareByDescending<StructureHotspot> { it.id == selectedHotspotId }
                .thenBy { it.y }
                .thenBy { it.x }
        )

        val placedLabels = mutableListOf<BoundsRect>()
        val placedRings = mutableListOf<RingBounds>()
        val layouts = mutableListOf<HotspotLayout>()

        ordered.forEach { hotspot ->
            val isSelected = hotspot.id == selectedHotspotId
            val anchorX = hotspot.x * containerWidthPx
            val anchorY = hotspot.y * containerHeightPx
            val ringRadiusPx = resolveRingRadius(
                anchorX = anchorX,
                anchorY = anchorY,
                desiredRadiusPx = if (isSelected) selectedRingRadiusPx else baseRingRadiusPx,
                minRadiusPx = minRingRadiusPx,
                shrinkStepPx = ringShrinkStepPx,
                placedRings = placedRings,
                marginPx = collisionMarginPx
            )

            val metrics = labelMetrics[hotspot.id] ?: estimateLabelSize(hotspot.name, density, containerWidthPx)
            val labelWidth = metrics.widthPx
            val labelHeight = metrics.heightPx
            val labelRect = findLabelPosition(
                anchorX = anchorX,
                anchorY = anchorY,
                ringRadiusPx = ringRadiusPx,
                labelWidth = labelWidth,
                labelHeight = labelHeight,
                labelGapPx = labelGapPx,
                collisionMarginPx = collisionMarginPx,
                containerWidthPx = containerWidthPx.toFloat(),
                containerHeightPx = containerHeightPx.toFloat(),
                hotspotX = hotspot.x,
                hotspotY = hotspot.y,
                placedLabels = placedLabels,
                placedRings = placedRings
            )

            placedLabels += labelRect
            placedRings += RingBounds(anchorX, anchorY, ringRadiusPx)

            layouts += HotspotLayout(
                hotspot = hotspot,
                anchorX = anchorX,
                anchorY = anchorY,
                ringRadiusPx = ringRadiusPx,
                labelLeft = labelRect.left,
                labelTop = labelRect.top,
                labelWidth = labelWidth,
                labelHeight = labelHeight,
                maxLines = metrics.maxLines
            )
        }

        return layouts.sortedBy { hotspots.indexOf(it.hotspot) }
    }

    private fun resolveRingRadius(
        anchorX: Float,
        anchorY: Float,
        desiredRadiusPx: Float,
        minRadiusPx: Float,
        shrinkStepPx: Float,
        placedRings: List<RingBounds>,
        marginPx: Float
    ): Float {
        var radius = desiredRadiusPx
        repeat(8) {
            val collides = placedRings.any { other ->
                circlesOverlap(anchorX, anchorY, radius, other.centerX, other.centerY, other.radiusPx, marginPx)
            }
            if (!collides) return radius
            radius = max(minRadiusPx, radius - shrinkStepPx)
        }
        return max(minRadiusPx, radius)
    }

    private fun findLabelPosition(
        anchorX: Float,
        anchorY: Float,
        ringRadiusPx: Float,
        labelWidth: Float,
        labelHeight: Float,
        labelGapPx: Float,
        collisionMarginPx: Float,
        containerWidthPx: Float,
        containerHeightPx: Float,
        hotspotX: Float,
        hotspotY: Float,
        placedLabels: List<BoundsRect>,
        placedRings: List<RingBounds>
    ): BoundsRect {
        val candidates = buildLabelCandidates(
            anchorX = anchorX,
            anchorY = anchorY,
            ringRadiusPx = ringRadiusPx,
            labelWidth = labelWidth,
            labelHeight = labelHeight,
            labelGapPx = labelGapPx,
            hotspotX = hotspotX,
            hotspotY = hotspotY
        )

        for (extra in 0..5) {
            val push = labelGapPx * extra
            for (candidate in candidates) {
                val shifted = if (extra == 0) candidate else candidate.pushAway(anchorX, anchorY, push)
                val rect = shifted.clamp(containerWidthPx, containerHeightPx)
                if (!hasCollision(rect, placedLabels, placedRings, collisionMarginPx)) {
                    return rect
                }
            }
        }

        return candidates.first().clamp(containerWidthPx, containerHeightPx)
    }

    private fun hasCollision(
        rect: BoundsRect,
        placedLabels: List<BoundsRect>,
        placedRings: List<RingBounds>,
        marginPx: Float
    ): Boolean {
        if (placedLabels.any { it.intersects(rect, marginPx) }) return true
        return placedRings.any { ring ->
            rect.intersectsCircle(ring.centerX, ring.centerY, ring.radiusPx, marginPx)
        }
    }

    private fun buildLabelCandidates(
        anchorX: Float,
        anchorY: Float,
        ringRadiusPx: Float,
        labelWidth: Float,
        labelHeight: Float,
        labelGapPx: Float,
        hotspotX: Float,
        hotspotY: Float
    ): List<LabelCandidate> {
        val offset = ringRadiusPx + labelGapPx
        val all = listOf(
            LabelCandidate(anchorX + offset, anchorY - labelHeight / 2f, labelWidth, labelHeight),
            LabelCandidate(anchorX - offset - labelWidth, anchorY - labelHeight / 2f, labelWidth, labelHeight),
            LabelCandidate(anchorX - labelWidth / 2f, anchorY - offset - labelHeight, labelWidth, labelHeight),
            LabelCandidate(anchorX - labelWidth / 2f, anchorY + offset, labelWidth, labelHeight),
            LabelCandidate(anchorX + offset, anchorY - offset - labelHeight, labelWidth, labelHeight),
            LabelCandidate(anchorX - offset - labelWidth, anchorY - offset - labelHeight, labelWidth, labelHeight),
            LabelCandidate(anchorX + offset, anchorY + offset, labelWidth, labelHeight),
            LabelCandidate(anchorX - offset - labelWidth, anchorY + offset, labelWidth, labelHeight)
        )

        val priority = mutableListOf<LabelCandidate>()
        if (hotspotX > 0.58f) priority += all[1]
        if (hotspotX < 0.42f) priority += all[0]
        if (hotspotY > 0.62f) priority += all[2]
        if (hotspotY < 0.38f) priority += all[3]
        all.forEach { if (it !in priority) priority += it }
        return priority
    }

    private fun estimateLabelSize(
        name: String,
        density: Density,
        containerWidthPx: Int
    ): HotspotLabelMetrics {
        val maxLines = if (name.length > 13 || name.contains('/')) 2 else 1
        val maxWidthPx = containerWidthPx * 0.48f
        val widthPx = with(density) {
            min(
                (name.length * 8.5f).dp.toPx() + 32.dp.toPx(),
                maxWidthPx
            )
        }.coerceAtLeast(with(density) { 80.dp.toPx() })
        val heightPx = with(density) {
            if (maxLines == 2) 44.dp.toPx() else 34.dp.toPx()
        }
        return HotspotLabelMetrics(widthPx = widthPx, heightPx = heightPx, maxLines = maxLines)
    }

    private fun circlesOverlap(
        x1: Float,
        y1: Float,
        r1: Float,
        x2: Float,
        y2: Float,
        r2: Float,
        margin: Float
    ): Boolean = hypot(x1 - x2, y1 - y2) < (r1 + r2 + margin)

    private data class RingBounds(
        val centerX: Float,
        val centerY: Float,
        val radiusPx: Float
    )

    private data class BoundsRect(
        val left: Float,
        val top: Float,
        val right: Float,
        val bottom: Float
    ) {
        fun intersects(other: BoundsRect, margin: Float): Boolean =
            left < other.right + margin &&
                right > other.left - margin &&
                top < other.bottom + margin &&
                bottom > other.top - margin

        fun intersectsCircle(centerX: Float, centerY: Float, radius: Float, margin: Float): Boolean {
            val closestX = centerX.coerceIn(left, right)
            val closestY = centerY.coerceIn(top, bottom)
            return hypot(centerX - closestX, centerY - closestY) < radius + margin
        }
    }

    private data class LabelCandidate(
        val left: Float,
        val top: Float,
        val width: Float,
        val height: Float
    ) {
        fun clamp(containerWidthPx: Float, containerHeightPx: Float): BoundsRect {
            val clampedLeft = left.coerceIn(0f, max(0f, containerWidthPx - width))
            val clampedTop = top.coerceIn(0f, max(0f, containerHeightPx - height))
            return BoundsRect(
                left = clampedLeft,
                top = clampedTop,
                right = clampedLeft + width,
                bottom = clampedTop + height
            )
        }

        fun pushAway(anchorX: Float, anchorY: Float, extra: Float): LabelCandidate {
            val centerX = left + width / 2f
            val centerY = top + height / 2f
            val dx = centerX - anchorX
            val dy = centerY - anchorY
            val length = hypot(dx, dy).coerceAtLeast(1f)
            val newCenterX = centerX + (dx / length) * extra
            val newCenterY = centerY + (dy / length) * extra
            return copy(
                left = newCenterX - width / 2f,
                top = newCenterY - height / 2f
            )
        }
    }
}
