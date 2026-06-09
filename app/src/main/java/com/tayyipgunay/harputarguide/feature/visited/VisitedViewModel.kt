package com.tayyipgunay.harputarguide.feature.visited

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
class VisitedViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val userPlaceRepository: UserPlaceRepository,
    private val localeManager: AppLocaleManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(VisitedUiState())
    val uiState: StateFlow<VisitedUiState> = _uiState.asStateFlow()

    private val allPlaces = MutableStateFlow<List<Place>>(emptyList())

    init {
        viewModelScope.launch {
            localeManager.contentLocale.collect { locale ->
                loadPlaces(locale)
            }
        }
        viewModelScope.launch {
            combine(allPlaces, userPlaceRepository.observeVisitedIds()) { places, visitedIds ->
                places.filter { it.id in visitedIds }.map { it.copy(isVisited = true) }
            }.collect { visited ->
                _uiState.update { it.copy(visitedPlaces = visited) }
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
                allPlaces.value = places
                _uiState.update { it.copy(isLoading = false) }
            }.onFailure {
                allPlaces.value = emptyList()
                _uiState.update { it.copy(isLoading = false, error = VisitedError.LOAD_FAILED) }
            }
        }
    }
}
