package com.tayyipgunay.harputarguide.core.design.component.analysis

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.R
import com.tayyipgunay.harputarguide.core.design.component.AssetAsyncImage

@Composable
fun AnalysisDiagramSection(
    previewImageAssetPath: String?,
    callouts: List<AnalysisCallout>,
    showCallouts: Boolean = false,
    missingImageTextResId: Int = R.string.model_viewer_missing,
    previewContentDescriptionResId: Int = R.string.model_viewer_cd_viewport,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .background(AnalysisDetailTheme.ImageAreaBackground)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        val density = LocalDensity.current
        val areaHeight = maxHeight
        val areaWidth = maxWidth

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(AnalysisDetailTheme.ImageSurface)
        ) {
            if (!previewImageAssetPath.isNullOrBlank()) {
                AssetAsyncImage(
                    assetPath = previewImageAssetPath,
                    contentDescription = stringResource(previewContentDescriptionResId),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    highQuality = true
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(missingImageTextResId),
                        color = Color.White.copy(alpha = 0.55f),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                AnalysisDetailTheme.CreamPanel.copy(alpha = 0.2f),
                                AnalysisDetailTheme.CreamPanel.copy(alpha = 0.5f)
                            )
                        )
                    )
            )

            if (showCallouts && callouts.isNotEmpty() && !previewImageAssetPath.isNullOrBlank()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val dotRadius = 4f
                    callouts.forEach { callout ->
                        val anchorX = size.width * callout.anchorXFraction
                        val anchorY = size.height * callout.anchorYFraction
                        drawCircle(
                            color = Color.White.copy(alpha = 0.25f),
                            radius = dotRadius + 5f,
                            center = Offset(anchorX, anchorY)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = dotRadius,
                            center = Offset(anchorX, anchorY)
                        )
                    }
                }

                callouts.forEach { callout ->
                    val yOffset = with(density) {
                        (areaHeight * callout.anchorYFraction - 14.dp)
                            .coerceIn(8.dp, areaHeight - 48.dp)
                    }
                    val xOffset = with(density) {
                        (areaWidth * callout.anchorXFraction - 8.dp)
                            .coerceIn(8.dp, areaWidth - 140.dp)
                    }
                    Text(
                        text = callout.label,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 14.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(x = xOffset, y = yOffset)
                            .clip(RoundedCornerShape(8.dp))
                            .background(AnalysisDetailTheme.CalloutBg)
                            .border(
                                width = 0.5.dp,
                                color = Color.White.copy(alpha = 0.18f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}
