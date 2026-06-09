package com.tayyipgunay.harputarguide.feature.hotspotdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tayyipgunay.harputarguide.core.navigation.AppRoute
import com.tayyipgunay.harputarguide.data.ar.ViewportPreviewPaths
import com.tayyipgunay.harputarguide.data.asset.SampleStructureHotspots
import com.tayyipgunay.harputarguide.domain.repository.ArContentRepository
import com.tayyipgunay.harputarguide.domain.repository.HotspotContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HotspotDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val hotspotContentRepository: HotspotContentRepository,
    private val arContentRepository: ArContentRepository
) : ViewModel() {

    private val placeId: String =
        savedStateHandle.get<String>(AppRoute.PLACE_ID_ARG).orEmpty()
    private val hotspotId: String =
        savedStateHandle.get<String>(AppRoute.HOTSPOT_ID_ARG).orEmpty()

    private val _uiState = MutableStateFlow(HotspotDetailUiState())
    val uiState: StateFlow<HotspotDetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    private fun loadDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val content = hotspotContentRepository.getHotspotContent(placeId, hotspotId)
            val legacyDetail = arContentRepository.getHotspotDetail(placeId, hotspotId)
                ?: SampleStructureHotspots.getDetail(placeId, hotspotId)
            val viewportPreviewPath = ViewportPreviewPaths.forHotspot(placeId, hotspotId)
                .takeIf { hotspotContentRepository.assetExists(it) }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    content = content,
                    viewportPreviewAssetPath = viewportPreviewPath,
                    detail = legacyDetail
                )
            }
        }
    }
}
