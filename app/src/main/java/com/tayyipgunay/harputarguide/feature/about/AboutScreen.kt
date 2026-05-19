package com.tayyipgunay.harputarguide.feature.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.R
import com.tayyipgunay.harputarguide.core.design.component.AboutFeatureItem
import com.tayyipgunay.harputarguide.core.design.component.AboutFooter
import com.tayyipgunay.harputarguide.core.design.component.AboutHeroSection
import com.tayyipgunay.harputarguide.core.design.component.AboutInfoCard
import com.tayyipgunay.harputarguide.core.design.component.AboutLinkRow
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBar
import com.tayyipgunay.harputarguide.core.design.component.LanguageSelectorCard
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBarStyle
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomTab
import kotlinx.coroutines.launch

private data class AboutFeature(
    val title: String,
    val description: String,
    val icon: ImageVector
)

private data class AboutLink(
    val label: String,
    val icon: ImageVector,
    val action: AboutLinkAction
)

private enum class AboutLinkAction {
    Privacy,
    Terms,
    Faq,
    Contact
}

private val aboutFeatures = listOf(
    AboutFeature(
        title = "Artırılmış Gerçeklik",
        description = "Kamera ile tarihi yapıları inceleyin ve AR bilgi katmanlarını görün.",
        icon = Icons.Filled.ViewInAr
    ),
    AboutFeature(
        title = "Geçmişi Gör",
        description = "Tarihi yapıların geçmiş ve günümüz halini karşılaştırın.",
        icon = Icons.Filled.History
    ),
    AboutFeature(
        title = "Yapı Bilgisi",
        description = "Yapı elemanları, malzemeler ve mimari detayları öğrenin.",
        icon = Icons.Filled.Apartment
    ),
    AboutFeature(
        title = "Kitabe Oku",
        description = "Kitabeleri tarayın, açıklamalarını ve çevirilerini görüntüleyin.",
        icon = Icons.Filled.MenuBook
    ),
    AboutFeature(
        title = "Sesli Rehber",
        description = "Harput hakkında sesli anlatımlarla bilgi alın.",
        icon = Icons.Filled.VolumeUp
    )
)

private val aboutLinks = listOf(
    AboutLink("Gizlilik Politikası", Icons.Filled.Shield, AboutLinkAction.Privacy),
    AboutLink("Kullanım Koşulları", Icons.Filled.Gavel, AboutLinkAction.Terms),
    AboutLink("Sıkça Sorulan Sorular", Icons.Filled.Help, AboutLinkAction.Faq),
    AboutLink("İletişim", Icons.Filled.Email, AboutLinkAction.Contact)
)

@Composable
fun AboutScreen(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPlacesClick: () -> Unit,
    onVisitedClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onFaqClick: () -> Unit
) {
    val cream = Color(0xFFF5EBDD)
    val cardColor = Color(0xFFFDFBF7)
    val darkBrown = Color(0xFF4B2E1F)
    val softBrown = Color(0xFF72533C)
    val iconBg = Color(0xFFE8DFD0)
    val dividerColor = Color(0xFFD8C9B6)
    val bottomBarBg = Color(0xFFF0E4D4)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun showPlaceholder(message: String) {
        scope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = cream,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            HarputBottomBar(
                selectedTab = HarputBottomTab.About,
                style = HarputBottomBarStyle.Explore,
                onHomeClick = onHomeClick,
                onPlacesClick = onPlacesClick,
                onArClick = { },
                onVisitedClick = onVisitedClick,
                onFavoritesClick = onFavoritesClick,
                onAboutClick = { },
                backgroundColor = bottomBarBg,
                activeColor = darkBrown,
                inactiveColor = softBrown,
                activeLabelColor = cream,
                inactiveLabelColor = softBrown
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                AboutTopBar(
                    onBackClick = onBackClick,
                    onInfoClick = {
                        showPlaceholder(
                            "Harput AR Rehber, kültürel miras deneyimini artırılmış gerçeklik ile sunar."
                        )
                    },
                    darkBrown = darkBrown
                )
            }

            item {
                AboutHeroSection(
                    heroImageRes = R.drawable.harput_welcome_png,
                    title = "Harput",
                    subtitle = "AR Rehber",
                    tagline = "Tarihi keşfet, geleceği artırılmış gerçeklikle yaşa.",
                    darkBrown = darkBrown,
                    softBrown = softBrown
                )
            }

            item {
                AboutInfoCard(
                    title = "Uygulama Hakkında",
                    body = "Harput AR Rehber, Harput'un tarihi ve kültürel mirasını artırılmış gerçeklik teknolojisi ile keşfetmeniz için tasarlanmış mobil bir rehberdir. Geçmişi bugüne bağlayan bu deneyimle, Harput'u daha yakından tanıyabilir; yapıları, kitabeleri, malzemeleri ve tarihi dokuyu interaktif bir şekilde inceleyebilirsiniz.",
                    icon = Icons.Filled.MenuBook,
                    cardColor = cardColor,
                    iconContainerColor = iconBg,
                    iconColor = darkBrown,
                    titleColor = darkBrown,
                    bodyColor = softBrown,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            item {
                LanguageSelectorCard(
                    selectedLanguage = selectedLanguage,
                    onLanguageSelected = onLanguageSelected,
                    cardColor = cardColor,
                    titleColor = darkBrown,
                    descriptionColor = softBrown,
                    selectedChipColor = darkBrown,
                    unselectedChipColor = iconBg,
                    selectedTextColor = cream,
                    unselectedTextColor = softBrown,
                    borderColor = dividerColor,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            item {
                Text(
                    text = "Öne Çıkan Özellikler",
                    color = darkBrown,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            items(aboutFeatures) { feature ->
                AboutFeatureItem(
                    title = feature.title,
                    description = feature.description,
                    icon = feature.icon,
                    iconContainerColor = iconBg,
                    iconColor = darkBrown,
                    titleColor = darkBrown,
                    descriptionColor = softBrown,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(cardColor)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    aboutLinks.forEachIndexed { index, link ->
                        AboutLinkRow(
                            label = link.label,
                            icon = link.icon,
                            onClick = {
                                when (link.action) {
                                    AboutLinkAction.Faq -> onFaqClick()
                                    AboutLinkAction.Privacy -> showPlaceholder(
                                        "Gizlilik politikası yakında eklenecektir."
                                    )
                                    AboutLinkAction.Terms -> showPlaceholder(
                                        "Kullanım koşulları yakında eklenecektir."
                                    )
                                    AboutLinkAction.Contact -> showPlaceholder(
                                        "İletişim bilgileri yakında eklenecektir."
                                    )
                                }
                            },
                            textColor = darkBrown,
                            iconColor = darkBrown,
                            chevronColor = softBrown,
                            dividerColor = dividerColor,
                            showDivider = index > 0
                        )
                    }
                }
            }

            item {
                AboutFooter(
                    version = "Versiyon 1.0.0",
                    techStack = "Kotlin • Jetpack Compose • AR Destekli Mobil Rehber",
                    copyright = "© 2026 HarputARGuide. Tüm hakları saklıdır.",
                    textColor = darkBrown,
                    mutedColor = softBrown
                )
            }
        }
    }
}

@Composable
private fun AboutTopBar(
    onBackClick: () -> Unit,
    onInfoClick: () -> Unit,
    darkBrown: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.cd_back),
                tint = darkBrown
            )
        }
        Text(
            text = stringResource(R.string.about_title),
            color = darkBrown,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        IconButton(
            onClick = onInfoClick,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8DFD0))
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Bilgi",
                tint = darkBrown,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
