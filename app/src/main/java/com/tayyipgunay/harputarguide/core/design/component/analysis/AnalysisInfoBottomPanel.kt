package com.tayyipgunay.harputarguide.core.design.component.analysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.R
import com.tayyipgunay.harputarguide.domain.model.HotspotContentSections
import com.tayyipgunay.harputarguide.domain.model.HotspotFeature

@Composable
fun AnalysisInfoBottomPanel(
    panelTitleResId: Int,
    shortDescription: String,
    sections: HotspotContentSections,
    features: List<HotspotFeature>,
    footerHint: String? = null,
    historyBody: String? = null,
    modifier: Modifier = Modifier
) {
    val panelShape = RoundedCornerShape(
        topStart = AnalysisDetailTheme.PanelCornerRadius,
        topEnd = AnalysisDetailTheme.PanelCornerRadius
    )
    val useStructured = sections.hasStructuredContent() || shortDescription.isNotBlank()

    Column(
        modifier = modifier
            .fillMaxHeight()
            .shadow(
                elevation = 8.dp,
                shape = panelShape,
                clip = false
            )
            .clip(panelShape)
            .background(AnalysisDetailTheme.CreamPanel)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(AnalysisDetailTheme.Accent.copy(alpha = 0.35f))
            )
        }

        Text(
            text = stringResource(panelTitleResId),
            color = AnalysisDetailTheme.Accent,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.6.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp)
        ) {
            if (useStructured) {
                AnalysisStructuredContent(
                    shortDescription = shortDescription,
                    sections = sections
                )
            }

            if (features.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.hotspot_highlights_title),
                    color = AnalysisDetailTheme.TitleColor,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                AnalysisFeatureGrid(features = features)
            } else if (!useStructured) {
                Spacer(modifier = Modifier.height(16.dp))
                AnalysisFeatureCard(
                    feature = HotspotFeature(
                        label = stringResource(R.string.model_analysis_no_features_label),
                        value = stringResource(R.string.model_analysis_no_features_value)
                    )
                )
            }

            if (!useStructured) {
                footerHint?.takeIf { it.isNotBlank() }?.let { hint ->
                    Spacer(modifier = Modifier.height(20.dp))
                    AnalysisDetailSectionBlockLegacy(
                        title = stringResource(R.string.hotspot_description_title),
                        body = hint
                    )
                }
            }

            historyBody?.let { body ->
                Spacer(modifier = Modifier.height(16.dp))
                AnalysisHistorySection(body = body)
            }
        }
    }
}

@Composable
private fun AnalysisDetailSectionBlockLegacy(
    title: String,
    body: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = AnalysisDetailTheme.TitleColor,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = body,
            color = AnalysisDetailTheme.BodyColor,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(AnalysisDetailTheme.FeatureCardBg)
                .padding(horizontal = 14.dp, vertical = 12.dp)
        )
    }
}
