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
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.R
import com.tayyipgunay.harputarguide.core.design.theme.HarputColors
import com.tayyipgunay.harputarguide.core.design.component.ConstructionStepRow
import com.tayyipgunay.harputarguide.feature.hotspotdetail.HotspotDetailViewModel

@Composable
fun ConstructionProcessScreen(
    placeId: String,
    hotspotId: String,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit,
    viewModel: HotspotDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (uiState.isLoading || uiState.detail == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    val detail = uiState.detail!!

    val cream = HarputColors.CardLight
    val darkBrown = HarputColors.DarkBrownText
    val softBrown = HarputColors.SoftBrown
    val accentColor = HarputColors.DarkBrown
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
                        contentDescription = stringResource(R.string.cd_back),
                        tint = darkBrown
                    )
                }
                Text(
                    text = stringResource(R.string.construction_title),
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
                        .background(HarputColors.IconBg)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.cd_close),
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
