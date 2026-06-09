package com.tayyipgunay.harputarguide.core.common

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

fun openGoogleMapsNavigation(
    context: Context,
    latitude: Double,
    longitude: Double,
    mode: String = "w"
) {
    val uri = Uri.parse("google.navigation:q=$latitude,$longitude&mode=$mode")
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.google.android.apps.maps")
    }

    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        val fallbackUri = Uri.parse(
            "https://www.google.com/maps/dir/?api=1" +
                "&destination=$latitude,$longitude" +
                "&travelmode=walking"
        )
        context.startActivity(Intent(Intent.ACTION_VIEW, fallbackUri))
    }
}
