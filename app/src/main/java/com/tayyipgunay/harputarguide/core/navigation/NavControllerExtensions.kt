package com.tayyipgunay.harputarguide.core.navigation

import androidx.navigation.NavController

/** Home hub üzerinden yan sekme ekranlarına (Favoriler, FAQ, …) gider; geri yığını temiz kalır. */
fun NavController.navigateFromHomeHub(route: String) {
    navigate(route) {
        popUpTo(AppRoute.HOME) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

/** Alt bardan Ana Menü — mevcut Home örneğine döner veya state geri yükler. */
fun NavController.navigateToHomeTab() {
    navigate(AppRoute.HOME) {
        popUpTo(AppRoute.HOME) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

/** Alt bardan Noktalar — Home altında tek Places örneği. */
fun NavController.navigateToPlacesTab() {
    navigate(AppRoute.PLACES) {
        popUpTo(AppRoute.HOME) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

/** Explore bar sekmeleri arası geçiş (Favoriler ↔ Gezdiğim ↔ Hakkında ↔ FAQ). */
fun NavController.navigateBetweenExploreTabs(route: String) {
    navigate(route) {
        popUpTo(AppRoute.HOME) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

/** Home veya kısayollardan doğrudan AR kamera ekranı. */
fun NavController.navigateToDefaultAr() {
    navigate(AppRoute.ar(AppRoute.DEFAULT_AR_PLACE_ID))
}
