package com.tayyipgunay.harputarguide.core.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import com.tayyipgunay.harputarguide.data.content.ContentAssetPathResolver

@Composable
fun HarputAssetImage(
    imageAssetPath: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholderGradientColors: List<Color> = defaultHarputPlaceholderGradient()
) {
    val imageModel = remember(imageAssetPath) {
        ContentAssetPathResolver.toImageModel(imageAssetPath)
    }

    if (imageModel == null) {
        HarputImagePlaceholder(
            modifier = modifier,
            gradientColors = placeholderGradientColors
        )
        return
    }

    SubcomposeAsyncImage(
        model = imageModel,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        loading = {
            HarputImagePlaceholder(
                modifier = Modifier.fillMaxSize(),
                gradientColors = placeholderGradientColors
            )
        },
        error = {
            HarputImagePlaceholder(
                modifier = Modifier.fillMaxSize(),
                gradientColors = placeholderGradientColors
            )
        }
    )
}

@Composable
private fun HarputImagePlaceholder(
    modifier: Modifier = Modifier,
    gradientColors: List<Color>
) {
    Box(
        modifier = modifier.background(
            brush = Brush.linearGradient(
                colors = gradientColors.ifEmpty { defaultHarputPlaceholderGradient() }
            )
        )
    )
}

fun defaultHarputPlaceholderGradient(): List<Color> = listOf(
    Color(0xFFDCC7A8),
    Color(0xFFBEA07A)
)
