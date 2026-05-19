package com.tayyipgunay.harputarguide.core.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlaceDetailInfoGrid(
    periodLabel: String,
    periodValue: String,
    periodIcon: ImageVector,
    buildLabel: String,
    buildValue: String,
    buildIcon: ImageVector,
    categoryLabel: String,
    categoryValue: String,
    categoryIcon: ImageVector,
    borderColor: Color,
    labelColor: Color,
    valueColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        InfoColumn(
            label = periodLabel,
            value = periodValue,
            icon = periodIcon,
            labelColor = labelColor,
            valueColor = valueColor,
            iconColor = iconColor,
            modifier = Modifier.weight(1f)
        )
        VerticalDivider(
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .width(1.dp),
            color = borderColor
        )
        InfoColumn(
            label = buildLabel,
            value = buildValue,
            icon = buildIcon,
            labelColor = labelColor,
            valueColor = valueColor,
            iconColor = iconColor,
            modifier = Modifier.weight(1f)
        )
        VerticalDivider(
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .width(1.dp),
            color = borderColor
        )
        InfoColumn(
            label = categoryLabel,
            value = categoryValue,
            icon = categoryIcon,
            labelColor = labelColor,
            valueColor = valueColor,
            iconColor = iconColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun InfoColumn(
    label: String,
    value: String,
    icon: ImageVector,
    labelColor: Color,
    valueColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 6.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = label,
            color = labelColor,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Text(
            text = value,
            color = valueColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = 14.sp
        )
    }
}
