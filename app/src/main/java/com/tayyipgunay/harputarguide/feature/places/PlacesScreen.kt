package com.tayyipgunay.harputarguide.feature.places

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import com.tayyipgunay.harputarguide.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBar
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBarStyle
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomTab
import com.tayyipgunay.harputarguide.core.design.component.PlaceListCard
import com.tayyipgunay.harputarguide.core.design.component.PlaceListEmptyState
import com.tayyipgunay.harputarguide.core.design.theme.HarputColors
import kotlinx.coroutines.launch

private val imageGradients = HarputColors.PlaceholderGradients

@Composable
fun PlacesScreen(
    uiState: PlacesUiState,
    onRetry: () -> Unit,
    onPlaceClick: (String) -> Unit,
    onHomeClick: () -> Unit,
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
    val filterSoonMessage = stringResource(R.string.places_filter_soon)
    val errorMessage = when (uiState.error) {
        PlacesError.LOAD_FAILED -> stringResource(R.string.places_error_load)
        PlacesError.EMPTY -> stringResource(R.string.places_error_empty)
        null -> null
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = cream,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            HarputBottomBar(
                selectedTab = HarputBottomTab.Places,
                style = HarputBottomBarStyle.Explore,
                onHomeClick = onHomeClick,
                onPlacesClick = { },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PlacesTopBar(
                darkBrown = darkBrown,
                softBrown = softBrown,
                onMenuClick = onHomeClick,
                onFilterClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(filterSoonMessage)
                    }
                }
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
                    PlacesMessageContent(
                        message = errorMessage,
                        darkBrown = darkBrown,
                        softBrown = softBrown,
                        showRetry = true,
                        onRetry = onRetry,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                uiState.places.isEmpty() -> {
                    PlaceListEmptyState(
                        icon = Icons.Filled.Place,
                        title = stringResource(R.string.places_empty_title),
                        description = stringResource(R.string.places_empty_description),
                        iconTint = darkBrown,
                        titleColor = darkBrown,
                        descriptionColor = softBrown,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(uiState.places, key = { _, place -> place.id }) { index, place ->
                            PlaceListCard(
                                place = place,
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

@Composable
private fun PlacesMessageContent(
    message: String,
    darkBrown: Color,
    softBrown: Color,
    showRetry: Boolean,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = softBrown,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            textAlign = TextAlign.Center
        )
        if (showRetry) {
            Button(
                onClick = onRetry,
                modifier = Modifier.padding(top = 16.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = darkBrown,
                    contentColor = HarputColors.Cream
                )
            ) {
                Text(text = stringResource(R.string.action_retry))
            }
        }
    }
}

@Composable
private fun PlacesTopBar(
    darkBrown: Color,
    softBrown: Color,
    onMenuClick: () -> Unit,
    onFilterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFF5EBDD), Color(0xFFF3E8D8))
                )
            )
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.cd_menu),
                    tint = darkBrown
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.places_title),
                    color = darkBrown,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.places_subtitle),
                    color = softBrown,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center
                )
            }

            IconButton(onClick = onFilterClick) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = stringResource(R.string.cd_filter),
                    tint = darkBrown
                )
            }
        }
    }
}
