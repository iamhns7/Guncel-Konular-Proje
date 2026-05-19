package com.tayyipgunay.harputarguide.core.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.R
import com.tayyipgunay.harputarguide.core.locale.AppLanguage

@Composable
fun LanguageSelectorCard(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    cardColor: Color,
    titleColor: Color,
    descriptionColor: Color,
    selectedChipColor: Color,
    unselectedChipColor: Color,
    selectedTextColor: Color,
    unselectedTextColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardColor)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(R.string.language_section_title),
            color = titleColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = stringResource(R.string.language_section_description),
            color = descriptionColor,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            LanguageChip(
                label = stringResource(R.string.language_turkish),
                selected = selectedLanguage == AppLanguage.TR,
                onClick = { onLanguageSelected(AppLanguage.TR) },
                selectedBackground = selectedChipColor,
                unselectedBackground = unselectedChipColor,
                selectedTextColor = selectedTextColor,
                unselectedTextColor = unselectedTextColor,
                borderColor = borderColor,
                modifier = Modifier.weight(1f)
            )
            LanguageChip(
                label = stringResource(R.string.language_english),
                selected = selectedLanguage == AppLanguage.EN,
                onClick = { onLanguageSelected(AppLanguage.EN) },
                selectedBackground = selectedChipColor,
                unselectedBackground = unselectedChipColor,
                selectedTextColor = selectedTextColor,
                unselectedTextColor = unselectedTextColor,
                borderColor = borderColor,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun LanguageChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    selectedBackground: Color,
    unselectedBackground: Color,
    selectedTextColor: Color,
    unselectedTextColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    val background = if (selected) selectedBackground else unselectedBackground
    val textColor = if (selected) selectedTextColor else unselectedTextColor
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .border(
                width = 1.dp,
                color = if (selected) selectedBackground else borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = textColor,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}
