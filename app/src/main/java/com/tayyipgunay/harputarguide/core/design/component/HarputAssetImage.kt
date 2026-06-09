package com.tayyipgunay.harputarguide.core.design.component

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import com.tayyipgunay.harputarguide.data.content.ContentAssetPathResolver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun HarputAssetImage(
    imageAssetPath: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholderGradientColors: List<Color> = defaultHarputPlaceholderGradient()
) {
    val context = LocalContext.current
    val trimmedPath = imageAssetPath?.trim().orEmpty()
    if (trimmedPath.startsWith("ar/")) {
        var bitmap by remember(trimmedPath) { mutableStateOf<android.graphics.Bitmap?>(null) }
        LaunchedEffect(trimmedPath) {
            bitmap = withContext(Dispatchers.IO) {
                runCatching {
                    context.assets.open(trimmedPath).use { BitmapFactory.decodeStream(it) }
                }.getOrNull()
            }
        }
        val loaded = bitmap
        if (loaded != null) {
            Image(
                bitmap = loaded.asImageBitmap(),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        } else {
            HarputImagePlaceholder(
                modifier = modifier,
                gradientColors = placeholderGradientColors
            )
        }
        return
    }

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
