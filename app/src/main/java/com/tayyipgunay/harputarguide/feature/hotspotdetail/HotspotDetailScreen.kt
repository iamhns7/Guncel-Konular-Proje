package com.tayyipgunay.harputarguide.feature.hotspotdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tayyipgunay.harputarguide.R
import com.tayyipgunay.harputarguide.core.design.component.analysis.AnalysisDetailTheme
import com.tayyipgunay.harputarguide.core.design.component.analysis.AnalysisDetailTopBar
import com.tayyipgunay.harputarguide.core.design.component.analysis.AnalysisDiagramSection
import com.tayyipgunay.harputarguide.core.design.component.analysis.AnalysisInfoBottomPanel
import com.tayyipgunay.harputarguide.core.design.component.analysis.AnalysisInfoPanelTitle
import com.tayyipgunay.harputarguide.domain.model.HotspotFeature
import com.tayyipgunay.harputarguide.feature.modelviewer.ModelCalloutLabel
import com.tayyipgunay.harputarguide.feature.modelviewer.ModelCalloutLabels

@Composable
fun HotspotDetailScreen(
    placeId: String,
    hotspotId: String,
    onBackClick: () -> Unit,
    viewModel: HotspotDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AnalysisDetailTheme.DarkBackground),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
        return
    }

    val content = uiState.content
    if (content == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AnalysisDetailTheme.DarkBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.hotspot_content_missing),
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(24.dp)
            )
        }
        return
    }

    val legacyDetail = uiState.detail
    val title = content.name.ifBlank { legacyDetail?.name.orEmpty() }
    val detailImage = content.media.detailImageAssetPath
    val viewportPreview = uiState.viewportPreviewAssetPath
    val previewImage = viewportPreview ?: detailImage
    val hasPhotoPreview = !previewImage.isNullOrBlank()
    val modelFile = content.media.model3dAssetPath?.substringAfterLast('/').orEmpty()
    val callouts = ModelCalloutLabels.forHotspot(
        placeId = placeId,
        hotspotId = hotspotId,
        modelFileName = modelFile
    ).ifEmpty {
        legacyDetail?.materialLayers?.mapIndexed { index, layer ->
            val anchorY = 0.22f + (index * 0.18f).coerceAtMost(0.56f)
            ModelCalloutLabel(layer.title, anchorY)
        }.orEmpty()
    }
    val rawFeatures = content.features.takeIf { it.isNotEmpty() }
        ?: legacyDetail?.materialRows?.map { HotspotFeature(label = it.label, value = it.value) }
        ?: emptyList()
    val features = rawFeatures.filterNot(::isMetaFeature)
    val panelTitleResId = AnalysisInfoPanelTitle.resolveResId(
        contentType = content.contentType,
        modelFileName = modelFile,
        hotspotId = hotspotId
    )
    val showHistory = content.actions.showHistory && !content.historyIdea.isNullOrBlank()
    val hasStructured = content.sections.hasStructuredContent()
    val legacyDescription = content.detailDescription.ifBlank { content.shortDescription }

    Scaffold(
        containerColor = AnalysisDetailTheme.DarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AnalysisDetailTopBar(
                title = title.ifBlank { stringResource(R.string.model_analysis_title) },
                onBack = onBackClick
            )

            AnalysisDiagramSection(
                previewImageAssetPath = previewImage,
                callouts = callouts.map { it.toAnalysisCallout() },
                showCallouts = !hasPhotoPreview && callouts.isNotEmpty(),
                modifier = Modifier
                    .weight(0.40f)
                    .heightIn(max = 300.dp)
                    .fillMaxWidth()
            )

            AnalysisInfoBottomPanel(
                panelTitleResId = panelTitleResId,
                shortDescription = content.shortDescription,
                sections = content.sections,
                features = features,
                footerHint = legacyDescription.takeIf { !hasStructured && it.isNotBlank() },
                historyBody = content.historyIdea.takeIf { showHistory },
                modifier = Modifier
                    .weight(0.60f)
                    .fillMaxWidth()
            )
        }
    }
}

private fun isMetaFeature(feature: HotspotFeature): Boolean {
    val label = feature.label.lowercase()
    return label == "görsel" ||
        label.contains("geçmişi gör") ||
        label.contains("ses") ||
        label == "model"
}
