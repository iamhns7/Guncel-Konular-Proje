package com.tayyipgunay.harputarguide.feature.favorites

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.core.design.component.CollectionPlacesTopBar
import com.tayyipgunay.harputarguide.core.design.component.FavoritePlaceListItem
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBar
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBarStyle
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomTab
import com.tayyipgunay.harputarguide.core.design.component.PlaceListEmptyState
import com.tayyipgunay.harputarguide.data.asset.SampleUserPlaces
import com.tayyipgunay.harputarguide.domain.model.Place
import kotlinx.coroutines.launch

private val imageGradients = listOf(
    listOf(Color(0xFFDCC7A8), Color(0xFFBEA07A)),
    listOf(Color(0xFFC9B59A), Color(0xFF9A7B5C)),
    listOf(Color(0xFFE0D0B8), Color(0xFFB8956E)),
    listOf(Color(0xFFD4C4AA), Color(0xFFA6845F))
)

@Composable
fun FavoritesScreen(
    onBackClick: () -> Unit,
    onPlaceClick: (String) -> Unit,
    onHomeClick: () -> Unit,
    onPlacesClick: () -> Unit,
    onVisitedClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    var favorites by remember { mutableStateOf(SampleUserPlaces.getFavorites()) }
    var placePendingDelete by remember { mutableStateOf<Place?>(null) }

    val cream = Color(0xFFF5EBDD)
    val cardColor = Color(0xFFF8F1E6)
    val darkBrown = Color(0xFF4B2E1F)
    val softBrown = Color(0xFF72533C)
    val bottomBarBg = Color(0xFFF0E4D4)
    val visitedGreen = Color(0xFF5A8F4A)
    val favoriteGold = Color(0xFFB8860B)
    val deleteRed = Color(0xFF9B3D3D)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    placePendingDelete?.let { place ->
        RemoveFavoriteDialog(
            placeName = place.name,
            onDismiss = { placePendingDelete = null },
            onConfirmDelete = {
                favorites = favorites.filter { it.id != place.id }
                placePendingDelete = null
                scope.launch {
                    snackbarHostState.showSnackbar("${place.name} favorilerden kaldırıldı.")
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
                title = "Favorilerim",
                subtitle = "Kaydettiğin tarihi noktaları buradan görüntüleyebilirsin.",
                onBackClick = onBackClick,
                onInfoClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "Favoriler şimdilik bu oturumda düzenlenebilir. Kalıcı kayıt sonraki sürümde eklenecek."
                        )
                    }
                },
                darkBrown = darkBrown,
                softBrown = softBrown
            )

            if (favorites.isEmpty()) {
                PlaceListEmptyState(
                    icon = Icons.Filled.Star,
                    title = "Henüz favori nokta yok",
                    description = "Beğendiğin noktaları favorilere eklediğinde burada listelenecek.",
                    iconTint = favoriteGold,
                    titleColor = darkBrown,
                    descriptionColor = softBrown
                )
            } else {
                Text(
                    text = "${favorites.size} nokta",
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
                        items = favorites,
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
                text = "Favorilerden sil",
                color = darkBrown,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Text(
                text = "\"$placeName\" favorilerden kaldırılsın mı?\n\nSilmek istediğinize emin misiniz?",
                color = softBrown,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Vazgeç",
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
                    text = "Evet, Sil",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    )
}
