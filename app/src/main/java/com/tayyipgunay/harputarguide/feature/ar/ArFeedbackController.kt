package com.tayyipgunay.harputarguide.feature.ar

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class ArFeedbackController(context: Context) {

    private val appContext = context.applicationContext
    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 38)
    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        appContext.getSystemService(VibratorManager::class.java)?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        appContext.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    fun playCameraReady() {
        playTone(ToneGenerator.TONE_PROP_BEEP, durationMs = 90)
        vibrate(pattern = longArrayOf(0, 22))
    }

    fun playTrackingLocked() {
        playTone(ToneGenerator.TONE_PROP_ACK, durationMs = 130)
        vibrate(pattern = longArrayOf(0, 38, 45, 28))
    }

    fun playTrackingLost() {
        playTone(ToneGenerator.TONE_PROP_NACK, durationMs = 110)
        vibrate(pattern = longArrayOf(0, 28))
    }

    fun playHotspotSelected() {
        playTone(ToneGenerator.TONE_DTMF_1, durationMs = 55)
        vibrate(pattern = longArrayOf(0, 16, 28, 14))
    }

    fun playHotspotDeselected() {
        playTone(ToneGenerator.TONE_DTMF_0, durationMs = 40)
        vibrate(pattern = longArrayOf(0, 12))
    }

    fun playHotspotsRevealed() {
        playTone(ToneGenerator.TONE_DTMF_2, durationMs = 75)
        vibrate(pattern = longArrayOf(0, 14, 35, 18))
    }

    fun release() {
        toneGenerator.release()
    }

    private fun playTone(toneType: Int, durationMs: Int) {
        runCatching { toneGenerator.startTone(toneType, durationMs) }
    }

    private fun vibrate(pattern: LongArray) {
        val vib = vibrator ?: return
        if (!vib.hasVibrator()) return
        runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(pattern, -1)
            }
        }
    }
}
