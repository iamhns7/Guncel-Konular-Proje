package com.tayyipgunay.harputarguide.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.R
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBar
import com.tayyipgunay.harputarguide.core.design.theme.HarputColors
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBarStyle
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomTab
import com.tayyipgunay.harputarguide.core.design.component.HomeMenuCard

private data class HomeMenuItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun HomeScreen(
    onStartTourClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onVisitedClick: () -> Unit,
    onFaqClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val cream = HarputColors.Cream
    val cardColor = HarputColors.CardCream
    val darkBrown = HarputColors.DarkBrown
    val softBrown = HarputColors.SoftBrown
    val bottomBarBg = HarputColors.BottomBarBg
    val iconTint = HarputColors.Cream

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val menuItems = listOf(
        HomeMenuItem(
            title = stringResource(R.string.home_menu_start_tour_title),
            description = stringResource(R.string.home_menu_start_tour_desc),
            icon = Icons.Filled.LocationOn,
            onClick = onStartTourClick
        ),
        HomeMenuItem(
            title = stringResource(R.string.home_menu_favorites_title),
            description = stringResource(R.string.home_menu_favorites_desc),
            icon = Icons.Filled.Star,
            onClick = onFavoritesClick
        ),
        HomeMenuItem(
            title = stringResource(R.string.home_menu_visited_title),
            description = stringResource(R.string.home_menu_visited_desc),
            icon = Icons.Filled.CheckCircle,
            onClick = onVisitedClick
        ),
        HomeMenuItem(
            title = stringResource(R.string.home_menu_faq_title),
            description = stringResource(R.string.home_menu_faq_desc),
            icon = Icons.Filled.QuestionAnswer,
            onClick = onFaqClick
        ),
        HomeMenuItem(
            title = stringResource(R.string.home_menu_about_title),
            description = stringResource(R.string.home_menu_about_desc),
            icon = Icons.Filled.Info,
            onClick = onAboutClick
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cream)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            HomeHeroSection(
                cream = cream,
                darkBrown = darkBrown,
                softBrown = softBrown
            )

            HomeMenuGrid(
                items = menuItems,
                cardColor = cardColor,
                titleColor = darkBrown,
                descriptionColor = softBrown,
                iconContainerColor = darkBrown,
                iconTint = iconTint,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        HarputBottomBar(
            selectedTab = HarputBottomTab.Home,
            style = HarputBottomBarStyle.Main,
            onHomeClick = { scope.launch { scrollState.animateScrollTo(0) } },
            onPlacesClick = onStartTourClick,
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
}

@Composable
private fun HomeHeroSection(
    cream: Color,
    darkBrown: Color,
    softBrown: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(cream, Color(0xFFF3E8D8))
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .border(width = 1.dp, color = softBrown.copy(alpha = 0.4f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "H",
                    color = darkBrown,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 23.sp
                )
            }
            Text(
                text = stringResource(R.string.app_brand),
                color = darkBrown,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp
            )
            Text(
                text = stringResource(R.string.app_tagline),
                color = softBrown,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.harput_welcome_png),
                contentDescription = stringResource(R.string.cd_castle_image),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                cream.copy(alpha = 0.2f),
                                cream
                            ),
                            startY = 80f
                        )
                    )
            )
        }
    }
}

@Composable
private fun HomeMenuGrid(
    items: List<HomeMenuItem>,
    cardColor: Color,
    titleColor: Color,
    descriptionColor: Color,
    iconContainerColor: Color,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items.chunked(3).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowItems.forEach { item ->
                    HomeMenuCard(
                        title = item.title,
                        description = item.description,
                        icon = item.icon,
                        onClick = item.onClick,
                        cardColor = cardColor,
                        titleColor = titleColor,
                        descriptionColor = descriptionColor,
                        iconContainerColor = iconContainerColor,
                        iconTint = iconTint,
                        modifier = Modifier.weight(1f)
                    )
                }
                repeat(3 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
