package com.tayyipgunay.harputarguide.core.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.tayyipgunay.harputarguide.R
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class HarputBottomTab {
    Home,
    Places,
    Ar,
    Visited,
    Favorites,
    About
}

enum class HarputBottomBarStyle {
    Main,
    Explore
}

@Composable
fun HarputBottomBar(
    selectedTab: HarputBottomTab?,
    style: HarputBottomBarStyle,
    onHomeClick: () -> Unit,
    onPlacesClick: () -> Unit,
    onArClick: () -> Unit,
    onVisitedClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onAboutClick: () -> Unit,
    backgroundColor: Color,
    activeColor: Color,
    inactiveColor: Color,
    activeLabelColor: Color,
    inactiveLabelColor: Color
) {
    val items = when (style) {
        HarputBottomBarStyle.Main -> listOf(
            BottomBarItem(HarputBottomTab.Home, Icons.Filled.Home, stringResource(R.string.tab_home), onHomeClick),
            BottomBarItem(HarputBottomTab.Places, Icons.Filled.Place, stringResource(R.string.tab_places), onPlacesClick),
            BottomBarItem(HarputBottomTab.Ar, Icons.Filled.CameraAlt, stringResource(R.string.tab_ar), onArClick),
            BottomBarItem(HarputBottomTab.Favorites, Icons.Filled.Star, stringResource(R.string.tab_favorites), onFavoritesClick),
            BottomBarItem(HarputBottomTab.About, Icons.Filled.Info, stringResource(R.string.tab_about), onAboutClick)
        )
        HarputBottomBarStyle.Explore -> listOf(
            BottomBarItem(HarputBottomTab.Home, Icons.Filled.Home, stringResource(R.string.tab_home), onHomeClick),
            BottomBarItem(HarputBottomTab.Places, Icons.Filled.Place, stringResource(R.string.tab_places), onPlacesClick),
            BottomBarItem(HarputBottomTab.Visited, Icons.Filled.CheckCircle, stringResource(R.string.tab_visited), onVisitedClick),
            BottomBarItem(HarputBottomTab.Favorites, Icons.Filled.Star, stringResource(R.string.tab_favorites), onFavoritesClick),
            BottomBarItem(HarputBottomTab.About, Icons.Filled.Info, stringResource(R.string.tab_about), onAboutClick)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            BottomBarItemView(
                item = item,
                selected = item.tab == selectedTab,
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                activeLabelColor = activeLabelColor,
                inactiveLabelColor = inactiveLabelColor,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private data class BottomBarItem(
    val tab: HarputBottomTab,
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit
)

@Composable
private fun BottomBarItemView(
    item: BottomBarItem,
    selected: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    activeLabelColor: Color,
    inactiveLabelColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = item.onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(width = 52.dp, height = 44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(activeColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = activeLabelColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text = item.label,
                color = activeLabelColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        } else {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = inactiveColor,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = item.label,
                color = inactiveLabelColor,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
