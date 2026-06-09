package com.tayyipgunay.harputarguide.feature.ar

import android.util.Log

/**
 * AR hata ayıklama — Logcat'te `AR_DEBUG` ile filtreleyin.
 */
object ArDebugLog {
    private const val TAG = "AR_DEBUG"

    fun log(message: String) {
        println("$TAG: $message")
        Log.d(TAG, message)
    }

    fun warn(message: String, throwable: Throwable? = null) {
        println("$TAG WARN: $message${throwable?.let { " — ${it.message}" } ?: ""}")
        Log.w(TAG, message, throwable)
    }

    fun error(message: String, throwable: Throwable? = null) {
        println("$TAG ERROR: $message${throwable?.let { " — ${it.message}" } ?: ""}")
        Log.e(TAG, message, throwable)
    }
}
