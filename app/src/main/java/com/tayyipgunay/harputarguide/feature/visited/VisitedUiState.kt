package com.tayyipgunay.harputarguide.feature.visited

import com.tayyipgunay.harputarguide.domain.model.Place

enum class VisitedError {
    LOAD_FAILED
}

data class VisitedUiState(
    val isLoading: Boolean = true,
    val visitedPlaces: List<Place> = emptyList(),
    val error: VisitedError? = null
)
