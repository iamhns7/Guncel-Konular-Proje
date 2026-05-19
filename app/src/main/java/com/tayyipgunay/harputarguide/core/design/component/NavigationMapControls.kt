package com.tayyipgunay.harputarguide.core.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NavigationMapControls(
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MapControlButton(iconColor = iconColor) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Filled.Explore,
                    contentDescription = "Kuzey",
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(text = "Kuzey", color = iconColor, fontSize = 9.sp)
            }
        }
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.95f)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MapControlButton(iconColor = iconColor, showBackground = false) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Yakınlaştır",
                    tint = iconColor
                )
            }
            MapControlButton(iconColor = iconColor, showBackground = false) {
                Icon(
                    imageVector = Icons.Filled.Remove,
                    contentDescription = "Uzaklaştır",
                    tint = iconColor
                )
            }
        }
        MapControlButton(iconColor = iconColor) {
            Icon(
                imageVector = Icons.Filled.MyLocation,
                contentDescription = "Konumuma dön",
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun MapControlButton(
    iconColor: Color,
    showBackground: Boolean = true,
    content: @Composable () -> Unit
) {
    val modifier = if (showBackground) {
        Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.95f))
    } else {
        Modifier.size(40.dp)
    }
    IconButton(
        onClick = { },
        modifier = modifier
    ) {
        content()
    }
}
