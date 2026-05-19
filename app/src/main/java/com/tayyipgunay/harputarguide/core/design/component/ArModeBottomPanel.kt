package com.tayyipgunay.harputarguide.core.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ArMode {
    Info,
    Past,
    Structure,
    Inscription,
    Audio
}

@Composable
fun ArModeBottomPanel(
    selectedMode: ArMode,
    onModeSelected: (ArMode) -> Unit,
    onPlacesClick: () -> Unit,
    panelColor: Color,
    activeColor: Color,
    inactiveColor: Color,
    labelColor: Color,
    footerColor: Color,
    showVisitFooter: Boolean = true,
    modifier: Modifier = Modifier
) {
    val modes = listOf(
        ArModeItem(ArMode.Info, "Bilgi", Icons.Filled.Info),
        ArModeItem(ArMode.Past, "Geçmişi Gör", Icons.Filled.AccountBalance),
        ArModeItem(ArMode.Structure, "Yapı Bilgisi", Icons.Filled.Apartment),
        ArModeItem(ArMode.Inscription, "Kitabe Oku", Icons.Filled.MenuBook),
        ArModeItem(ArMode.Audio, "Dinle", Icons.Filled.VolumeUp)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(panelColor)
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            modes.forEach { mode ->
                ArModeButton(
                    item = mode,
                    selected = mode.mode == selectedMode,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor,
                    labelColor = labelColor,
                    onClick = { onModeSelected(mode.mode) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (showVisitFooter) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(footerColor)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF5A8F4A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = "Bu noktayı ziyaret ettiniz",
                        color = labelColor,
                        fontSize = 12.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .height(28.dp)
                        .width(1.dp)
                        .background(labelColor.copy(alpha = 0.25f))
                )

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onPlacesClick)
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.FormatListBulleted,
                        contentDescription = null,
                        tint = labelColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "  Noktalar Listesine Dön",
                        color = labelColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        }
    }
}

private data class ArModeItem(
    val mode: ArMode,
    val label: String,
    val icon: ImageVector
)

@Composable
private fun ArModeButton(
    item: ArModeItem,
    selected: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    labelColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(if (selected) activeColor else inactiveColor)
                .then(
                    if (selected) Modifier else Modifier.border(
                        width = 1.dp,
                        color = labelColor.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = if (selected) Color(0xFFF5EBDD) else labelColor,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = item.label,
            color = labelColor,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = 12.sp
        )
    }
}
