package com.tayyipgunay.harputarguide.core.common

import android.util.Log

/**
 * Merkezi loglama. Uygulama genelinde sessizce yutulan hataları görünür kılmak için kullanılır.
 * İleride Crashlytics / Timber gibi bir altyapıya tek noktadan bağlanabilir.
 */
object AppLogger {
    private const val TAG = "HarputAR"

    fun d(message: String) {
        Log.d(TAG, message)
    }

    fun w(message: String, throwable: Throwable? = null) {
        Log.w(TAG, message, throwable)
    }

    fun e(message: String, throwable: Throwable? = null) {
        Log.e(TAG, message, throwable)
    }
}
