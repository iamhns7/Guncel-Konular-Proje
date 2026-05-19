package com.tayyipgunay.harputarguide.core.design.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tayyipgunay.harputarguide.domain.model.Place

@Composable
fun FavoritePlaceListItem(
    place: Place,
    onPlaceClick: () -> Unit,
    onDeleteClick: () -> Unit,
    cardColor: Color,
    titleColor: Color,
    descriptionColor: Color,
    visitedGreen: Color,
    imagePlaceholderColors: List<Color>,
    favoriteColor: Color,
    deleteIconColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PlaceListCard(
            place = place,
            onClick = onPlaceClick,
            cardColor = cardColor,
            titleColor = titleColor,
            descriptionColor = descriptionColor,
            visitedGreen = visitedGreen,
            imagePlaceholderColors = imagePlaceholderColors,
            showFavoriteStar = true,
            favoriteColor = favoriteColor,
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.DeleteOutline,
                contentDescription = "Favorilerden sil",
                tint = deleteIconColor,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}
