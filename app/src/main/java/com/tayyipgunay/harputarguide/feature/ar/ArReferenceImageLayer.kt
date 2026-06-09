package com.tayyipgunay.harputarguide.feature.ar

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import com.tayyipgunay.harputarguide.core.design.component.StructureHotspotOverlay
import com.tayyipgunay.harputarguide.data.asset.StructureHotspot
import kotlin.math.roundToInt

@Composable
fun ArReferenceImageLayer(
    bitmap: Bitmap,
    hotspots: List<StructureHotspot>,
    selectedHotspotId: String?,
    onHotspotClick: (String) -> Unit,
    showHotspots: Boolean,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val bounds = ArImageLayout.computeFitBounds(
            imageWidthPx = bitmap.width.toFloat(),
            imageHeightPx = bitmap.height.toFloat(),
            containerWidthPx = constraints.maxWidth.toFloat(),
            containerHeightPx = constraints.maxHeight.toFloat()
        )
        val density = LocalDensity.current

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(bounds.left.roundToInt(), bounds.top.roundToInt())
                }
                .size(
                    width = with(density) { bounds.width.toDp() },
                    height = with(density) { bounds.height.toDp() }
                )
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
            if (showHotspots && hotspots.isNotEmpty()) {
                StructureHotspotOverlay(
                    hotspots = hotspots,
                    selectedHotspotId = selectedHotspotId,
                    onHotspotClick = onHotspotClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
