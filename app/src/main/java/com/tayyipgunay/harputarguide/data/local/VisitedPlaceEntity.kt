package com.tayyipgunay.harputarguide.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visited_places")
data class VisitedPlaceEntity(
    @PrimaryKey val placeId: String,
    val visitedAt: Long
)
