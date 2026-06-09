package com.tayyipgunay.harputarguide.feature.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.R
import com.tayyipgunay.harputarguide.core.design.component.CollectionPlacesTopBar
import com.tayyipgunay.harputarguide.core.design.component.FavoritePlaceListItem
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBar
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBarStyle
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomTab
import com.tayyipgunay.harputarguide.core.design.component.PlaceListEmptyState
import com.tayyipgunay.harputarguide.core.design.theme.HarputColors
import com.tayyipgunay.harputarguide.domain.model.Place
import kotlinx.coroutines.launch

private val imageGradients = HarputColors.PlaceholderGradients

@Composable
fun FavoritesScreen(
    uiState: FavoritesUiState,
    onRetry: () -> Unit,
    onRemoveFavorite: (String) -> Unit,
    onRemoveFailedConsumed: () -> Unit,
    onBackClick: () -> Unit,
    onPlaceClick: (String) -> Unit,
    onHomeClick: () -> Unit,
    onPlacesClick: () -> Unit,
    onVisitedClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    var placePendingDelete by remember { mutableStateOf<Place?>(null) }

    val cream = HarputColors.Cream
    val cardColor = HarputColors.CardCream
    val darkBrown = HarputColors.DarkBrown
    val softBrown = HarputColors.SoftBrown
    val bottomBarBg = HarputColors.BottomBarBg
    val visitedGreen = HarputColors.VisitedGreen
    val favoriteGold = HarputColors.FavoriteGold
    val deleteRed = HarputColors.DeleteRed

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val removedTemplate = stringResource(R.string.favorites_removed)
    val favoritesInfo = stringResource(R.string.favorites_info)
    val removeFailedMsg = stringResource(R.string.favorites_remove_failed)
    val errorMessage = when (uiState.error) {
        FavoritesError.LOAD_FAILED -> stringResource(R.string.favorites_error_load)
        null -> null
    }

    LaunchedEffect(uiState.removeFailed) {
        if (uiState.removeFailed) {
            snackbarHostState.showSnackbar(removeFailedMsg)
            onRemoveFailedConsumed()
        }
    }

    placePendingDelete?.let { place ->
        RemoveFavoriteDialog(
            placeName = place.name,
            onDismiss = { placePendingDelete = null },
            onConfirmDelete = {
                onRemoveFavorite(place.id)
                placePendingDelete = null
                scope.launch {
                    snackbarHostState.showSnackbar(removedTemplate.format(place.name))
                }
            },
            darkBrown = darkBrown,
            softBrown = softBrown,
            cream = cream,
            deleteRed = deleteRed
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = cream,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            HarputBottomBar(
                selectedTab = HarputBottomTab.Favorites,
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
                title = stringResource(R.string.favorites_title),
                subtitle = stringResource(R.string.favorites_subtitle),
                onBackClick = onBackClick,
                onInfoClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(favoritesInfo)
                    }
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

                uiState.favoritePlaces.isEmpty() -> {
                    PlaceListEmptyState(
                        icon = Icons.Filled.Star,
                        title = stringResource(R.string.favorites_empty_title),
                        description = stringResource(R.string.favorites_empty_description),
                        iconTint = favoriteGold,
                        titleColor = darkBrown,
                        descriptionColor = softBrown
                    )
                }

                else -> {
                    Text(
                        text = stringResource(R.string.favorites_count, uiState.favoritePlaces.size),
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
                            items = uiState.favoritePlaces,
                            key = { _, place -> place.id }
                        ) { index, place ->
                            FavoritePlaceListItem(
                                place = place,
                                onPlaceClick = { onPlaceClick(place.id) },
                                onDeleteClick = { placePendingDelete = place },
                                cardColor = cardColor,
                                titleColor = darkBrown,
                                descriptionColor = softBrown,
                                visitedGreen = visitedGreen,
                                imagePlaceholderColors = imageGradients[index % imageGradients.size],
                                favoriteColor = favoriteGold,
                                deleteIconColor = deleteRed
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RemoveFavoriteDialog(
    placeName: String,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit,
    darkBrown: Color,
    softBrown: Color,
    cream: Color,
    deleteRed: Color
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = cream,
        title = {
            Text(
                text = stringResource(R.string.favorites_remove_title),
                color = darkBrown,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Text(
                text = stringResource(R.string.favorites_remove_message, placeName),
                color = softBrown,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.action_cancel),
                    color = softBrown,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmDelete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = deleteRed,
                    contentColor = cream
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.favorites_remove_confirm),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    )
}
