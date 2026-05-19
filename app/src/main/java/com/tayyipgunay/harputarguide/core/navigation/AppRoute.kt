package com.tayyipgunay.harputarguide.core.navigation

object AppRoute {
    /** Varsayılan AR demo mekânı; bkz. [DEFAULT_AR_PLACE_ID]. */
    const val DEFAULT_AR_PLACE_ID = "kale_surlari"

    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val WELCOME = "welcome"
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val PLACES = "places"
    const val FAVORITES = "favorites"
    const val VISITED = "visited"
    const val FAQ = "faq"
    const val ABOUT = "about"

    const val PLACE_ID_ARG = "placeId"
    const val HOTSPOT_ID_ARG = "hotspotId"

    const val PLACE_DETAIL = "place_detail/{$PLACE_ID_ARG}"
    const val TOUR_NAVIGATION = "tour_navigation/{$PLACE_ID_ARG}"
    const val AR = "ar/{$PLACE_ID_ARG}"
    const val MODEL_VIEWER = "model_viewer/{$PLACE_ID_ARG}"
    const val HOTSPOT_DETAIL = "hotspot_detail/{$PLACE_ID_ARG}/{$HOTSPOT_ID_ARG}"
    const val CONSTRUCTION_PROCESS = "construction_process/{$PLACE_ID_ARG}/{$HOTSPOT_ID_ARG}"
    const val MATERIAL_ANALYSIS = "material_analysis/{$PLACE_ID_ARG}/{$HOTSPOT_ID_ARG}"

    fun placeDetail(placeId: String): String = "place_detail/$placeId"
    fun tourNavigation(placeId: String): String = "tour_navigation/$placeId"
    fun ar(placeId: String): String = "ar/$placeId"
    fun modelViewer(placeId: String): String = "model_viewer/$placeId"
    fun hotspotDetail(placeId: String, hotspotId: String): String =
        "hotspot_detail/$placeId/$hotspotId"
    fun constructionProcess(placeId: String, hotspotId: String): String =
        "construction_process/$placeId/$hotspotId"
    fun materialAnalysis(placeId: String, hotspotId: String): String =
        "material_analysis/$placeId/$hotspotId"
}
