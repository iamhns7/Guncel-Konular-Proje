package com.tayyipgunay.harputarguide.feature.places

import com.tayyipgunay.harputarguide.domain.model.Place

enum class PlacesError {
    LOAD_FAILED,
    EMPTY
}

data class PlacesUiState(
    val isLoading: Boolean = true,
    val places: List<Place> = emptyList(),
    val error: PlacesError? = null,
    val selectedFilter: String? = null
)
