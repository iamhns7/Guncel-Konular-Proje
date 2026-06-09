package com.tayyipgunay.harputarguide.feature.visited

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.R
import com.tayyipgunay.harputarguide.core.design.component.CollectionPlacesTopBar
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBar
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBarStyle
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomTab
import com.tayyipgunay.harputarguide.core.design.component.PlaceListCard
import com.tayyipgunay.harputarguide.core.design.component.PlaceListEmptyState
import com.tayyipgunay.harputarguide.core.design.theme.HarputColors
import kotlinx.coroutines.launch

private val imageGradients = HarputColors.PlaceholderGradients

@Composable
fun VisitedScreen(
    uiState: VisitedUiState,
    onRetry: () -> Unit,
    onBackClick: () -> Unit,
    onPlaceClick: (String) -> Unit,
    onHomeClick: () -> Unit,
    onPlacesClick: () -> Unit,
    onVisitedClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val cream = HarputColors.Cream
    val cardColor = HarputColors.CardCream
    val darkBrown = HarputColors.DarkBrown
    val softBrown = HarputColors.SoftBrown
    val bottomBarBg = HarputColors.BottomBarBg
    val visitedGreen = HarputColors.VisitedGreen

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val infoMessage = stringResource(R.string.visited_info)
    val errorMessage = when (uiState.error) {
        VisitedError.LOAD_FAILED -> stringResource(R.string.visited_error_load)
        null -> null
    }

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
                title = stringResource(R.string.visited_title),
                subtitle = stringResource(R.string.visited_subtitle),
                onBackClick = onBackClick,
                onInfoClick = {
                    scope.launch { snackbarHostState.showSnackbar(infoMessage) }
                },
                darkBrown = darkBrown,
                softBrown = softBrown
            )

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = darkBrown)
                    }
                }

                errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = errorMessage,
                            color = softBrown,
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = onRetry,
                            modifier = Modifier.padding(top = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = darkBrown,
                                contentColor = cream
                            )
                        ) {
                            Text(text = stringResource(R.string.action_retry))
                        }
                    }
                }

                uiState.visitedPlaces.isEmpty() -> {
                    PlaceListEmptyState(
                        icon = Icons.Filled.CheckCircle,
                        title = stringResource(R.string.visited_empty_title),
                        description = stringResource(R.string.visited_empty_description),
                        iconTint = visitedGreen,
                        titleColor = darkBrown,
                        descriptionColor = softBrown
                    )
                }

                else -> {
                    Text(
                        text = stringResource(R.string.visited_count, uiState.visitedPlaces.size),
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
                        itemsIndexed(
                            items = uiState.visitedPlaces,
                            key = { _, place -> place.id }
                        ) { index, place ->
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
}
