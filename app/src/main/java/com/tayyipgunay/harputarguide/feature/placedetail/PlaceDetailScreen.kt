package com.tayyipgunay.harputarguide.feature.placedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import com.tayyipgunay.harputarguide.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.core.design.component.HarputAssetImage
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBar
import com.tayyipgunay.harputarguide.core.design.component.defaultHarputPlaceholderGradient
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBarStyle
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomTab
import com.tayyipgunay.harputarguide.core.design.component.PlaceDetailInfoGrid
import com.tayyipgunay.harputarguide.core.common.openGoogleMapsNavigation
import com.tayyipgunay.harputarguide.core.design.theme.HarputColors
import com.tayyipgunay.harputarguide.data.map.HarputMapPoints
import com.tayyipgunay.harputarguide.domain.model.PlaceDetail
import kotlinx.coroutines.launch

private val HeroHeight = 240.dp
private val PanelOverlap = 24.dp

@Composable
fun PlaceDetailScreen(
    placeId: String,
    uiState: PlaceDetailUiState,
    onRetry: () -> Unit,
    onToggleVisited: () -> Unit,
    onToggleFavorite: () -> Unit,
    onMessageConsumed: () -> Unit,
    onBackClick: () -> Unit,
    onArClick: (String) -> Unit,
    onHomeClick: () -> Unit,
    onPlacesClick: () -> Unit,
    onVisitedClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val cream = HarputColors.Cream
    val panelColor = HarputColors.CardCream
    val darkBrown = HarputColors.DarkBrown
    val softBrown = HarputColors.SoftBrown
    val bottomBarBg = HarputColors.BottomBarBg
    val dividerColor = Color(0xFFB89977).copy(alpha = 0.35f)
    val checkGreen = HarputColors.VisitedGreen
    val errorMessage = when (uiState.error) {
        PlaceDetailError.LOAD_FAILED -> stringResource(R.string.place_detail_error_load)
        PlaceDetailError.NOT_FOUND -> stringResource(R.string.place_detail_error_missing)
        null -> null
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val locationNotFoundMsg = stringResource(R.string.navigation_location_not_found)
    val visitedAddedMsg = stringResource(R.string.place_detail_visited_added)
    val visitedRemovedMsg = stringResource(R.string.place_detail_visited_removed)
    val favoriteAddedMsg = stringResource(R.string.place_detail_favorite_added)
    val favoriteRemovedMsg = stringResource(R.string.place_detail_favorite_removed)
    val actionFailedMsg = stringResource(R.string.place_detail_action_failed)

    LaunchedEffect(uiState.message) {
        val message = uiState.message ?: return@LaunchedEffect
        val text = when (message) {
            PlaceDetailMessage.VISITED_ADDED -> visitedAddedMsg
            PlaceDetailMessage.VISITED_REMOVED -> visitedRemovedMsg
            PlaceDetailMessage.FAVORITE_ADDED -> favoriteAddedMsg
            PlaceDetailMessage.FAVORITE_REMOVED -> favoriteRemovedMsg
            PlaceDetailMessage.ACTION_FAILED -> actionFailedMsg
        }
        snackbarHostState.showSnackbar(text)
        onMessageConsumed()
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
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = darkBrown)
                }
            }

            errorMessage != null -> {
                PlaceDetailMessageContent(
                    message = errorMessage,
                    darkBrown = darkBrown,
                    softBrown = softBrown,
                    onBackClick = onBackClick,
                    onRetry = onRetry,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            uiState.placeDetail != null -> {
                val detail = uiState.placeDetail
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    DetailHeroSection(
                        detail = detail,
                        isFavorite = uiState.isFavorite,
                        panelColor = panelColor,
                        onBackClick = onBackClick,
                        onFavoriteClick = onToggleFavorite
                    )

                    DetailContentPanel(
                        detail = detail,
                        isVisited = uiState.isVisited,
                        isArAvailable = uiState.isArAvailable,
                        panelColor = panelColor,
                        darkBrown = darkBrown,
                        softBrown = softBrown,
                        dividerColor = dividerColor,
                        checkGreen = checkGreen,
                        onNavigateClick = {
                            val mapPoint = HarputMapPoints.findByPlaceId(placeId)
                            if (mapPoint != null) {
                                openGoogleMapsNavigation(
                                    context = context,
                                    latitude = mapPoint.latitude,
                                    longitude = mapPoint.longitude,
                                    mode = "w"
                                )
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar(locationNotFoundMsg)
                                }
                            }
                        },
                        onArClick = { onArClick(placeId) },
                        onToggleVisited = onToggleVisited,
                        modifier = Modifier.offset(y = -PanelOverlap)
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun PlaceDetailMessageContent(
    message: String,
    darkBrown: Color,
    softBrown: Color,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            horizontalArrangement = Arrangement.Start
        ) {
            CircleIconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.cd_back),
                    tint = darkBrown,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = message,
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
                contentColor = Color(0xFFF5EBDD)
            )
        ) {
            Text(text = stringResource(R.string.action_retry))
        }
    }
}

@Composable
private fun DetailHeroSection(
    detail: PlaceDetail,
    isFavorite: Boolean,
    panelColor: Color,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(HeroHeight)
    ) {
        HarputAssetImage(
            imageAssetPath = detail.imageAssetPath,
            contentDescription = detail.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholderGradientColors = defaultHarputPlaceholderGradient()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to Color.Black.copy(alpha = 0.2f),
                            0.45f to Color.Transparent,
                            0.85f to panelColor.copy(alpha = 0.55f),
                            1f to panelColor
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleIconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.cd_back),
                    tint = Color(0xFF4B2E1F),
                    modifier = Modifier.size(22.dp)
                )
            }
            CircleIconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = stringResource(R.string.cd_favorite),
                    tint = Color(0xFF4B2E1F),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun CircleIconButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.95f))
    ) {
        content()
    }
}

@Composable
private fun DetailContentPanel(
    detail: PlaceDetail,
    isVisited: Boolean,
    isArAvailable: Boolean,
    panelColor: Color,
    darkBrown: Color,
    softBrown: Color,
    dividerColor: Color,
    checkGreen: Color,
    onNavigateClick: () -> Unit,
    onArClick: () -> Unit,
    onToggleVisited: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = panelColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = detail.name,
                    color = darkBrown,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    lineHeight = 32.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = softBrown,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = detail.location,
                        color = softBrown,
                        fontSize = 13.sp
                    )
                }
            }

            Text(
                text = detail.description,
                color = softBrown,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            HorizontalDivider(color = dividerColor)

            PlaceDetailInfoGrid(
                periodLabel = stringResource(R.string.place_detail_period),
                periodValue = detail.period,
                periodIcon = Icons.Filled.AccountBalance,
                buildLabel = stringResource(R.string.place_detail_estimated_build),
                buildValue = detail.estimatedBuild,
                buildIcon = Icons.Filled.CalendarMonth,
                categoryLabel = stringResource(R.string.place_detail_category),
                categoryValue = detail.category,
                categoryIcon = Icons.Filled.Info,
                borderColor = dividerColor,
                labelColor = softBrown,
                valueColor = darkBrown,
                iconColor = darkBrown
            )

            if (detail.highlights.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.place_detail_highlights),
                        color = darkBrown,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    detail.highlights.forEach { highlight ->
                        HighlightRow(
                            text = highlight,
                            checkGreen = checkGreen,
                            textColor = softBrown
                        )
                    }
                }

                HorizontalDivider(color = dividerColor)
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (isVisited) {
                    Button(
                        onClick = onToggleVisited,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = checkGreen,
                            contentColor = Color.White
                        )
                    ) {
                        ActionButtonContent(
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            label = stringResource(R.string.place_detail_visited),
                            labelColor = Color.White
                        )
                    }
                } else {
                    OutlinedButton(
                        onClick = onToggleVisited,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = checkGreen)
                    ) {
                        ActionButtonContent(
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = checkGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            label = stringResource(R.string.place_detail_mark_visited),
                            labelColor = checkGreen
                        )
                    }
                }

                OutlinedButton(
                    onClick = onNavigateClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = darkBrown)
                ) {
                    ActionButtonContent(
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Navigation,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = stringResource(R.string.place_detail_navigate)
                    )
                }

                if (isArAvailable) {
                    Button(
                        onClick = onArClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = darkBrown)
                    ) {
                        ActionButtonContent(
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.ViewInAr,
                                    contentDescription = null,
                                    tint = Color(0xFFF5EBDD),
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            label = stringResource(R.string.place_detail_ar),
                            labelColor = Color(0xFFF5EBDD)
                        )
                    }
                } else {
                    Text(
                        text = stringResource(R.string.place_detail_ar_unavailable),
                        color = softBrown,
                        fontSize = 13.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionButtonContent(
    icon: @Composable () -> Unit,
    label: String,
    labelColor: Color? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = label,
            color = labelColor ?: Color.Unspecified,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun HighlightRow(
    text: String,
    checkGreen: Color,
    textColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(checkGreen),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
        }
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp
        )
    }
}
