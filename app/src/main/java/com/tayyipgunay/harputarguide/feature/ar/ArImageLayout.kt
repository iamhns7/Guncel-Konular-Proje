package com.tayyipgunay.harputarguide.feature.ar

import kotlin.math.roundToInt

data class ArImageDisplayBounds(
    val left: Float,
    val top: Float,
    val width: Float,
    val height: Float
)

object ArImageLayout {

    fun computeFitBounds(
        imageWidthPx: Float,
        imageHeightPx: Float,
        containerWidthPx: Float,
        containerHeightPx: Float
    ): ArImageDisplayBounds {
        if (imageWidthPx <= 0f || imageHeightPx <= 0f ||
            containerWidthPx <= 0f || containerHeightPx <= 0f
        ) {
            return ArImageDisplayBounds(0f, 0f, containerWidthPx, containerHeightPx)
        }

        val imageAspect = imageWidthPx / imageHeightPx
        val containerAspect = containerWidthPx / containerHeightPx

        return if (imageAspect > containerAspect) {
            val width = containerWidthPx
            val height = width / imageAspect
            ArImageDisplayBounds(
                left = 0f,
                top = ((containerHeightPx - height) / 2f).roundToInt().toFloat(),
                width = width,
                height = height
            )
        } else {
            val height = containerHeightPx
            val width = height * imageAspect
            ArImageDisplayBounds(
                left = ((containerWidthPx - width) / 2f).roundToInt().toFloat(),
                top = 0f,
                width = width,
                height = height
            )
        }
    }
}
