package com.tayyipgunay.harputarguide.feature.placedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tayyipgunay.harputarguide.core.common.AppLogger
import com.tayyipgunay.harputarguide.core.locale.AppLocaleManager
import com.tayyipgunay.harputarguide.core.navigation.AppRoute
import com.tayyipgunay.harputarguide.domain.repository.ArContentRepository
import com.tayyipgunay.harputarguide.domain.repository.PlaceRepository
import com.tayyipgunay.harputarguide.domain.repository.UserPlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val placeRepository: PlaceRepository,
    private val userPlaceRepository: UserPlaceRepository,
    private val arContentRepository: ArContentRepository,
    private val localeManager: AppLocaleManager
) : ViewModel() {

    private val placeId: String =
        savedStateHandle.get<String>(AppRoute.PLACE_ID_ARG).orEmpty()

    private val _uiState = MutableStateFlow(PlaceDetailUiState())
    val uiState: StateFlow<PlaceDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            localeManager.contentLocale.collect { locale ->
                loadPlaceDetail(locale)
            }
        }
        viewModelScope.launch {
            userPlaceRepository.observeIsVisited(placeId).collect { visited ->
                _uiState.update { it.copy(isVisited = visited) }
            }
        }
        viewModelScope.launch {
            userPlaceRepository.observeIsFavorite(placeId).collect { favorite ->
                _uiState.update { it.copy(isFavorite = favorite) }
            }
        }
        viewModelScope.launch {
            val arAvailable = arContentRepository.isArSupported(placeId)
            _uiState.update { it.copy(isArAvailable = arAvailable) }
        }
    }

    fun loadPlaceDetail() {
        loadPlaceDetail(localeManager.getContentLocale())
    }

    fun toggleVisited() {
        val target = !_uiState.value.isVisited
        viewModelScope.launch {
            runCatching { userPlaceRepository.setVisited(placeId, target) }
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            message = if (target) PlaceDetailMessage.VISITED_ADDED
                            else PlaceDetailMessage.VISITED_REMOVED
                        )
                    }
                }
                .onFailure {
                    AppLogger.e("Gezildi durumu güncellenemedi (placeId=$placeId).", it)
                    _uiState.update { it.copy(message = PlaceDetailMessage.ACTION_FAILED) }
                }
        }
    }

    fun toggleFavorite() {
        val target = !_uiState.value.isFavorite
        viewModelScope.launch {
            runCatching { userPlaceRepository.setFavorite(placeId, target) }
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            message = if (target) PlaceDetailMessage.FAVORITE_ADDED
                            else PlaceDetailMessage.FAVORITE_REMOVED
                        )
                    }
                }
                .onFailure {
                    AppLogger.e("Favori durumu güncellenemedi (placeId=$placeId).", it)
                    _uiState.update { it.copy(message = PlaceDetailMessage.ACTION_FAILED) }
                }
        }
    }

    fun consumeMessage() {
        _uiState.update { it.copy(message = null) }
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
}
