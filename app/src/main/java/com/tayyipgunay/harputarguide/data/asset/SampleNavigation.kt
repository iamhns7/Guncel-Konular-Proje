package com.tayyipgunay.harputarguide.data.asset

object SampleNavigation {
    fun destinationName(placeId: String): String =
        SamplePlaceDetails.getById(placeId).name

    fun distance(placeId: String): String =
        SamplePlaces.list.find { it.id == placeId }?.distance ?: "1.2 km"

    const val duration: String = "15 dk"
    const val instructionDistance: String = "120 m sonra"
    const val instructionAction: String = "sağa dön"
    const val approachMessage: String = "Hedefinize yaklaşıyorsunuz"
    const val routeProgress: Float = 0.68f
}
