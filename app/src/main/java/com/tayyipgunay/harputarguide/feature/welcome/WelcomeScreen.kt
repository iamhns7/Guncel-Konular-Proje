package com.tayyipgunay.harputarguide.feature.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.R
import com.tayyipgunay.harputarguide.core.design.theme.HarputColors

@Composable
fun WelcomeScreen(
    onStartTourClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val cream = HarputColors.Cream
    val beige = HarputColors.Beige
    val darkBrown = HarputColors.DarkBrown
    val softBrown = HarputColors.SoftBrown

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(cream, Color(0xFFF1E2CF), beige)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 18.dp, vertical = 14.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BrandHeader(
                    titleColor = darkBrown,
                    subtitleColor = softBrown
                )
                Spacer(modifier = Modifier.height(12.dp))
                WelcomeTexts(
                    titleColor = darkBrown,
                    descriptionColor = softBrown
                )
                Spacer(modifier = Modifier.height(12.dp))
                CastleImage()
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                StartTourButton(
                    onClick = onStartTourClick,
                    containerColor = darkBrown
                )
                BottomActions(
                    titleColor = darkBrown,
                    onFavoritesClick = onFavoritesClick,
                    onAboutClick = onAboutClick
                )
            }
        }
    }
}

@Composable
private fun BrandHeader(
    titleColor: Color,
    subtitleColor: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .border(width = 1.dp, color = subtitleColor.copy(alpha = 0.4f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "H",
                color = titleColor,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                fontSize = 23.sp
            )
        }
        Text(
            text = stringResource(R.string.app_brand),
            color = titleColor,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp
        )
        Text(
            text = stringResource(R.string.app_tagline),
            color = subtitleColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp
        )
        DecorativeDivider(color = subtitleColor.copy(alpha = 0.45f))
    }
}

@Composable
private fun DecorativeDivider(color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.62f)
            .padding(top = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(color)
        )
        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = "◆",
            color = color,
            fontSize = 10.sp
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(color)
        )
    }
}

@Composable
private fun WelcomeTexts(
    titleColor: Color,
    descriptionColor: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(R.string.welcome_title),
            color = titleColor,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 36.sp,
            lineHeight = 41.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.welcome_description),
            color = descriptionColor,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CastleImage() {
    Image(
        painter = painterResource(id = R.drawable.harput_welcome_png),
        contentDescription = stringResource(R.string.cd_castle_image),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                color = Color(0xFF6C4D32).copy(alpha = 0.45f),
                shape = RoundedCornerShape(20.dp)
            ),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun StartTourButton(
    onClick: () -> Unit,
    containerColor: Color
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(R.string.welcome_start_tour),
                color = HarputColors.Cream,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "→",
                color = HarputColors.Cream,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun BottomActions(
    titleColor: Color,
    onFavoritesClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            modifier = Modifier.weight(1f),
            onClick = onFavoritesClick
        ) {
            Text(
                text = stringResource(R.string.welcome_favorites),
                color = titleColor,
                fontSize = 14.sp
            )
        }
        Box(
            modifier = Modifier
                .height(20.dp)
                .width(1.dp)
                .background(titleColor.copy(alpha = 0.25f))
        )
        TextButton(
            modifier = Modifier.weight(1f),
            onClick = onAboutClick
        ) {
            Text(
                text = stringResource(R.string.welcome_about),
                color = titleColor,
                fontSize = 14.sp
            )
        }
    }
}
