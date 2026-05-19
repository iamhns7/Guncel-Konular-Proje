package com.tayyipgunay.harputarguide.core.locale

import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

/**
 * Persists UI + content locale (tr/en). Content JSON paths use [contentLocale];
 * [wrapContext] applies the same locale to Android resources (strings.xml).
 */
class AppLocaleManager private constructor(
    context: Context
) {
    private val appContext = context.applicationContext
    private val preferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _contentLocale = MutableStateFlow(readStoredContentLocale())
    val contentLocale: StateFlow<String> = _contentLocale.asStateFlow()

    fun getContentLocale(): String = _contentLocale.value

    fun setContentLocale(languageCode: String) {
        val normalized = AppLanguage.normalize(languageCode)
        if (_contentLocale.value == normalized) return
        preferences.edit().putString(KEY_CONTENT_LOCALE, normalized).apply()
        _contentLocale.value = normalized
    }

    fun wrapContext(base: Context): Context {
        val locale = Locale.forLanguageTag(getContentLocale())
        val config = Configuration(base.resources.configuration)
        config.setLocales(LocaleList(locale))
        return base.createConfigurationContext(config)
    }

    private fun readStoredContentLocale(): String {
        val stored = preferences.getString(KEY_CONTENT_LOCALE, null)
        return AppLanguage.normalize(stored)
    }

    companion object {
        private const val PREFS_NAME = "harput_app_locale"
        private const val KEY_CONTENT_LOCALE = "content_locale"

        @Volatile
        private var instance: AppLocaleManager? = null

        fun getInstance(context: Context): AppLocaleManager {
            return instance ?: synchronized(this) {
                instance ?: AppLocaleManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
