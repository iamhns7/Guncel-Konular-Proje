package com.tayyipgunay.harputarguide.core.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.domain.model.Place

@Composable
fun PlaceListCard(
    place: Place,
    onClick: () -> Unit,
    cardColor: Color,
    titleColor: Color,
    descriptionColor: Color,
    visitedGreen: Color,
    imagePlaceholderColors: List<Color>,
    showFavoriteStar: Boolean = false,
    favoriteColor: Color = Color(0xFFC9A227),
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(108.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HarputAssetImage(
                imageAssetPath = place.imageAssetPath,
                contentDescription = place.name,
                modifier = Modifier
                    .width(88.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                placeholderGradientColors = imagePlaceholderColors
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = place.name,
                    color = titleColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1
                )
                Text(
                    text = place.description,
                    color = descriptionColor,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    maxLines = 2
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = descriptionColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = place.distance,
                        color = descriptionColor,
                        fontSize = 12.sp
                    )
                }
            }

            if (showFavoriteStar) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(favoriteColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Favori",
                        tint = favoriteColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                VisitedIndicator(
                    isVisited = place.isVisited,
                    visitedGreen = visitedGreen,
                    outlineColor = titleColor.copy(alpha = 0.35f)
                )
            }
        }
    }
}

@Composable
private fun VisitedIndicator(
    isVisited: Boolean,
    visitedGreen: Color,
    outlineColor: Color
) {
    if (isVisited) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(visitedGreen),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Gezildi",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    } else {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .border(width = 2.dp, color = outlineColor, shape = CircleShape)
        )
    }
}
