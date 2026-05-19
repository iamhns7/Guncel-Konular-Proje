package com.tayyipgunay.harputarguide.feature.visited

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.core.design.component.CollectionPlacesTopBar
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBar
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBarStyle
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomTab
import com.tayyipgunay.harputarguide.core.design.component.PlaceListCard
import com.tayyipgunay.harputarguide.core.design.component.PlaceListEmptyState
import com.tayyipgunay.harputarguide.data.asset.SampleUserPlaces
import kotlinx.coroutines.launch

private val imageGradients = listOf(
    listOf(Color(0xFFDCC7A8), Color(0xFFBEA07A)),
    listOf(Color(0xFFC9B59A), Color(0xFF9A7B5C)),
    listOf(Color(0xFFE0D0B8), Color(0xFFB8956E)),
    listOf(Color(0xFFD4C4AA), Color(0xFFA6845F))
)

@Composable
fun VisitedScreen(
    onBackClick: () -> Unit,
    onPlaceClick: (String) -> Unit,
    onHomeClick: () -> Unit,
    onPlacesClick: () -> Unit,
    onVisitedClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val visitedPlaces = remember { SampleUserPlaces.getVisited() }

    val cream = Color(0xFFF5EBDD)
    val cardColor = Color(0xFFF8F1E6)
    val darkBrown = Color(0xFF4B2E1F)
    val softBrown = Color(0xFF72533C)
    val bottomBarBg = Color(0xFFF0E4D4)
    val visitedGreen = Color(0xFF5A8F4A)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = cream,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            HarputBottomBar(
                selectedTab = HarputBottomTab.Visited,
                style = HarputBottomBarStyle.Explore,
                onHomeClick = onHomeClick,
                onPlacesClick = onPlacesClick,
                onArClick = { },
                onVisitedClick = onVisitedClick,
                onFavoritesClick = onFavoritesClick,
                onAboutClick = onAboutClick,
                backgroundColor = bottomBarBg,
                activeColor = darkBrown,
                inactiveColor = softBrown,
                activeLabelColor = cream,
                inactiveLabelColor = softBrown
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CollectionPlacesTopBar(
                title = "Gezdiğim Noktalar",
                subtitle = "Ziyaret ettiğin tarihi noktaların listesi.",
                onBackClick = onBackClick,
                onInfoClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "Gezilen noktalar şimdilik hazır örnek veriden gösterilmektedir."
                        )
                    }
                },
                darkBrown = darkBrown,
                softBrown = softBrown
            )

            if (visitedPlaces.isEmpty()) {
                PlaceListEmptyState(
                    icon = Icons.Filled.CheckCircle,
                    title = "Henüz gezilen nokta yok",
                    description = "Bir noktayı ziyaret ettiğinde burada görünecek.",
                    iconTint = visitedGreen,
                    titleColor = darkBrown,
                    descriptionColor = softBrown
                )
            } else {
                Text(
                    text = "${visitedPlaces.size} nokta gezildi",
                    color = softBrown,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(visitedPlaces) { index, place ->
                        PlaceListCard(
                            place = place.copy(isVisited = true),
                            onClick = { onPlaceClick(place.id) },
                            cardColor = cardColor,
                            titleColor = darkBrown,
                            descriptionColor = softBrown,
                            visitedGreen = visitedGreen,
                            imagePlaceholderColors = imageGradients[index % imageGradients.size]
                        )
                    }
                }
            }
        }
    }
}
