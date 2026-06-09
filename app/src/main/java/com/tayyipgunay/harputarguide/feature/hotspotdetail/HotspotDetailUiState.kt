package com.tayyipgunay.harputarguide.feature.hotspotdetail

import com.tayyipgunay.harputarguide.data.asset.HotspotDetailData
import com.tayyipgunay.harputarguide.domain.model.HotspotDetailContent

data class HotspotDetailUiState(
    val isLoading: Boolean = true,
    val content: HotspotDetailContent? = null,
    val viewportPreviewAssetPath: String? = null,
    /** Malzeme analizi / yapım süreci ekranları için mevcut AR paket detayı. */
    val detail: HotspotDetailData? = null
)
