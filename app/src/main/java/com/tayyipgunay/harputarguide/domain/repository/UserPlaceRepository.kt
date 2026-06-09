package com.tayyipgunay.harputarguide.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Kullanıcıya özel kalıcı veriler (gezildi ve favori işaretleri). Room ile saklanır.
 */
interface UserPlaceRepository {
    fun observeVisitedIds(): Flow<Set<String>>
    fun observeIsVisited(placeId: String): Flow<Boolean>
    suspend fun setVisited(placeId: String, visited: Boolean)

    fun observeFavoriteIds(): Flow<Set<String>>
    fun observeIsFavorite(placeId: String): Flow<Boolean>
    suspend fun setFavorite(placeId: String, favorite: Boolean)
}
