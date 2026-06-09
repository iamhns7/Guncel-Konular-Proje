package com.tayyipgunay.harputarguide.feature.placedetail

import com.tayyipgunay.harputarguide.domain.model.PlaceDetail

enum class PlaceDetailError {
    LOAD_FAILED,
    NOT_FOUND
}

/** Tek seferlik kullanıcı geri bildirim mesajları (snackbar). */
enum class PlaceDetailMessage {
    VISITED_ADDED,
    VISITED_REMOVED,
    FAVORITE_ADDED,
    FAVORITE_REMOVED,
    ACTION_FAILED
}

data class PlaceDetailUiState(
    val isLoading: Boolean = true,
    val placeDetail: PlaceDetail? = null,
    val error: PlaceDetailError? = null,
    val isVisited: Boolean = false,
    val isFavorite: Boolean = false,
    val isArAvailable: Boolean = false,
    val message: PlaceDetailMessage? = null
)
