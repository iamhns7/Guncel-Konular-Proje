package com.tayyipgunay.harputarguide.domain.repository

import com.tayyipgunay.harputarguide.domain.model.HotspotDetailContent

interface HotspotContentRepository {
    suspend fun getHotspotContent(placeId: String, hotspotId: String): HotspotDetailContent?
    fun assetExists(assetPath: String?): Boolean
}
