package com.tayyipgunay.harputarguide.feature.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tayyipgunay.harputarguide.core.locale.AppLocaleManager
import com.tayyipgunay.harputarguide.domain.model.Place
import com.tayyipgunay.harputarguide.domain.repository.PlaceRepository
import com.tayyipgunay.harputarguide.domain.repository.UserPlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val userPlaceRepository: UserPlaceRepository,
    private val localeManager: AppLocaleManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlacesUiState())
    val uiState: StateFlow<PlacesUiState> = _uiState.asStateFlow()

    private val rawPlaces = MutableStateFlow<List<Place>>(emptyList())

    init {
        viewModelScope.launch {
            localeManager.contentLocale.collect { locale ->
                loadPlaces(locale)
            }
        }
        viewModelScope.launch {
            combine(rawPlaces, userPlaceRepository.observeVisitedIds()) { places, visitedIds ->
                places.map { it.copy(isVisited = it.id in visitedIds) }
            }.collect { merged ->
                _uiState.update { it.copy(places = merged) }
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
                rawPlaces.value = places
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = if (places.isEmpty()) PlacesError.EMPTY else null
                    )
                }
            }.onFailure {
                rawPlaces.value = emptyList()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = PlacesError.LOAD_FAILED
                    )
                }
            }
        }
    }
}
