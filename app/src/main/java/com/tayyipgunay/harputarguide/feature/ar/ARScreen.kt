package com.tayyipgunay.harputarguide.feature.ar

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.key
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.ar.core.ArCoreApk
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException
import com.tayyipgunay.harputarguide.R
import com.tayyipgunay.harputarguide.core.design.component.ArFocusFrame
import com.tayyipgunay.harputarguide.core.design.component.ArInfoOverlayCard
import com.tayyipgunay.harputarguide.core.design.component.ArMode
import com.tayyipgunay.harputarguide.core.design.component.ArModeBottomPanel
import com.tayyipgunay.harputarguide.core.design.component.PastViewControlCard
import com.tayyipgunay.harputarguide.core.design.component.PastViewInfoBadge
import com.tayyipgunay.harputarguide.core.design.component.PastViewOverlay
import com.tayyipgunay.harputarguide.core.design.component.StructureHotspotOverlay
import com.tayyipgunay.harputarguide.core.design.theme.HarputColors
import com.tayyipgunay.harputarguide.data.ar.PastOverlayPaths
import com.tayyipgunay.harputarguide.data.asset.SamplePlaceDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

@Composable
fun ARScreen(
    placeId: String,
    onBackClick: () -> Unit,
    onPlacesClick: () -> Unit,
    onHotspotDetailClick: (placeId: String, hotspotId: String) -> Unit,
    viewModel: ARViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val arContent = uiState.arContent
    val fallbackDetail = remember(placeId) { SamplePlaceDetails.getById(placeId) }
    val infoTitle = arContent?.title ?: arContent?.info?.title ?: fallbackDetail.name
    val infoLocation = arContent?.info?.location ?: fallbackDetail.location
    val infoDescription = arContent?.shortDescription?.takeIf { it.isNotBlank() }
        ?: arContent?.info?.description
        ?: fallbackDetail.description
    val structureHotspots = uiState.hotspots
    val pastOverlayPath = remember(placeId) { PastOverlayPaths.forPlace(placeId) }
    val pastOverlayAvailable = pastOverlayPath != null

    var selectedMode by remember { mutableStateOf(ArMode.Structure) }
    var selectedHotspotId by remember { mutableStateOf<String?>(null) }
    var pastSliderValue by remember { mutableFloatStateOf(0.5f) }
    var referenceBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var cameraPermissionGranted by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val haptic = LocalHapticFeedback.current
    val arFeedback = remember { ArFeedbackController(context) }
    var lastTrackingState by remember { mutableStateOf(ArTrackingState.NOT_INITIALIZED) }
    var cameraReadyFeedbackPlayed by remember { mutableStateOf(false) }
    var hotspotsRevealed by remember { mutableStateOf(false) }
    var useAssistedPlacement by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose { arFeedback.release() }
    }

    LaunchedEffect(uiState.trackingState) {
        when {
            uiState.trackingState.isAlignedEnough() &&
                !lastTrackingState.isAlignedEnough() -> {
                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                arFeedback.playTrackingLocked()
            }
            (uiState.trackingState == ArTrackingState.LOST ||
                uiState.trackingState == ArTrackingState.SEARCHING) &&
                lastTrackingState.isAlignedEnough() -> {
                haptic.performHapticFeedback(HapticFeedbackType.Reject)
                arFeedback.playTrackingLost()
            }
        }
        lastTrackingState = uiState.trackingState
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val granted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                cameraPermissionGranted = granted
                viewModel.onCameraPermissionResult(granted)
                viewModel.recheckArCoreAfterResume()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(uiState.needsArCoreInstall) {
        if (!uiState.needsArCoreInstall) return@LaunchedEffect
        val activity = context as? ComponentActivity ?: return@LaunchedEffect
        try {
            when (ArCoreApk.getInstance().requestInstall(activity, true)) {
                ArCoreApk.InstallStatus.INSTALLED -> viewModel.recheckArCoreAfterResume()
                ArCoreApk.InstallStatus.INSTALL_REQUESTED -> Unit
            }
        } catch (_: UnavailableUserDeclinedInstallationException) {
            viewModel.enableFallbackStaticView()
        } catch (_: UnavailableDeviceNotCompatibleException) {
            viewModel.enableFallbackStaticView()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        cameraPermissionGranted = granted
        viewModel.onCameraPermissionResult(granted)
    }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        cameraPermissionGranted = granted
        if (granted) {
            viewModel.onCameraPermissionResult(true)
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(uiState.referenceImagePath) {
        val path = uiState.referenceImagePath ?: return@LaunchedEffect
        referenceBitmap = withContext(Dispatchers.IO) {
            runCatching {
                context.assets.open(path).use { BitmapFactory.decodeStream(it) }
            }.onFailure {
                ArDebugLog.error("Referans bitmap yüklenemedi: $path", it)
            }.getOrNull()
        }
        val bitmap = referenceBitmap
        if (bitmap != null) {
            ArDebugLog.log("Referans bitmap yüklendi: $path → ${bitmap.width}x${bitmap.height}")
        } else {
            ArDebugLog.error("Referans bitmap NULL: $path")
        }
    }

    val favoriteAddedMsg = stringResource(R.string.place_detail_favorite_added)
    val favoriteRemovedMsg = stringResource(R.string.place_detail_favorite_removed)
    val actionFailedMsg = stringResource(R.string.place_detail_action_failed)
    val pastInfo = stringResource(R.string.ar_past_info)
    val pastUnavailableMsg = stringResource(R.string.ar_past_unavailable)

    LaunchedEffect(uiState.message) {
        val message = uiState.message ?: return@LaunchedEffect
        val text = when (message) {
            ArMessage.FAVORITE_ADDED -> favoriteAddedMsg
            ArMessage.FAVORITE_REMOVED -> favoriteRemovedMsg
            ArMessage.ACTION_FAILED -> actionFailedMsg
        }
        snackbarHostState.showSnackbar(text)
        viewModel.consumeMessage()
    }

    val cream = HarputColors.Cream.copy(alpha = 0.92f)
    val darkBrown = HarputColors.DarkBrown
    val softBrown = HarputColors.SoftBrown
    val panelColor = Color(0xFF3D2618).copy(alpha = 0.88f)
    val footerColor = Color(0xFF2E1A10).copy(alpha = 0.75f)
    val iconBrown = HarputColors.DarkBrown
    val hotspotPanelColor = Color(0xFF2A1810).copy(alpha = 0.92f)
    val pastControlCardColor = Color(0xFF2A1810).copy(alpha = 0.92f)

    val selectedHotspot = structureHotspots.find { it.id == selectedHotspotId }
    val isTrackingActive = uiState.trackingState.isAlignedEnough()
    val useFallback = uiState.useFallbackStaticView
    val isContentReady = arContent != null && !uiState.isLoading
    val isBitmapReady = referenceBitmap != null
    val isWaitingForBitmap = isContentReady && uiState.referenceImagePath != null && !isBitmapReady
    val showStaticFallback = useFallback && isBitmapReady && isContentReady
    val runArCoreSession = uiState.isArSupported &&
        cameraPermissionGranted &&
        isBitmapReady &&
        !useFallback &&
        !uiState.needsArCoreInstall
    val canShowContent = isContentReady && when {
        runArCoreSession -> true
        showStaticFallback -> true
        else -> false
    }
    val showStructureHotspotsOnStatic = selectedMode == ArMode.Structure &&
        showStaticFallback &&
        structureHotspots.isNotEmpty()
    val showArTrackingLayer = selectedMode == ArMode.Structure && runArCoreSession
    val showStructureHotspotsOnCamera = showArTrackingLayer &&
        hotspotsRevealed &&
        structureHotspots.isNotEmpty()

    LaunchedEffect(placeId, runArCoreSession, selectedMode) {
        hotspotsRevealed = false
        useAssistedPlacement = false
        selectedHotspotId = null
    }

    LaunchedEffect(placeId, pastOverlayAvailable) {
        if (selectedMode == ArMode.Past && !pastOverlayAvailable) {
            selectedMode = ArMode.Structure
        }
    }

    LaunchedEffect(uiState.trackingState, runArCoreSession) {
        if (!runArCoreSession || hotspotsRevealed) return@LaunchedEffect
        if (!uiState.trackingState.isAlignedEnough()) return@LaunchedEffect
        delay(ArHotspotReveal.DELAY_MS)
        if (runArCoreSession && !hotspotsRevealed && uiState.trackingState.isAlignedEnough()) {
            hotspotsRevealed = true
            useAssistedPlacement = false
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            arFeedback.playHotspotsRevealed()
            ArDebugLog.log(
                "hotspotsRevealed=aligned placeId=$placeId state=${uiState.trackingState} " +
                    "rect=${uiState.trackedImageScreenRect}"
            )
        }
    }

    LaunchedEffect(runArCoreSession, placeId, selectedMode) {
        if (!runArCoreSession || selectedMode != ArMode.Structure) return@LaunchedEffect
        delay(ArHotspotReveal.ASSISTED_FALLBACK_MS)
        if (runArCoreSession && selectedMode == ArMode.Structure && !hotspotsRevealed) {
            hotspotsRevealed = true
            useAssistedPlacement = true
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            arFeedback.playHotspotsRevealed()
            ArDebugLog.log("hotspotsRevealed=assisted placeId=$placeId (fit bounds fallback)")
        }
    }

    val onStructureHotspotClick: (String) -> Unit = { id ->
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        arFeedback.playHotspotSelected()
        selectedHotspotId = null
        onHotspotDetailClick(placeId, id)
    }

    LaunchedEffect(runArCoreSession) {
        if (runArCoreSession && !cameraReadyFeedbackPlayed) {
            arFeedback.playCameraReady()
            cameraReadyFeedbackPlayed = true
        }
    }

    LaunchedEffect(
        placeId,
        selectedMode,
        runArCoreSession,
        showStaticFallback,
        showStructureHotspotsOnCamera,
        isTrackingActive,
        uiState.trackingState,
        structureHotspots.size,
        uiState.trackedImageScreenRect
    ) {
        ArDebugLog.log(
            "ARScreen placeId=$placeId mode=$selectedMode tracking=${uiState.trackingState} " +
                "hotspots=${structureHotspots.size} runSession=$runArCoreSession " +
                "revealed=$hotspotsRevealed assisted=$useAssistedPlacement " +
                "showCameraHotspots=$showStructureHotspotsOnCamera " +
                "staticFallback=$showStaticFallback rect=${uiState.trackedImageScreenRect}"
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            runArCoreSession -> {
                key(uiState.referenceImagePath) {
                    AugmentedImageArView(
                        referenceBitmap = referenceBitmap!!,
                        physicalWidthMeters = uiState.physicalWidthMeters,
                        onTrackingUpdate = viewModel::onTrackingUpdate,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            showStaticFallback && referenceBitmap != null -> {
                ArReferenceImageLayer(
                    bitmap = referenceBitmap!!,
                    hotspots = structureHotspots,
                    selectedHotspotId = selectedHotspotId,
                    onHotspotClick = onStructureHotspotClick,
                    showHotspots = showStructureHotspotsOnStatic,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                )
            }
        }

        if (!runArCoreSession && (uiState.isLoading || isWaitingForBitmap || uiState.needsArCoreInstall)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        if (selectedMode == ArMode.Past && canShowContent && pastOverlayAvailable && pastOverlayPath != null) {
            PastViewOverlay(
                sliderValue = pastSliderValue,
                pastImageAssetPath = pastOverlayPath,
                modifier = Modifier.fillMaxSize()
            )
        }

        val vignetteAlpha = if (selectedMode == ArMode.Structure && runArCoreSession) {
            if (isTrackingActive) 0.22f else 0.38f
        } else if (selectedMode != ArMode.Structure) {
            0.35f
        } else {
            0f
        }
        if (vignetteAlpha > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(2f)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = vignetteAlpha)
                            ),
                            radius = 900f
                        )
                    )
            )
        }

        if (runArCoreSession && canShowContent && selectedMode == ArMode.Structure) {
            ArTrackingStatusChip(
                trackingState = uiState.trackingState,
                hotspotsRevealed = hotspotsRevealed,
                useAssistedPlacement = useAssistedPlacement,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(18f)
                    .padding(bottom = 196.dp, start = 24.dp, end = 24.dp)
            )
        }

        if (useFallback && (
            uiState.trackingState == ArTrackingState.UNSUPPORTED ||
            uiState.trackingState == ArTrackingState.PERMISSION_DENIED
        )) {
            ArTrackingGuideBanner(
                trackingState = uiState.trackingState,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 24.dp)
            )
        }

        if (selectedMode == ArMode.Structure && showStaticFallback &&
            selectedHotspot == null && canShowContent
        ) {
            Text(
                text = stringResource(R.string.ar_structure_hint),
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 180.dp, start = 24.dp, end = 24.dp)
            )
        }

        if (runArCoreSession && canShowContent) {
            ArCameraHudOverlay(
                trackingState = uiState.trackingState,
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(11f)
            )
        }

        if (showArTrackingLayer) {
            ArCameraHotspotOverlay(
                hotspots = structureHotspots,
                referenceBitmap = referenceBitmap,
                trackingState = uiState.trackingState,
                trackedRect = uiState.trackedImageScreenRect,
                selectedHotspotId = selectedHotspotId,
                onHotspotClick = onStructureHotspotClick,
                showHotspots = hotspotsRevealed,
                useAssistedPlacement = useAssistedPlacement,
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(10f)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .zIndex(20f)
        ) {
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
                        contentDescription = stringResource(R.string.cd_back),
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                when (selectedMode) {
                    ArMode.Structure -> {
                        ArModeTitlePill(title = stringResource(R.string.ar_structure_title))
                    }
                    ArMode.Past -> {
                        PastViewInfoBadge(
                            title = stringResource(R.string.ar_past_title),
                            onInfoClick = {
                                scope.launch { snackbarHostState.showSnackbar(pastInfo) }
                            }
                        )
                    }
                    else -> Spacer(modifier = Modifier.size(42.dp))
                }

                ArCircleButton(onClick = { viewModel.toggleFavorite() }) {
                    Icon(
                        imageVector = if (uiState.isFavorite) Icons.Filled.Favorite
                        else Icons.Filled.FavoriteBorder,
                        contentDescription = stringResource(R.string.cd_favorite),
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            when (selectedMode) {
                ArMode.Info -> {
                    if (canShowContent) {
                        ArInfoOverlayCard(
                            title = infoTitle,
                            location = infoLocation,
                            description = infoDescription,
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
                }
                else -> Spacer(modifier = Modifier.size(4.dp))
            }

        }

        if (!(selectedMode == ArMode.Structure && showStaticFallback)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(12f)
            ) {
                when (selectedMode) {
                    ArMode.Structure -> Unit
                    ArMode.Info -> {
                        if (canShowContent) {
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
                    else -> Unit
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .zIndex(20f)
        ) {

            if (selectedMode == ArMode.Past && canShowContent && pastOverlayAvailable) {
                PastViewControlCard(
                    sliderValue = pastSliderValue,
                    onSliderValueChange = { pastSliderValue = it },
                    cardColor = pastControlCardColor,
                    textColor = HarputColors.Cream,
                    accentColor = Color(0xFFD4A574),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            ArModeBottomPanel(
                selectedMode = selectedMode,
                onModeSelected = { mode ->
                    if (mode == ArMode.Past && !pastOverlayAvailable) {
                        scope.launch { snackbarHostState.showSnackbar(pastUnavailableMsg) }
                        return@ArModeBottomPanel
                    }
                    selectedMode = if (selectedMode == mode) ArMode.Structure else mode
                    if (selectedMode != ArMode.Structure) selectedHotspotId = null
                },
                onPlacesClick = onPlacesClick,
                panelColor = panelColor,
                activeColor = Color.White.copy(alpha = 0.25f),
                inactiveColor = Color.White.copy(alpha = 0.12f),
                labelColor = HarputColors.Cream,
                footerColor = footerColor,
                showVisitFooter = selectedMode != ArMode.Past
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
private fun ArTrackingGuideBanner(
    trackingState: ArTrackingState,
    modifier: Modifier = Modifier
) {
    val message = when (trackingState) {
        ArTrackingState.SEARCHING -> stringResource(R.string.ar_tracking_searching)
        ArTrackingState.LOST -> stringResource(R.string.ar_tracking_lost)
        ArTrackingState.PERMISSION_DENIED -> stringResource(R.string.ar_permission_denied)
        ArTrackingState.UNSUPPORTED -> stringResource(R.string.ar_unsupported)
        else -> stringResource(R.string.ar_point_reference_hint)
    }
    Text(
        text = message,
        color = Color.White,
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.72f))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
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
            text = stringResource(R.string.ar_help_hint),
            color = Color.White,
            fontSize = 12.sp
        )
    }
}
