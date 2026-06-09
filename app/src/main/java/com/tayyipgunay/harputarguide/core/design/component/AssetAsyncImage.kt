package com.tayyipgunay.harputarguide.core.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.tayyipgunay.harputarguide.R
import com.tayyipgunay.harputarguide.core.design.theme.HarputColors

@Composable
fun AssetAsyncImage(
    assetPath: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    highQuality: Boolean = false
) {
    if (assetPath.isNullOrBlank()) return
    val context = LocalContext.current
    val requestBuilder = ImageRequest.Builder(context)
        .data("file:///android_asset/$assetPath")
        .crossfade(true)
    if (highQuality) {
        requestBuilder
            .size(Size.ORIGINAL)
            .allowHardware(false)
    }
    SubcomposeAsyncImage(
        model = requestBuilder.build(),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        loading = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        },
        error = {
            AssetImageErrorPlaceholder(modifier = Modifier.fillMaxSize())
        }
    )
}

@Composable
fun AssetImageErrorPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(HarputColors.IconBg.copy(alpha = 0.55f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.layout.Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.BrokenImage,
                contentDescription = null,
                tint = HarputColors.SoftBrown
            )
            Text(
                text = stringResource(R.string.hotspot_image_load_failed),
                color = HarputColors.SoftBrown,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
