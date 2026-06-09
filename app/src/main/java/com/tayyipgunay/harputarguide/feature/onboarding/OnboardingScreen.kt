package com.tayyipgunay.harputarguide.feature.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.R
import com.tayyipgunay.harputarguide.core.design.component.OnboardingStepRow
import com.tayyipgunay.harputarguide.core.design.theme.HarputColors

private data class OnboardingStep(
    val number: Int,
    @StringRes val titleRes: Int,
    @StringRes val descRes: Int,
    @DrawableRes val imageResId: Int
)

private val onboardingSteps = listOf(
    OnboardingStep(
        number = 1,
        titleRes = R.string.onboarding_step1_title,
        descRes = R.string.onboarding_step1_desc,
        imageResId = R.drawable.onboarding_png_1
    ),
    OnboardingStep(
        number = 2,
        titleRes = R.string.onboarding_step2_title,
        descRes = R.string.onboarding_step2_desc,
        imageResId = R.drawable.onboarding_png_2
    ),
    OnboardingStep(
        number = 3,
        titleRes = R.string.onboarding_step3_title,
        descRes = R.string.onboarding_step3_desc,
        imageResId = R.drawable.onboarding_png_3
    ),
    OnboardingStep(
        number = 4,
        titleRes = R.string.onboarding_step4_title,
        descRes = R.string.onboarding_step4_desc,
        imageResId = R.drawable.onboarding_png_4
    )
)

@Composable
fun OnboardingScreen(
    onBackClick: () -> Unit,
    onStartClick: () -> Unit
) {
    val cream = HarputColors.Cream
    val beige = HarputColors.Beige
    val darkBrown = HarputColors.DarkBrown
    val softBrown = HarputColors.SoftBrown
    val iconBorder = Color(0xFFB99977).copy(alpha = 0.35f)
    val dividerColor = Color(0xFFB89977).copy(alpha = 0.28f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(cream, Color(0xFFF2E5D4), beige)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            OnboardingTopBar(
                onBackClick = onBackClick,
                iconColor = darkBrown
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                OnboardingHeader(
                    titleColor = darkBrown,
                    subtitleColor = softBrown
                )

                Spacer(modifier = Modifier.height(8.dp))

                onboardingSteps.forEachIndexed { index, step ->
                    OnboardingStepRow(
                        number = step.number,
                        title = stringResource(step.titleRes),
                        description = stringResource(step.descRes),
                        imageResId = step.imageResId,
                        titleColor = darkBrown,
                        descriptionColor = softBrown,
                        iconBorderColor = iconBorder,
                        showDivider = index < onboardingSteps.lastIndex,
                        dividerColor = dividerColor
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            StartButton(
                onClick = onStartClick,
                containerColor = darkBrown,
                contentColor = cream,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
private fun OnboardingTopBar(
    onBackClick: () -> Unit,
    iconColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 18.dp, top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Text(
                text = "←",
                color = iconColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun OnboardingHeader(
    titleColor: Color,
    subtitleColor: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(R.string.onboarding_title),
            color = titleColor,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.onboarding_subtitle),
            color = subtitleColor,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StartButton(
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(R.string.onboarding_start),
                color = contentColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "→",
                color = contentColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
