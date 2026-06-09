package com.tayyipgunay.harputarguide.feature.hotspotdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tayyipgunay.harputarguide.domain.model.HotspotDetailContent
import com.tayyipgunay.harputarguide.domain.repository.HotspotContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HotspotContentLoaderViewModel @Inject constructor(
    private val hotspotContentRepository: HotspotContentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HotspotContentLoaderUiState())
    val uiState: StateFlow<HotspotContentLoaderUiState> = _uiState.asStateFlow()

    fun load(placeId: String, hotspotId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = false) }
            val content = hotspotContentRepository.getHotspotContent(placeId, hotspotId)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    content = content,
                    error = content == null
                )
            }
        }
    }

    fun clear() {
        _uiState.value = HotspotContentLoaderUiState()
    }
}

data class HotspotContentLoaderUiState(
    val isLoading: Boolean = false,
    val content: HotspotDetailContent? = null,
    val error: Boolean = false
)
