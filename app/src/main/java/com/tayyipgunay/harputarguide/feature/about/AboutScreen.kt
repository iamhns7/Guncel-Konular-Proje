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
import androidx.annotation.StringRes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.R
import com.tayyipgunay.harputarguide.core.design.theme.HarputColors
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
    @StringRes val titleRes: Int,
    @StringRes val descRes: Int,
    val icon: ImageVector
)

private data class AboutLink(
    @StringRes val labelRes: Int,
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
        titleRes = R.string.about_feature_ar_title,
        descRes = R.string.about_feature_ar_desc,
        icon = Icons.Filled.ViewInAr
    ),
    AboutFeature(
        titleRes = R.string.about_feature_past_title,
        descRes = R.string.about_feature_past_desc,
        icon = Icons.Filled.History
    ),
    AboutFeature(
        titleRes = R.string.about_feature_structure_title,
        descRes = R.string.about_feature_structure_desc,
        icon = Icons.Filled.Apartment
    )
)

private val aboutLinks = listOf(
    AboutLink(R.string.about_link_privacy, Icons.Filled.Shield, AboutLinkAction.Privacy),
    AboutLink(R.string.about_link_terms, Icons.Filled.Gavel, AboutLinkAction.Terms),
    AboutLink(R.string.about_link_faq, Icons.Filled.Help, AboutLinkAction.Faq),
    AboutLink(R.string.about_link_contact, Icons.Filled.Email, AboutLinkAction.Contact)
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
    val cream = HarputColors.Cream
    val cardColor = HarputColors.CardLight
    val darkBrown = HarputColors.DarkBrown
    val softBrown = HarputColors.SoftBrown
    val iconBg = HarputColors.IconBg
    val dividerColor = HarputColors.Divider
    val bottomBarBg = HarputColors.BottomBarBg

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val aboutInfoMessage = stringResource(R.string.about_info_snackbar)
    val privacySoon = stringResource(R.string.about_privacy_soon)
    val termsSoon = stringResource(R.string.about_terms_soon)
    val contactSoon = stringResource(R.string.about_contact_soon)

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
                    onInfoClick = { showPlaceholder(aboutInfoMessage) },
                    darkBrown = darkBrown
                )
            }

            item {
                AboutHeroSection(
                    heroImageRes = R.drawable.harput_welcome_png,
                    title = stringResource(R.string.about_hero_title),
                    subtitle = stringResource(R.string.about_hero_subtitle),
                    tagline = stringResource(R.string.about_hero_tagline),
                    darkBrown = darkBrown,
                    softBrown = softBrown
                )
            }

            item {
                AboutInfoCard(
                    title = stringResource(R.string.about_card_title),
                    body = stringResource(R.string.about_card_body),
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
                    text = stringResource(R.string.about_features_title),
                    color = darkBrown,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            items(aboutFeatures) { feature ->
                AboutFeatureItem(
                    title = stringResource(feature.titleRes),
                    description = stringResource(feature.descRes),
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
                            label = stringResource(link.labelRes),
                            icon = link.icon,
                            onClick = {
                                when (link.action) {
                                    AboutLinkAction.Faq -> onFaqClick()
                                    AboutLinkAction.Privacy -> showPlaceholder(privacySoon)
                                    AboutLinkAction.Terms -> showPlaceholder(termsSoon)
                                    AboutLinkAction.Contact -> showPlaceholder(contactSoon)
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
                    version = stringResource(R.string.about_footer_version),
                    techStack = stringResource(R.string.about_footer_tech),
                    copyright = stringResource(R.string.about_footer_copyright),
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
                .background(HarputColors.IconBg)
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = stringResource(R.string.cd_info),
                tint = darkBrown,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
