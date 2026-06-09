package com.tayyipgunay.harputarguide.core.design.component.analysis

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.domain.model.HotspotFeature

@Composable
fun AnalysisFeatureGrid(
    features: List<HotspotFeature>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        features.forEach { feature ->
            AnalysisFeatureCard(
                feature = feature,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AnalysisFeatureCard(
    feature: HotspotFeature,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(AnalysisDetailTheme.FeatureCardBg)
            .border(
                width = 0.5.dp,
                color = AnalysisDetailTheme.FeatureCardBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = FeatureIconMapper.iconFor(feature.label),
                contentDescription = null,
                tint = AnalysisDetailTheme.Accent,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = feature.label,
                color = AnalysisDetailTheme.BodyColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = feature.value,
            color = AnalysisDetailTheme.TitleColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun AnalysisFeatureTableRow(
    label: String,
    value: String,
    textColor: Color,
    iconTint: Color,
    dividerColor: Color,
    showDivider: Boolean,
    modifier: Modifier = Modifier
) {
    AnalysisFeatureCard(
        feature = HotspotFeature(label = label, value = value),
        modifier = modifier
    )
}
