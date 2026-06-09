package com.tayyipgunay.harputarguide.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tayyipgunay.harputarguide.core.common.AppLogger
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
class FavoritesViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val userPlaceRepository: UserPlaceRepository,
    private val localeManager: AppLocaleManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private val allPlaces = MutableStateFlow<List<Place>>(emptyList())

    init {
        viewModelScope.launch {
            localeManager.contentLocale.collect { locale ->
                loadPlaces(locale)
            }
        }
        viewModelScope.launch {
            combine(allPlaces, userPlaceRepository.observeFavoriteIds()) { places, favoriteIds ->
                places.filter { it.id in favoriteIds }
            }.collect { favorites ->
                _uiState.update { it.copy(favoritePlaces = favorites) }
            }
        }
    }

    fun loadPlaces() {
        loadPlaces(localeManager.getContentLocale())
    }

    fun removeFavorite(placeId: String) {
        viewModelScope.launch {
            runCatching { userPlaceRepository.setFavorite(placeId, false) }
                .onFailure {
                    AppLogger.e("Favori kaldırılamadı (placeId=$placeId).", it)
                    _uiState.update { it.copy(removeFailed = true) }
                }
        }
    }

    fun consumeRemoveFailed() {
        _uiState.update { it.copy(removeFailed = false) }
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
                _uiState.update { it.copy(isLoading = false, error = FavoritesError.LOAD_FAILED) }
            }
        }
    }
}
