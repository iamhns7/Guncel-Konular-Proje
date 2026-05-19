package com.tayyipgunay.harputarguide.data.repository

import android.content.Context
import com.tayyipgunay.harputarguide.core.locale.AppLocaleManager
import com.tayyipgunay.harputarguide.data.content.HarputContentDataSource
import com.tayyipgunay.harputarguide.domain.repository.PlaceRepository

/**
 * Manual wiring until Hilt is introduced. Remove when DI is added.
 */
object RepositoryProvider {

    @Volatile
    private var placeRepository: PlaceRepository? = null

    fun providePlaceRepository(context: Context): PlaceRepository {
        return placeRepository ?: synchronized(this) {
            placeRepository ?: PlaceRepositoryImpl(
                dataSource = HarputContentDataSource(context.applicationContext)
            ).also { placeRepository = it }
        }
    }

    fun provideLocaleManager(context: Context): AppLocaleManager =
        AppLocaleManager.getInstance(context.applicationContext)
}
