package com.tayyipgunay.harputarguide.feature.hotspotdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.R
import com.tayyipgunay.harputarguide.core.design.component.HotspotPropertyRow
import com.tayyipgunay.harputarguide.data.asset.HotspotProperty
import com.tayyipgunay.harputarguide.data.asset.SampleStructureHotspots

@Composable
fun HotspotDetailScreen(
    placeId: String,
    hotspotId: String,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit,
    onConstructionProcessClick: (String, String) -> Unit,
    onMaterialAnalysisClick: (String, String) -> Unit,
    onModelViewerClick: (String) -> Unit
) {
    val detail = remember(placeId, hotspotId) {
        SampleStructureHotspots.getDetail(placeId, hotspotId)
    }

    val cream = Color(0xFFFDFBF7)
    val darkBrown = Color(0xFF2D241E)
    val softBrown = Color(0xFF72533C)
    val dividerColor = softBrown.copy(alpha = 0.2f)

    Scaffold(
        containerColor = cream,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Geri",
                        tint = darkBrown
                    )
                }
                Text(
                    text = detail.name,
                    color = darkBrown,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onCloseClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8DFD0))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Kapat",
                        tint = darkBrown,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cream)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { onConstructionProcessClick(placeId, hotspotId) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = darkBrown)
                ) {
                    Text("Yapım Süreci", fontWeight = FontWeight.SemiBold)
                }
                OutlinedButton(
                    onClick = { onMaterialAnalysisClick(placeId, hotspotId) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = darkBrown)
                ) {
                    Text("Malzeme Analizi", fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = { onModelViewerClick(placeId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4B2E1F),
                        contentColor = cream
                    )
                ) {
                    Text("3D Modeli Gör", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.harput_welcome_png),
                contentDescription = detail.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Açıklama",
                color = darkBrown,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = detail.longDescription,
                color = softBrown,
                fontSize = 15.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Özellikler",
                color = darkBrown,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(4.dp))

            detail.properties.forEachIndexed { index, property ->
                HotspotPropertyRow(
                    icon = propertyIcon(property, index),
                    label = property.label,
                    value = property.value,
                    textColor = darkBrown,
                    dividerColor = dividerColor,
                    showDivider = index > 0
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun propertyIcon(property: HotspotProperty, index: Int): ImageVector {
    return when (property.label) {
        "Taş Türü" -> Icons.Filled.Diamond
        "Dayanıklılık" -> Icons.Filled.Shield
        "Dönem" -> Icons.Filled.CalendarMonth
        "Renk" -> Icons.Filled.ColorLens
        else -> when (index % 4) {
            0 -> Icons.Filled.Diamond
            1 -> Icons.Filled.Shield
            2 -> Icons.Filled.CalendarMonth
            else -> Icons.Filled.ColorLens
        }
    }
}
