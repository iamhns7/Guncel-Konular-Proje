package com.tayyipgunay.harputarguide.feature.auth

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.R

@Composable
fun LoginScreen(
    onGoogleSignInClick: () -> Unit
) {
    val cream = Color(0xFFF5EBDD)
    val cardColor = Color(0xFFFDFBF7)
    val darkBrown = Color(0xFF4B2E1F)
    val softBrown = Color(0xFF72533C)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cream)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        LoginHeroSection(
            darkBrown = darkBrown,
            softBrown = softBrown
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "HarputARGuide",
                color = darkBrown,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Harput'u artırılmış gerçeklik ile keşfet.",
                color = darkBrown,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Tarihi noktaları gezin, geçmişi görün, kitabeleri okuyun ve keşiflerinizi kaydedin.",
                color = softBrown,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            GoogleSignInCard(
                onGoogleSignInClick = onGoogleSignInClick,
                cardColor = cardColor,
                darkBrown = darkBrown,
                softBrown = softBrown
            )

            LoginFooterNote(softBrown = softBrown)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun LoginHeroSection(
    darkBrown: Color,
    softBrown: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.harput_welcome_png),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.4f
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to Color(0xFFF5EBDD).copy(alpha = 0.3f),
                            0.5f to Color(0xFFF5EBDD).copy(alpha = 0.7f),
                            1f to Color(0xFFF5EBDD)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8DFD0)),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Filled.AccountBox,
                    contentDescription = null,
                    tint = darkBrown,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
private fun GoogleSignInCard(
    onGoogleSignInClick: () -> Unit,
    cardColor: Color,
    darkBrown: Color,
    softBrown: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardColor)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Devam etmek için giriş yap",
            color = darkBrown,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
        )
        Text(
            text = "Favorilerinizi ve gezdiğiniz noktaları kaydetmek için Google hesabınızla devam edin.",
            color = softBrown,
            fontSize = 13.sp,
            lineHeight = 19.sp
        )
        GoogleSignInButton(
            onClick = onGoogleSignInClick,
            darkBrown = darkBrown
        )
    }
}

@Composable
private fun GoogleSignInButton(
    onClick: () -> Unit,
    darkBrown: Color
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = darkBrown
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF1F1F1)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "G",
                    color = Color(0xFF4285F4),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Text(
                text = "Google ile Devam Et",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
private fun LoginFooterNote(softBrown: Color) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Kullanıcı adı veya şifre ile giriş desteklenmez.",
            color = softBrown.copy(alpha = 0.8f),
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Google ile güvenli giriş desteği sonraki adımda eklenecektir.",
            color = softBrown.copy(alpha = 0.65f),
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}
