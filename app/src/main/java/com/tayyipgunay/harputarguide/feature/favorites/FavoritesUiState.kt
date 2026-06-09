package com.tayyipgunay.harputarguide.feature.favorites

import com.tayyipgunay.harputarguide.domain.model.Place

enum class FavoritesError {
    LOAD_FAILED
}

data class FavoritesUiState(
    val isLoading: Boolean = true,
    val favoritePlaces: List<Place> = emptyList(),
    val error: FavoritesError? = null,
    val removeFailed: Boolean = false
)
