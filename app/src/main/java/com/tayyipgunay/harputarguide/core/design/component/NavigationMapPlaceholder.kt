package com.tayyipgunay.harputarguide.core.design.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NavigationMapPlaceholder(
    destinationLabel: String,
    modifier: Modifier = Modifier
) {
    val mapBase = Color(0xFFE8E2D4)
    val mapGreen = Color(0xFFD4DFC8)
    val routeBlue = Color(0xFF4A90D9)
    val faintPath = Color(0xFFC8C0B4).copy(alpha = 0.6f)

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(mapBase, mapGreen, mapBase)
                )
            )

            val w = size.width
            val h = size.height

            listOf(
                listOf(Offset(0f, h * 0.3f), Offset(w * 0.9f, h * 0.25f)),
                listOf(Offset(w * 0.1f, h * 0.55f), Offset(w * 0.85f, h * 0.5f)),
                listOf(Offset(w * 0.2f, h * 0.8f), Offset(w * 0.7f, h * 0.75f))
            ).forEach { line ->
                drawLine(
                    color = faintPath,
                    start = line[0],
                    end = line[1],
                    strokeWidth = 3f,
                    cap = StrokeCap.Round
                )
            }

            val route = Path().apply {
                moveTo(w * 0.22f, h * 0.72f)
                quadraticTo(w * 0.35f, h * 0.55f, w * 0.48f, h * 0.48f)
                quadraticTo(w * 0.62f, h * 0.38f, w * 0.78f, h * 0.28f)
            }
            drawPath(
                path = route,
                color = routeBlue,
                style = Stroke(width = 10f, cap = StrokeCap.Round)
            )

            drawCircle(
                color = routeBlue.copy(alpha = 0.2f),
                radius = 28f,
                center = Offset(w * 0.22f, h * 0.72f)
            )
        }

        Box(
            modifier = Modifier
                .align(BiasAlignment(0.22f, 0.72f))
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFF4A90D9).copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Navigation,
                contentDescription = "Konumunuz",
                tint = Color(0xFF2E6EB5),
                modifier = Modifier.size(22.dp)
            )
        }

        Box(
            modifier = Modifier
                .align(BiasAlignment(0.78f, 0.28f))
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.95f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            RowMarkerContent(
                destinationLabel = destinationLabel,
                pinColor = Color(0xFF4B2E1F)
            )
        }
    }
}

@Composable
private fun RowMarkerContent(
    destinationLabel: String,
    pinColor: Color
) {
    androidx.compose.foundation.layout.Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = null,
            tint = pinColor,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = destinationLabel,
            color = pinColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}
