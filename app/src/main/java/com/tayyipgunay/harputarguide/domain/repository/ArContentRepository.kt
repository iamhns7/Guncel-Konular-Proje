package com.tayyipgunay.harputarguide.domain.repository

import android.graphics.Bitmap
import com.tayyipgunay.harputarguide.data.asset.HotspotDetailData
import com.tayyipgunay.harputarguide.data.asset.StructureHotspot
import com.tayyipgunay.harputarguide.domain.model.ArPlaceContent

interface ArContentRepository {
    suspend fun getSupportedPlaceIds(): List<String>
    suspend fun isArSupported(placeId: String): Boolean
    suspend fun getPlaceContent(placeId: String): ArPlaceContent?
    suspend fun loadReferenceBitmap(placeId: String): Bitmap?
    suspend fun getHotspots(placeId: String): List<StructureHotspot>
    suspend fun getHotspotDetail(placeId: String, hotspotId: String): HotspotDetailData?
}
