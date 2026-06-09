package com.tayyipgunay.harputarguide.feature.ar

import android.graphics.RectF
import com.tayyipgunay.harputarguide.data.asset.StructureHotspot
import com.tayyipgunay.harputarguide.domain.model.ArPlaceContent

enum class ArMessage {
    FAVORITE_ADDED,
    FAVORITE_REMOVED,
    ACTION_FAILED
}

data class ArUiState(
    val isLoading: Boolean = true,
    val isArSupported: Boolean = false,
    val placeArEnabled: Boolean = false,
    val needsArCoreInstall: Boolean = false,
    val arContent: ArPlaceContent? = null,
    val hotspots: List<StructureHotspot> = emptyList(),
    val physicalWidthMeters: Float = 1f,
    val referenceImagePath: String? = null,
    val useFallbackStaticView: Boolean = false,
    val trackingState: ArTrackingState = ArTrackingState.NOT_INITIALIZED,
    val trackedImageScreenRect: RectF? = null,
    val isFavorite: Boolean = false,
    val isVisited: Boolean = false,
    val message: ArMessage? = null
)
