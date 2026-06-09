package com.tayyipgunay.harputarguide.core.design.component.analysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.R
import com.tayyipgunay.harputarguide.domain.model.HotspotContentSections

@Composable
fun AnalysisStructuredContent(
    shortDescription: String,
    sections: HotspotContentSections,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (shortDescription.isNotBlank()) {
            AnalysisDetailSectionBlock(
                title = stringResource(R.string.hotspot_section_short),
                body = shortDescription,
                highlighted = true,
                showDivider = false
            )
        }
        if (sections.material.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            AnalysisDetailSectionBlock(
                title = stringResource(R.string.hotspot_section_material),
                body = sections.material
            )
        }
        if (sections.construction.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            AnalysisDetailSectionBlock(
                title = stringResource(R.string.hotspot_section_construction),
                body = sections.construction
            )
        }
        if (sections.purpose.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            AnalysisDetailSectionBlock(
                title = stringResource(R.string.hotspot_section_purpose),
                body = sections.purpose
            )
        }
        if (sections.significance.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            AnalysisDetailSectionBlock(
                title = stringResource(R.string.hotspot_section_significance),
                body = sections.significance,
                showDivider = false
            )
        }
    }
}

@Composable
private fun AnalysisDetailSectionBlock(
    title: String,
    body: String,
    highlighted: Boolean = false,
    showDivider: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = AnalysisDetailTheme.TitleColor,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = body,
            color = AnalysisDetailTheme.BodyColor,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            modifier = if (highlighted) {
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(AnalysisDetailTheme.FeatureCardBg)
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            } else {
                Modifier.fillMaxWidth()
            }
        )
        if (showDivider) {
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(
                color = AnalysisDetailTheme.FeatureCardBorder.copy(alpha = 0.5f),
                thickness = 0.5.dp
            )
        }
    }
}
