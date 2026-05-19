package com.tayyipgunay.harputarguide.feature.constructionprocess

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.core.design.component.ConstructionStepRow
import com.tayyipgunay.harputarguide.data.asset.SampleStructureHotspots

@Composable
fun ConstructionProcessScreen(
    placeId: String,
    hotspotId: String,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    val detail = remember(placeId, hotspotId) {
        SampleStructureHotspots.getDetail(placeId, hotspotId)
    }

    val cream = Color(0xFFFDFBF7)
    val darkBrown = Color(0xFF2D241E)
    val softBrown = Color(0xFF72533C)
    val accentColor = Color(0xFF4B2E1F)
    val lineColor = softBrown.copy(alpha = 0.25f)

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
                    text = "Nasıl Yapılmış?",
                    color = darkBrown,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onCloseClick,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFE8DFD0))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Kapat",
                        tint = darkBrown
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
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = detail.name,
                color = softBrown,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            detail.constructionSteps.forEachIndexed { index, step ->
                ConstructionStepRow(
                    step = step,
                    isLast = index == detail.constructionSteps.lastIndex,
                    accentColor = accentColor,
                    textColor = darkBrown,
                    mutedColor = softBrown,
                    lineColor = lineColor
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
