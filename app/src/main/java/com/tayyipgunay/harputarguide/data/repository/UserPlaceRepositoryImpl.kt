package com.tayyipgunay.harputarguide.data.repository

import com.tayyipgunay.harputarguide.data.local.FavoritePlaceDao
import com.tayyipgunay.harputarguide.data.local.FavoritePlaceEntity
import com.tayyipgunay.harputarguide.data.local.VisitedPlaceDao
import com.tayyipgunay.harputarguide.data.local.VisitedPlaceEntity
import com.tayyipgunay.harputarguide.domain.repository.UserPlaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPlaceRepositoryImpl(
    private val visitedPlaceDao: VisitedPlaceDao,
    private val favoritePlaceDao: FavoritePlaceDao
) : UserPlaceRepository {

    override fun observeVisitedIds(): Flow<Set<String>> =
        visitedPlaceDao.observeVisitedIds().map { it.toSet() }

    override fun observeIsVisited(placeId: String): Flow<Boolean> =
        visitedPlaceDao.observeIsVisited(placeId)

    override suspend fun setVisited(placeId: String, visited: Boolean) {
        if (visited) {
            visitedPlaceDao.insert(
                VisitedPlaceEntity(placeId = placeId, visitedAt = System.currentTimeMillis())
            )
        } else {
            visitedPlaceDao.delete(placeId)
        }
    }

    override fun observeFavoriteIds(): Flow<Set<String>> =
        favoritePlaceDao.observeFavoriteIds().map { it.toSet() }

    override fun observeIsFavorite(placeId: String): Flow<Boolean> =
        favoritePlaceDao.observeIsFavorite(placeId)

    override suspend fun setFavorite(placeId: String, favorite: Boolean) {
        if (favorite) {
            favoritePlaceDao.insert(
                FavoritePlaceEntity(placeId = placeId, addedAt = System.currentTimeMillis())
            )
        } else {
            favoritePlaceDao.delete(placeId)
        }
    }
}
