package com.tayyipgunay.harputarguide.core.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Kamera görüntüsünün üzerine bindirilen kitabe okuma AR katmanı.
 * Alt katmandaki canlı/placeholder görüntü [ARScreen] içinde gösterilir.
 */
@Composable
fun InscriptionReadOverlay(
    language: InscriptionLanguage,
    modifier: Modifier = Modifier
) {
    val translation = SampleInscriptionTexts.translation(language)
    val hijriDate = SampleInscriptionTexts.hijriDate(language)

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                .height(220.dp)
        ) {
            ScanFrame(
                modifier = Modifier.fillMaxSize(),
                cornerColor = Color.White.copy(alpha = 0.95f)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 20.dp, vertical = 36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF3D2814).copy(alpha = 0.55f),
                                Color(0xFF2A1810).copy(alpha = 0.65f)
                            )
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = translation,
                        color = Color.White.copy(alpha = 0.95f),
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center
                    )
                    HijriDateDecoration(date = hijriDate)
                }
            }
        }

        OcrStatusPill(
            text = "Metin OCR ile algılandı",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}

@Composable
private fun HijriDateDecoration(date: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color.White.copy(alpha = 0.4f),
                thickness = 0.5.dp
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(6.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(Color(0xFFD4A574))
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color.White.copy(alpha = 0.4f),
                thickness = 0.5.dp
            )
        }
        Text(
            text = date,
            color = Color(0xFFD4A574),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
