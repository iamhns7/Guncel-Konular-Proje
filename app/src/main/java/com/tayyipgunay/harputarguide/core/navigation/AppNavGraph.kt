package com.tayyipgunay.harputarguide.core.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tayyipgunay.harputarguide.feature.about.AboutScreen
import com.tayyipgunay.harputarguide.feature.ar.ARScreen
import com.tayyipgunay.harputarguide.feature.constructionprocess.ConstructionProcessScreen
import com.tayyipgunay.harputarguide.feature.hotspotdetail.HotspotDetailScreen
import com.tayyipgunay.harputarguide.feature.faq.FaqScreen
import com.tayyipgunay.harputarguide.feature.favorites.FavoritesScreen
import com.tayyipgunay.harputarguide.feature.favorites.FavoritesViewModel
import com.tayyipgunay.harputarguide.feature.home.HomeScreen
import com.tayyipgunay.harputarguide.feature.onboarding.OnboardingScreen
import com.tayyipgunay.harputarguide.feature.placedetail.PlaceDetailScreen
import com.tayyipgunay.harputarguide.feature.placedetail.PlaceDetailViewModel
import com.tayyipgunay.harputarguide.feature.places.PlacesScreen
import com.tayyipgunay.harputarguide.feature.places.PlacesViewModel
import com.tayyipgunay.harputarguide.feature.splash.SplashScreen
import com.tayyipgunay.harputarguide.feature.visited.VisitedScreen
import com.tayyipgunay.harputarguide.feature.visited.VisitedViewModel
import com.tayyipgunay.harputarguide.core.locale.AppLocaleManager
import com.tayyipgunay.harputarguide.feature.welcome.WelcomeScreen

@Composable
fun AppNavGraph(
    localeManager: AppLocaleManager,
    onAppLocaleChanged: () -> Unit
) {
    val navController = rememberNavController()
    val currentLanguage = localeManager.contentLocale.collectAsStateWithLifecycle().value

    NavHost(
        navController = navController,
        startDestination = AppRoute.SPLASH
    ) {
        composable(AppRoute.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(AppRoute.WELCOME) {
                        popUpTo(AppRoute.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoute.WELCOME) {
            WelcomeScreen(
                onStartTourClick = { navController.navigate(AppRoute.ONBOARDING) },
                onFavoritesClick = { navController.navigate(AppRoute.FAVORITES) },
                onAboutClick = { navController.navigate(AppRoute.ABOUT) }
            )
        }

        composable(AppRoute.ONBOARDING) {
            OnboardingScreen(
                onBackClick = { navController.popBackStack() },
                onStartClick = {
                    navController.navigate(AppRoute.HOME) {
                        popUpTo(AppRoute.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoute.HOME) {
            HomeScreen(
                onStartTourClick = { navController.navigateToPlacesTab() },
                onFavoritesClick = { navController.navigateFromHomeHub(AppRoute.FAVORITES) },
                onVisitedClick = { navController.navigateFromHomeHub(AppRoute.VISITED) },
                onFaqClick = { navController.navigateFromHomeHub(AppRoute.FAQ) },
                onAboutClick = { navController.navigateFromHomeHub(AppRoute.ABOUT) }
            )
        }

        composable(AppRoute.PLACES) {
            val placesViewModel: PlacesViewModel = hiltViewModel()
            val placesUiState = placesViewModel.uiState.collectAsStateWithLifecycle().value

            PlacesScreen(
                uiState = placesUiState,
                onRetry = placesViewModel::loadPlaces,
                onPlaceClick = { placeId ->
                    navController.navigate(AppRoute.placeDetail(placeId))
                },
                onHomeClick = { navController.navigateToHomeTab() },
                onVisitedClick = { navController.navigateBetweenExploreTabs(AppRoute.VISITED) },
                onFavoritesClick = { navController.navigateBetweenExploreTabs(AppRoute.FAVORITES) },
                onAboutClick = { navController.navigateBetweenExploreTabs(AppRoute.ABOUT) }
            )
        }

        composable(
            route = AppRoute.PLACE_DETAIL,
            arguments = listOf(navArgument(AppRoute.PLACE_ID_ARG) { type = NavType.StringType })
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getString(AppRoute.PLACE_ID_ARG).orEmpty()
            val placeDetailViewModel: PlaceDetailViewModel = hiltViewModel()
            val placeDetailUiState = placeDetailViewModel.uiState.collectAsStateWithLifecycle().value

            PlaceDetailScreen(
                placeId = placeId,
                uiState = placeDetailUiState,
                onRetry = placeDetailViewModel::loadPlaceDetail,
                onToggleVisited = placeDetailViewModel::toggleVisited,
                onToggleFavorite = placeDetailViewModel::toggleFavorite,
                onMessageConsumed = placeDetailViewModel::consumeMessage,
                onBackClick = { navController.popBackStack() },
                onArClick = { id ->
                    navController.navigate(AppRoute.ar(id))
                },
                onHomeClick = { navController.navigateToHomeTab() },
                onPlacesClick = { navController.navigateToPlacesTab() },
                onVisitedClick = { navController.navigateBetweenExploreTabs(AppRoute.VISITED) },
                onFavoritesClick = { navController.navigateBetweenExploreTabs(AppRoute.FAVORITES) },
                onAboutClick = { navController.navigateBetweenExploreTabs(AppRoute.ABOUT) }
            )
        }

        composable(
            route = AppRoute.AR,
            arguments = listOf(navArgument(AppRoute.PLACE_ID_ARG) { type = NavType.StringType })
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getString(AppRoute.PLACE_ID_ARG).orEmpty()
            ARScreen(
                placeId = placeId,
                onBackClick = { navController.popBackStack() },
                onPlacesClick = { navController.navigateToPlacesTab() },
                onHotspotDetailClick = { pid, hotspotId ->
                    navController.navigate(AppRoute.hotspotDetail(pid, hotspotId))
                }
            )
        }

        composable(
            route = AppRoute.HOTSPOT_DETAIL,
            arguments = listOf(
                navArgument(AppRoute.PLACE_ID_ARG) { type = NavType.StringType },
                navArgument(AppRoute.HOTSPOT_ID_ARG) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getString(AppRoute.PLACE_ID_ARG).orEmpty()
            val hotspotId = backStackEntry.arguments?.getString(AppRoute.HOTSPOT_ID_ARG).orEmpty()
            HotspotDetailScreen(
                placeId = placeId,
                hotspotId = hotspotId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = AppRoute.CONSTRUCTION_PROCESS,
            arguments = listOf(
                navArgument(AppRoute.PLACE_ID_ARG) { type = NavType.StringType },
                navArgument(AppRoute.HOTSPOT_ID_ARG) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getString(AppRoute.PLACE_ID_ARG).orEmpty()
            val hotspotId = backStackEntry.arguments?.getString(AppRoute.HOTSPOT_ID_ARG).orEmpty()
            ConstructionProcessScreen(
                placeId = placeId,
                hotspotId = hotspotId,
                onBackClick = { navController.popBackStack() },
                onCloseClick = {
                    navController.popBackStack(AppRoute.ar(placeId), inclusive = false)
                }
            )
        }

        composable(
            route = AppRoute.MATERIAL_ANALYSIS,
            arguments = listOf(
                navArgument(AppRoute.PLACE_ID_ARG) { type = NavType.StringType },
                navArgument(AppRoute.HOTSPOT_ID_ARG) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getString(AppRoute.PLACE_ID_ARG).orEmpty()
            val hotspotId = backStackEntry.arguments?.getString(AppRoute.HOTSPOT_ID_ARG).orEmpty()
            HotspotDetailScreen(
                placeId = placeId,
                hotspotId = hotspotId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = AppRoute.MODEL_VIEWER,
            arguments = listOf(
                navArgument(AppRoute.PLACE_ID_ARG) { type = NavType.StringType },
                navArgument(AppRoute.HOTSPOT_ID_ARG) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getString(AppRoute.PLACE_ID_ARG).orEmpty()
            val hotspotId = backStackEntry.arguments?.getString(AppRoute.HOTSPOT_ID_ARG).orEmpty()
            HotspotDetailScreen(
                placeId = placeId,
                hotspotId = hotspotId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppRoute.FAVORITES) {
            val favoritesViewModel: FavoritesViewModel = hiltViewModel()
            val favoritesUiState = favoritesViewModel.uiState.collectAsStateWithLifecycle().value
            FavoritesScreen(
                uiState = favoritesUiState,
                onRetry = favoritesViewModel::loadPlaces,
                onRemoveFavorite = favoritesViewModel::removeFavorite,
                onRemoveFailedConsumed = favoritesViewModel::consumeRemoveFailed,
                onBackClick = { navController.popBackStack() },
                onPlaceClick = { placeId ->
                    navController.navigate(AppRoute.placeDetail(placeId))
                },
                onHomeClick = { navController.navigateToHomeTab() },
                onPlacesClick = { navController.navigateToPlacesTab() },
                onVisitedClick = { navController.navigateBetweenExploreTabs(AppRoute.VISITED) },
                onFavoritesClick = { },
                onAboutClick = { navController.navigateBetweenExploreTabs(AppRoute.ABOUT) }
            )
        }

        composable(AppRoute.VISITED) {
            val visitedViewModel: VisitedViewModel = hiltViewModel()
            val visitedUiState = visitedViewModel.uiState.collectAsStateWithLifecycle().value
            VisitedScreen(
                uiState = visitedUiState,
                onRetry = visitedViewModel::loadPlaces,
                onBackClick = { navController.popBackStack() },
                onPlaceClick = { placeId ->
                    navController.navigate(AppRoute.placeDetail(placeId))
                },
                onHomeClick = { navController.navigateToHomeTab() },
                onPlacesClick = { navController.navigateToPlacesTab() },
                onVisitedClick = { },
                onFavoritesClick = { navController.navigateBetweenExploreTabs(AppRoute.FAVORITES) },
                onAboutClick = { navController.navigateBetweenExploreTabs(AppRoute.ABOUT) }
            )
        }

        composable(AppRoute.FAQ) {
            FaqScreen(
                onBackClick = { navController.popBackStack() },
                onHomeClick = { navController.navigateToHomeTab() },
                onPlacesClick = { navController.navigateToPlacesTab() },
                onVisitedClick = { navController.navigateBetweenExploreTabs(AppRoute.VISITED) },
                onFavoritesClick = { navController.navigateBetweenExploreTabs(AppRoute.FAVORITES) },
                onAboutClick = { navController.navigateBetweenExploreTabs(AppRoute.ABOUT) }
            )
        }

        composable(AppRoute.ABOUT) {
            AboutScreen(
                selectedLanguage = currentLanguage,
                onLanguageSelected = { languageCode ->
                    localeManager.setContentLocale(languageCode)
                    onAppLocaleChanged()
                },
                onBackClick = { navController.popBackStack() },
                onHomeClick = { navController.navigateToHomeTab() },
                onPlacesClick = { navController.navigateToPlacesTab() },
                onVisitedClick = { navController.navigateBetweenExploreTabs(AppRoute.VISITED) },
                onFavoritesClick = { navController.navigateBetweenExploreTabs(AppRoute.FAVORITES) },
                onFaqClick = { navController.navigateBetweenExploreTabs(AppRoute.FAQ) }
            )
        }
    }
}
