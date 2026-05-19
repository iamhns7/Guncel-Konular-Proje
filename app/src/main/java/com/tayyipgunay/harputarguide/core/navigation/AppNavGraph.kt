package com.tayyipgunay.harputarguide.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tayyipgunay.harputarguide.feature.about.AboutScreen
import com.tayyipgunay.harputarguide.feature.ar.ARScreen
import com.tayyipgunay.harputarguide.feature.auth.LoginScreen
import com.tayyipgunay.harputarguide.feature.constructionprocess.ConstructionProcessScreen
import com.tayyipgunay.harputarguide.feature.hotspotdetail.HotspotDetailScreen
import com.tayyipgunay.harputarguide.feature.materialanalysis.MaterialAnalysisScreen
import com.tayyipgunay.harputarguide.feature.faq.FaqScreen
import com.tayyipgunay.harputarguide.feature.favorites.FavoritesScreen
import com.tayyipgunay.harputarguide.feature.home.HomeScreen
import com.tayyipgunay.harputarguide.feature.modelviewer.ModelViewerScreen
import com.tayyipgunay.harputarguide.feature.onboarding.OnboardingScreen
import com.tayyipgunay.harputarguide.data.repository.RepositoryProvider
import com.tayyipgunay.harputarguide.feature.placedetail.PlaceDetailScreen
import com.tayyipgunay.harputarguide.feature.placedetail.PlaceDetailViewModel
import com.tayyipgunay.harputarguide.feature.places.PlacesScreen
import com.tayyipgunay.harputarguide.feature.places.PlacesViewModel
import com.tayyipgunay.harputarguide.feature.splash.SplashScreen
import com.tayyipgunay.harputarguide.feature.tournavigation.TourNavigationScreen
import com.tayyipgunay.harputarguide.feature.visited.VisitedScreen
import com.tayyipgunay.harputarguide.core.locale.AppLocaleManager
import com.tayyipgunay.harputarguide.feature.welcome.WelcomeScreen

@Composable
fun AppNavGraph(
    localeManager: AppLocaleManager,
    onAppLocaleChanged: () -> Unit
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val placeRepository = remember {
        RepositoryProvider.providePlaceRepository(context.applicationContext)
    }
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
                onStartTourClick = { navController.navigate(AppRoute.LOGIN) },
                onFavoritesClick = { navController.navigate(AppRoute.FAVORITES) },
                onAboutClick = { navController.navigate(AppRoute.ABOUT) }
            )
        }

        composable(AppRoute.LOGIN) {
            LoginScreen(
                onGoogleSignInClick = {
                    navController.navigate(AppRoute.ONBOARDING)
                }
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
                onOpenArClick = { navController.navigateToDefaultAr() },
                onFavoritesClick = { navController.navigateFromHomeHub(AppRoute.FAVORITES) },
                onVisitedClick = { navController.navigateFromHomeHub(AppRoute.VISITED) },
                onFaqClick = { navController.navigateFromHomeHub(AppRoute.FAQ) },
                onAboutClick = { navController.navigateFromHomeHub(AppRoute.ABOUT) }
            )
        }

        composable(AppRoute.PLACES) {
            val placesViewModel: PlacesViewModel = viewModel(
                factory = PlacesViewModel.factory(placeRepository, localeManager)
            )
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
            val placeDetailViewModel: PlaceDetailViewModel = viewModel(
                factory = PlaceDetailViewModel.factory(placeId, placeRepository, localeManager)
            )
            val placeDetailUiState = placeDetailViewModel.uiState.collectAsStateWithLifecycle().value

            PlaceDetailScreen(
                placeId = placeId,
                uiState = placeDetailUiState,
                onRetry = placeDetailViewModel::loadPlaceDetail,
                onBackClick = { navController.popBackStack() },
                onNavigateClick = { id ->
                    navController.navigate(AppRoute.tourNavigation(id))
                },
                onArClick = { id ->
                    navController.navigate(AppRoute.ar(id))
                },
                onModelViewerClick = { id ->
                    navController.navigate(AppRoute.modelViewer(id))
                },
                onHomeClick = { navController.navigateToHomeTab() },
                onPlacesClick = { navController.navigateToPlacesTab() },
                onVisitedClick = { navController.navigateBetweenExploreTabs(AppRoute.VISITED) },
                onFavoritesClick = { navController.navigateBetweenExploreTabs(AppRoute.FAVORITES) },
                onAboutClick = { navController.navigateBetweenExploreTabs(AppRoute.ABOUT) }
            )
        }

        composable(
            route = AppRoute.TOUR_NAVIGATION,
            arguments = listOf(navArgument(AppRoute.PLACE_ID_ARG) { type = NavType.StringType })
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getString(AppRoute.PLACE_ID_ARG).orEmpty()
            TourNavigationScreen(
                placeId = placeId,
                onCancelClick = { navController.popBackStack() },
                onArClick = { id -> navController.navigate(AppRoute.ar(id)) },
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
                onBackClick = { navController.popBackStack() },
                onCloseClick = {
                    navController.popBackStack(AppRoute.ar(placeId), inclusive = false)
                },
                onConstructionProcessClick = { pid, hid ->
                    navController.navigate(AppRoute.constructionProcess(pid, hid))
                },
                onMaterialAnalysisClick = { pid, hid ->
                    navController.navigate(AppRoute.materialAnalysis(pid, hid))
                },
                onModelViewerClick = { pid ->
                    navController.navigate(AppRoute.modelViewer(pid))
                }
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
            MaterialAnalysisScreen(
                placeId = placeId,
                hotspotId = hotspotId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = AppRoute.MODEL_VIEWER,
            arguments = listOf(navArgument(AppRoute.PLACE_ID_ARG) { type = NavType.StringType })
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getString(AppRoute.PLACE_ID_ARG).orEmpty()
            ModelViewerScreen(
                placeId = placeId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoute.FAVORITES) {
            FavoritesScreen(
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
            VisitedScreen(
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
