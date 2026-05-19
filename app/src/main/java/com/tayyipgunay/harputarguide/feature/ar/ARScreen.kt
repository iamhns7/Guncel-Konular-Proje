package com.tayyipgunay.harputarguide.feature.ar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.tayyipgunay.harputarguide.core.design.component.ArFocusFrame
import com.tayyipgunay.harputarguide.core.design.component.ArInfoOverlayCard
import com.tayyipgunay.harputarguide.core.design.component.ArMode
import com.tayyipgunay.harputarguide.core.design.component.ArModeBottomPanel
import com.tayyipgunay.harputarguide.core.design.component.InscriptionDetailPanel
import com.tayyipgunay.harputarguide.core.design.component.InscriptionLanguage
import com.tayyipgunay.harputarguide.core.design.component.InscriptionActionButtons
import com.tayyipgunay.harputarguide.core.design.component.InscriptionReadOverlay
import com.tayyipgunay.harputarguide.core.design.component.LanguageSegmentedControl
import com.tayyipgunay.harputarguide.core.design.component.PastViewControlCard
import com.tayyipgunay.harputarguide.core.design.component.PastViewInfoBadge
import com.tayyipgunay.harputarguide.core.design.component.PastViewOverlay
import com.tayyipgunay.harputarguide.core.design.component.SelectedHotspotPanel
import com.tayyipgunay.harputarguide.core.design.component.StructureHotspotOverlay
import com.tayyipgunay.harputarguide.data.asset.SamplePlaceDetails
import com.tayyipgunay.harputarguide.data.asset.SampleStructureHotspots
import kotlinx.coroutines.launch

@Composable
fun ARScreen(
    placeId: String,
    onBackClick: () -> Unit,
    onPlacesClick: () -> Unit,
    onHotspotDetailClick: (placeId: String, hotspotId: String) -> Unit
) {
    val detail = remember(placeId) { SamplePlaceDetails.getById(placeId) }
    val structureHotspots = remember(placeId) { SampleStructureHotspots.getHotspots(placeId) }

    var isFavorite by remember(placeId) { mutableStateOf(false) }
    var selectedMode by remember { mutableStateOf(ArMode.Info) }
    var selectedHotspotId by remember { mutableStateOf<String?>(null) }
    var pastSliderValue by remember { mutableFloatStateOf(0.5f) }
    var inscriptionLanguage by remember { mutableStateOf(InscriptionLanguage.TR) }
    var showInscriptionDetail by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val cream = Color(0xFFF5EBDD).copy(alpha = 0.92f)
    val inscriptionCream = Color(0xFFF5EBDD)
    val darkBrown = Color(0xFF4B2E1F)
    val softBrown = Color(0xFF72533C)
    val panelColor = Color(0xFF3D2618).copy(alpha = 0.88f)
    val footerColor = Color(0xFF2E1A10).copy(alpha = 0.75f)
    val iconBrown = Color(0xFF4B2E1F)
    val hotspotPanelColor = Color(0xFF2A1810).copy(alpha = 0.92f)
    val pastControlCardColor = Color(0xFF2A1810).copy(alpha = 0.92f)
    val inscriptionButtonColor = Color(0xFFE8DFD0)

    val selectedHotspot = structureHotspots.find { it.id == selectedHotspotId }
    val isInscriptionMode = selectedMode == ArMode.Inscription

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.harput_welcome_png),
            contentDescription = "Kamera görüntüsü",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (selectedMode == ArMode.Past) {
            PastViewOverlay(
                sliderValue = pastSliderValue,
                presentImageRes = R.drawable.harput_welcome_png,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (isInscriptionMode) {
            InscriptionReadOverlay(
                language = inscriptionLanguage,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (!isInscriptionMode) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0f to Color.Black.copy(alpha = 0.35f),
                                0.35f to Color.Transparent,
                                0.65f to Color.Black.copy(alpha = 0.25f),
                                1f to Color.Black.copy(alpha = 0.55f)
                            )
                        )
                    )
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            if (isInscriptionMode) {
                InscriptionModeHeader(
                    onBackClick = onBackClick,
                    onInfoClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Kitabe metni şimdilik hazır örnek veridir. İleride OCR ile algılanacaktır."
                            )
                        }
                    },
                    selectedLanguage = inscriptionLanguage,
                    onLanguageSelected = { inscriptionLanguage = it },
                    creamColor = inscriptionCream,
                    darkBrown = darkBrown,
                    softBrown = softBrown
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ArCircleButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    when (selectedMode) {
                        ArMode.Structure -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                ArModeTitlePill(title = "Yapı Bilgisi")
                                if (SampleStructureHotspots.isDemoSample(placeId)) {
                                    Spacer(modifier = Modifier.size(4.dp))
                                    ArModeTitlePill(title = "Örnek hotspot verisi")
                                }
                            }
                        }
                        ArMode.Past -> {
                            PastViewInfoBadge(
                                title = "Geçmişi Gör",
                                onInfoClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Geçmiş görselleri, tarihsel kaynaklara dayalı temsili rekonstrüksiyonlardır."
                                        )
                                    }
                                }
                            )
                        }
                        else -> Spacer(modifier = Modifier.size(42.dp))
                    }

                    ArCircleButton(onClick = { isFavorite = !isFavorite }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favori",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            when (selectedMode) {
                ArMode.Info -> {
                    ArInfoOverlayCard(
                        title = detail.name,
                        location = detail.location,
                        description = detail.description,
                        cardColor = cream,
                        titleColor = darkBrown,
                        mutedColor = softBrown,
                        iconContainerColor = iconBrown,
                        dividerColor = softBrown.copy(alpha = 0.35f),
                        activeDotColor = darkBrown,
                        inactiveDotColor = softBrown.copy(alpha = 0.35f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
                ArMode.Inscription -> Spacer(modifier = Modifier.size(4.dp))
                else -> Spacer(modifier = Modifier.size(4.dp))
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (selectedMode) {
                    ArMode.Past, ArMode.Inscription -> Unit
                    ArMode.Structure -> {
                        if (structureHotspots.isNotEmpty()) {
                            StructureHotspotOverlay(
                                hotspots = structureHotspots,
                                selectedHotspotId = selectedHotspotId,
                                onHotspotClick = { id ->
                                    selectedHotspotId = if (selectedHotspotId == id) null else id
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        if (selectedHotspot == null) {
                            Text(
                                text = "Detaylı bilgi için ekrandaki noktalara dokunun.",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 12.dp, start = 24.dp, end = 24.dp)
                            )
                        }
                    }
                    ArMode.Info -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ArFocusFrame()
                            Spacer(modifier = Modifier.size(16.dp))
                            ArHelpBubble()
                        }
                    }
                    else -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ArFocusFrame()
                            Spacer(modifier = Modifier.size(16.dp))
                            ArHelpBubble()
                        }
                    }
                }
            }

            if (selectedMode == ArMode.Structure && selectedHotspot != null) {
                SelectedHotspotPanel(
                    hotspot = selectedHotspot,
                    onCloseClick = { selectedHotspotId = null },
                    onDetailClick = { onHotspotDetailClick(placeId, selectedHotspot.id) },
                    panelColor = hotspotPanelColor,
                    textColor = Color(0xFFF5EBDD),
                    detailButtonColor = darkBrown,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            if (selectedMode == ArMode.Past) {
                PastViewControlCard(
                    sliderValue = pastSliderValue,
                    onSliderValueChange = { pastSliderValue = it },
                    cardColor = pastControlCardColor,
                    textColor = Color(0xFFF5EBDD),
                    accentColor = Color(0xFFD4A574),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            if (isInscriptionMode) {
                InscriptionActionButtons(
                    onListenClick = {
                        showInscriptionDetail = false
                        selectedMode = ArMode.Audio
                        scope.launch {
                            snackbarHostState.showSnackbar("Dinle moduna geçildi.")
                        }
                    },
                    onDetailClick = { showInscriptionDetail = true },
                    buttonColor = inscriptionButtonColor,
                    textColor = darkBrown
                )
            }

            ArModeBottomPanel(
                selectedMode = selectedMode,
                onModeSelected = { mode ->
                    selectedMode = mode
                    if (mode != ArMode.Structure) {
                        selectedHotspotId = null
                    }
                    if (mode != ArMode.Inscription) {
                        showInscriptionDetail = false
                    }
                },
                onPlacesClick = onPlacesClick,
                panelColor = panelColor,
                activeColor = Color.White.copy(alpha = 0.25f),
                inactiveColor = Color.White.copy(alpha = 0.12f),
                labelColor = Color(0xFFF5EBDD),
                footerColor = footerColor,
                showVisitFooter = selectedMode != ArMode.Past && !isInscriptionMode
            )
        }

        if (isInscriptionMode && showInscriptionDetail) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )
            InscriptionDetailPanel(
                title = "Kitabe Açıklaması",
                body = "Kitabe, yapının kim tarafından yaptırıldığını ve hangi dönemde tamamlandığını anlatır. Bu metin ilk sürümde hazır veri olarak gösterilmektedir.",
                onCloseClick = { showInscriptionDetail = false },
                panelColor = inscriptionCream,
                textColor = darkBrown,
                mutedColor = softBrown,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp)
        )
    }
}

@Composable
private fun InscriptionModeHeader(
    onBackClick: () -> Unit,
    onInfoClick: () -> Unit,
    selectedLanguage: InscriptionLanguage,
    onLanguageSelected: (InscriptionLanguage) -> Unit,
    creamColor: Color,
    darkBrown: Color,
    softBrown: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(creamColor)
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Geri",
                    tint = darkBrown
                )
            }
            Text(
                text = "Kitabe Oku",
                color = darkBrown,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = onInfoClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0D5C8))
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Bilgi",
                    tint = darkBrown,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        LanguageSegmentedControl(
            selectedLanguage = selectedLanguage,
            onLanguageSelected = onLanguageSelected,
            backgroundColor = Color(0xFFEDE4D8),
            selectedColor = darkBrown,
            selectedTextColor = creamColor,
            unselectedTextColor = softBrown,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun ArModeTitlePill(title: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black.copy(alpha = 0.55f))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun ArCircleButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.45f))
    ) {
        content()
    }
}

@Composable
private fun ArHelpBubble() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black.copy(alpha = 0.55f))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.CenterFocusStrong,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = "En iyi deneyim için kamerayı yapıya doğru tutun.",
            color = Color.White,
            fontSize = 12.sp
        )
    }
}
