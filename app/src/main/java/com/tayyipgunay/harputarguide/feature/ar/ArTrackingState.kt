package com.tayyipgunay.harputarguide.feature.ar

enum class ArTrackingState {
    NOT_INITIALIZED,
    UNSUPPORTED,
    PERMISSION_DENIED,
    SEARCHING,
    /** Kısmi eşleşme — %100 hizalama gerekmez. */
    ALIGNED,
    TRACKING,
    LOST
}

/** Hotspot gösterimi için yeterli hizalama (tam kilit şart değil). */
fun ArTrackingState.isAlignedEnough(): Boolean =
    this == ArTrackingState.TRACKING || this == ArTrackingState.ALIGNED

