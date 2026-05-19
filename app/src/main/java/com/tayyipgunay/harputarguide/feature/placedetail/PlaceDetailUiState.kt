package com.tayyipgunay.harputarguide.feature.placedetail

import com.tayyipgunay.harputarguide.domain.model.PlaceDetail

enum class PlaceDetailError {
    LOAD_FAILED,
    NOT_FOUND
}

data class PlaceDetailUiState(
    val isLoading: Boolean = true,
    val placeDetail: PlaceDetail? = null,
    val error: PlaceDetailError? = null
)
