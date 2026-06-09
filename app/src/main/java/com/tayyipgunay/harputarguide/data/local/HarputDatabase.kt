package com.tayyipgunay.harputarguide.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [VisitedPlaceEntity::class, FavoritePlaceEntity::class],
    version = 2,
    exportSchema = false
)
abstract class HarputDatabase : RoomDatabase() {
    abstract fun visitedPlaceDao(): VisitedPlaceDao
    abstract fun favoritePlaceDao(): FavoritePlaceDao

    companion object {
        const val NAME = "harput_database"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS favorite_places " +
                        "(placeId TEXT NOT NULL PRIMARY KEY, addedAt INTEGER NOT NULL)"
                )
            }
        }
    }
}
