package com.tayyipgunay.harputarguide.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitedPlaceDao {

    @Query("SELECT placeId FROM visited_places")
    fun observeVisitedIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM visited_places WHERE placeId = :placeId)")
    fun observeIsVisited(placeId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: VisitedPlaceEntity)

    @Query("DELETE FROM visited_places WHERE placeId = :placeId")
    suspend fun delete(placeId: String)
}
