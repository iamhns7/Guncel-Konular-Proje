package com.tayyipgunay.harputarguide.feature.placedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tayyipgunay.harputarguide.core.locale.AppLocaleManager
import com.tayyipgunay.harputarguide.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaceDetailViewModel(
    private val placeId: String,
    private val placeRepository: PlaceRepository,
    private val localeManager: AppLocaleManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaceDetailUiState())
    val uiState: StateFlow<PlaceDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            localeManager.contentLocale
                .collect { locale ->
                    loadPlaceDetail(locale)
                }
        }
    }

    fun loadPlaceDetail() {
        loadPlaceDetail(localeManager.getContentLocale())
    }

    private fun loadPlaceDetail(locale: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, error = null, placeDetail = null)
            }
            runCatching {
                placeRepository.getPlaceDetail(placeId = placeId, locale = locale)
            }.onSuccess { detail ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        placeDetail = detail,
                        error = if (detail == null) PlaceDetailError.NOT_FOUND else null
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        placeDetail = null,
                        error = PlaceDetailError.LOAD_FAILED
                    )
                }
            }
        }
    }

    companion object {
        fun factory(
            placeId: String,
            placeRepository: PlaceRepository,
            localeManager: AppLocaleManager
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PlaceDetailViewModel::class.java)) {
                    return PlaceDetailViewModel(placeId, placeRepository, localeManager) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
