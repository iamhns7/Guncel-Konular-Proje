package com.tayyipgunay.harputarguide.feature.tournavigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBar
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBarStyle
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomTab
import com.tayyipgunay.harputarguide.core.design.component.NavigationDestinationCard
import com.tayyipgunay.harputarguide.core.design.component.NavigationInstructionCard
import com.tayyipgunay.harputarguide.core.design.component.NavigationMapControls
import com.tayyipgunay.harputarguide.core.design.component.NavigationMapPlaceholder
import com.tayyipgunay.harputarguide.data.asset.SampleNavigation
import kotlinx.coroutines.launch

@Composable
fun TourNavigationScreen(
    placeId: String,
    onCancelClick: () -> Unit,
    onArClick: (String) -> Unit,
    onHomeClick: () -> Unit,
    onPlacesClick: () -> Unit,
    onVisitedClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val destinationName = remember(placeId) { SampleNavigation.destinationName(placeId) }
    val distance = remember(placeId) { SampleNavigation.distance(placeId) }

    val cream = Color(0xFFF5EBDD)
    val darkBrown = Color(0xFF4B2E1F)
    val softBrown = Color(0xFF72533C)
    val bottomBarBg = Color(0xFFF0E4D4)
    val cardColor = Color(0xFFFAF6F0)
    val arButtonColor = Color(0xFFE8D9C4)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = cream,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            HarputBottomBar(
                selectedTab = HarputBottomTab.Places,
                style = HarputBottomBarStyle.Explore,
                onHomeClick = onHomeClick,
                onPlacesClick = onPlacesClick,
                onArClick = { },
                onVisitedClick = onVisitedClick,
                onFavoritesClick = onFavoritesClick,
                onAboutClick = onAboutClick,
                backgroundColor = bottomBarBg,
                activeColor = darkBrown,
                inactiveColor = darkBrown.copy(alpha = 0.75f),
                activeLabelColor = cream,
                inactiveLabelColor = darkBrown
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavigationMapPlaceholder(
                destinationLabel = destinationName,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                NavigationInstructionCard(
                    distanceText = SampleNavigation.instructionDistance,
                    actionText = SampleNavigation.instructionAction,
                    containerColor = darkBrown,
                    contentColor = cream,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                )

                Box(modifier = Modifier.weight(1f)) {
                    NavigationMapControls(
                        iconColor = darkBrown,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 12.dp)
                    )
                }

                NavigationDestinationCard(
                    destinationName = destinationName,
                    distance = distance,
                    duration = SampleNavigation.duration,
                    approachMessage = SampleNavigation.approachMessage,
                    progress = SampleNavigation.routeProgress,
                    onCancelClick = onCancelClick,
                    onArClick = { onArClick(placeId) },
                    onSoundClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Sesli yönlendirme yakında eklenecek.")
                        }
                    },
                    cardColor = cardColor,
                    titleColor = darkBrown,
                    mutedColor = softBrown,
                    progressColor = darkBrown,
                    arButtonColor = arButtonColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
        }
    }
}
