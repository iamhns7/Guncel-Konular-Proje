package com.tayyipgunay.harputarguide.core.design.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

private val SepiaColorFilter = ColorFilter.colorMatrix(
    ColorMatrix(
        floatArrayOf(
            0.393f, 0.769f, 0.189f, 0f, 0f,
            0.349f, 0.686f, 0.168f, 0f, 0f,
            0.272f, 0.534f, 0.131f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
)

/**
 * Kamera / günümüz görüntüsünün ÜZERİNE bindirilen geçmiş katmanı.
 * Alt katmandaki canlı görüntü [ARScreen] içinde gösterilir; bu composable yalnızca overlay çizer.
 */
@Composable
fun PastViewOverlay(
    sliderValue: Float,
    presentImageRes: Int,
    modifier: Modifier = Modifier
) {
    val clampedSlider = sliderValue.coerceIn(0f, 1f)
    val overlayAlpha = clampedSlider.coerceIn(0f, 1f)

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val dividerX = (clampedSlider * constraints.maxWidth).roundToInt()

        // Tam ekran yarı saydam geçmiş tonu (slider → opaklık)
        Image(
            painter = painterResource(id = presentImageRes),
            contentDescription = "Geçmiş rekonstrüksiyon katmanı",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = overlayAlpha * 0.85f }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF3D2814).copy(alpha = 0.4f * overlayAlpha),
                                Color(0xFF6B4A2A).copy(alpha = 0.3f * overlayAlpha),
                                Color(0xFF2A1A0E).copy(alpha = 0.45f * overlayAlpha)
                            )
                        )
                    )
                },
            contentScale = ContentScale.Crop,
            colorFilter = SepiaColorFilter
        )

        // Sol tarafta karşılaştırma için daha belirgin geçmiş şeridi
        if (dividerX > 0) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(with(density) { dividerX.toDp() })
                    .clip(RectangleShape)
            ) {
                Image(
                    painter = painterResource(id = presentImageRes),
                    contentDescription = "Geçmiş karşılaştırma",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { alpha = (overlayAlpha * 0.95f).coerceAtLeast(0.05f) },
                    contentScale = ContentScale.Crop,
                    colorFilter = SepiaColorFilter
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colorStops = arrayOf(
                            0f to Color(0xFF4A3520).copy(alpha = overlayAlpha * 0.18f),
                            0.5f to Color.Transparent,
                            1f to Color.Transparent
                        )
                    )
                )
        )

        if (clampedSlider > 0.02f) {
            ComparisonDivider(
                dividerX = dividerX,
                maxHeight = constraints.maxHeight,
                modifier = Modifier.fillMaxSize()
            )
        }

        PastSideLabel(
            text = "Geçmiş",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 12.dp)
        )
        PastSideLabel(
            text = "Günümüz",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp)
        )
    }
}

@Composable
private fun ComparisonDivider(
    dividerX: Int,
    maxHeight: Int,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val lineColor = Color.White.copy(alpha = 0.9f)

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .offset { IntOffset(dividerX - with(density) { 1.dp.toPx() }.roundToInt(), 0) }
                .width(2.dp)
                .fillMaxHeight()
                .background(lineColor)
        )
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        dividerX - with(density) { 14.dp.toPx() }.roundToInt(),
                        maxHeight / 2 - with(density) { 14.dp.toPx() }.roundToInt()
                    )
                }
                .size(28.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color(0xFF4B2E1F))
            )
        }
    }
}

@Composable
private fun PastSideLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            .background(Color.Black.copy(alpha = 0.5f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
