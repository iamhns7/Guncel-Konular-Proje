package com.tayyipgunay.harputarguide.feature.places

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

class PlacesViewModel(
    private val placeRepository: PlaceRepository,
    private val localeManager: AppLocaleManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlacesUiState())
    val uiState: StateFlow<PlacesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            localeManager.contentLocale
                .collect { locale ->
                    loadPlaces(locale)
                }
        }
    }

    fun loadPlaces() {
        loadPlaces(localeManager.getContentLocale())
    }

    private fun loadPlaces(locale: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching {
                placeRepository.getPlaces(locale = locale)
            }.onSuccess { places ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        places = places,
                        error = if (places.isEmpty()) PlacesError.EMPTY else null
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = PlacesError.LOAD_FAILED
                    )
                }
            }
        }
    }

    companion object {
        fun factory(
            placeRepository: PlaceRepository,
            localeManager: AppLocaleManager
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PlacesViewModel::class.java)) {
                    return PlacesViewModel(placeRepository, localeManager) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
