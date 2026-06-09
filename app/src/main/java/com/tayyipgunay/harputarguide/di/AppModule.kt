package com.tayyipgunay.harputarguide.di

import android.content.Context
import androidx.room.Room
import com.tayyipgunay.harputarguide.core.locale.AppLocaleManager
import com.tayyipgunay.harputarguide.data.content.HarputContentDataSource
import com.tayyipgunay.harputarguide.data.local.FavoritePlaceDao
import com.tayyipgunay.harputarguide.data.local.HarputDatabase
import com.tayyipgunay.harputarguide.data.local.VisitedPlaceDao
import com.tayyipgunay.harputarguide.data.repository.ArContentRepositoryImpl
import com.tayyipgunay.harputarguide.data.repository.HotspotContentRepositoryImpl
import com.tayyipgunay.harputarguide.data.repository.PlaceRepositoryImpl
import com.tayyipgunay.harputarguide.data.repository.UserPlaceRepositoryImpl
import com.tayyipgunay.harputarguide.domain.repository.ArContentRepository
import com.tayyipgunay.harputarguide.domain.repository.HotspotContentRepository
import com.tayyipgunay.harputarguide.domain.repository.PlaceRepository
import com.tayyipgunay.harputarguide.domain.repository.UserPlaceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocaleManager(@ApplicationContext context: Context): AppLocaleManager =
        AppLocaleManager.getInstance(context)

    @Provides
    @Singleton
    fun provideContentDataSource(@ApplicationContext context: Context): HarputContentDataSource =
        HarputContentDataSource(context)

    @Provides
    @Singleton
    fun providePlaceRepository(dataSource: HarputContentDataSource): PlaceRepository =
        PlaceRepositoryImpl(dataSource)

    @Provides
    @Singleton
    fun provideArContentRepository(
        @ApplicationContext context: Context
    ): ArContentRepository =
        ArContentRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideHotspotContentRepository(
        @ApplicationContext context: Context
    ): HotspotContentRepository =
        HotspotContentRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HarputDatabase =
        Room.databaseBuilder(context, HarputDatabase::class.java, HarputDatabase.NAME)
            .addMigrations(HarputDatabase.MIGRATION_1_2)
            .build()

    @Provides
    fun provideVisitedPlaceDao(database: HarputDatabase): VisitedPlaceDao =
        database.visitedPlaceDao()

    @Provides
    fun provideFavoritePlaceDao(database: HarputDatabase): FavoritePlaceDao =
        database.favoritePlaceDao()

    @Provides
    @Singleton
    fun provideUserPlaceRepository(
        visitedPlaceDao: VisitedPlaceDao,
        favoritePlaceDao: FavoritePlaceDao
    ): UserPlaceRepository =
        UserPlaceRepositoryImpl(visitedPlaceDao, favoritePlaceDao)
}
