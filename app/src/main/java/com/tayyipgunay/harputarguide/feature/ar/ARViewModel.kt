package com.tayyipgunay.harputarguide.feature.ar

import android.content.Context
import android.graphics.RectF
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ar.core.ArCoreApk
import com.tayyipgunay.harputarguide.core.common.AppLogger
import com.tayyipgunay.harputarguide.core.navigation.AppRoute
import com.tayyipgunay.harputarguide.domain.repository.ArContentRepository
import com.tayyipgunay.harputarguide.domain.repository.UserPlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ARViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val arContentRepository: ArContentRepository,
    private val userPlaceRepository: UserPlaceRepository
) : ViewModel() {

    private val placeId: String =
        savedStateHandle.get<String>(AppRoute.PLACE_ID_ARG).orEmpty()

    private val _uiState = MutableStateFlow(ArUiState())
    val uiState: StateFlow<ArUiState> = _uiState.asStateFlow()
    private var smoothedTrackedRect: RectF? = null

    init {
        loadArContent()
        validateAllArPackages()
        viewModelScope.launch {
            userPlaceRepository.observeIsFavorite(placeId).collect { favorite ->
                _uiState.update { it.copy(isFavorite = favorite) }
            }
        }
        viewModelScope.launch {
            userPlaceRepository.observeIsVisited(placeId).collect { visited ->
                _uiState.update { it.copy(isVisited = visited) }
            }
        }
        markVisited()
    }

    private fun loadArContent() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val supported = arContentRepository.isArSupported(placeId)
            val content = arContentRepository.getPlaceContent(placeId)
            val placeArEnabled = supported && content != null
            val arCoreStatus = evaluateArCoreStatus()
            ArDebugLog.log(
                "loadArContent placeId=$placeId supported=$supported content=${content != null} " +
                    "placeArEnabled=$placeArEnabled arCore=${arCoreStatus.canRunSession} " +
                    "needsInstall=${arCoreStatus.needsInstall} fallback=${arCoreStatus.useFallback} " +
                    "hotspots=${content?.hotspots?.size ?: 0} refPath=${content?.referenceImageAssetPath} " +
                    "physicalWidth=${content?.physicalWidthMeters}"
            )
            if (content != null && content.hotspots.isEmpty()) {
                ArDebugLog.warn("UYARI: $placeId için hotspot listesi BOŞ!")
            }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    placeArEnabled = placeArEnabled,
                    isArSupported = placeArEnabled && arCoreStatus.canRunSession,
                    needsArCoreInstall = arCoreStatus.needsInstall,
                    arContent = content,
                    hotspots = content?.hotspots.orEmpty(),
                    physicalWidthMeters = content?.physicalWidthMeters ?: 1f,
                    referenceImagePath = content?.referenceImageAssetPath,
                    useFallbackStaticView = content == null || !supported || arCoreStatus.useFallback,
                    trackingState = when {
                        content == null -> ArTrackingState.UNSUPPORTED
                        !supported -> ArTrackingState.UNSUPPORTED
                        arCoreStatus.useFallback -> ArTrackingState.UNSUPPORTED
                        else -> ArTrackingState.SEARCHING
                    }
                )
            }
        }
    }

    private fun validateAllArPackages() {
        viewModelScope.launch {
            val ids = arContentRepository.getSupportedPlaceIds()
            ArDebugLog.log("AR paket doğrulama — manifest'te ${ids.size} nokta: $ids")
            ids.forEach { id ->
                val content = arContentRepository.getPlaceContent(id)
                if (content == null) {
                    ArDebugLog.error("EKSIK: $id JSON okunamadı!")
                    return@forEach
                }
                ArDebugLog.log(
                    "OK: $id — hotspots=${content.hotspots.size}, " +
                        "ref=${content.referenceImageAssetPath}, width=${content.physicalWidthMeters}m"
                )
            }
        }
    }

    fun onCameraPermissionResult(granted: Boolean) {
        ArDebugLog.log("onCameraPermissionResult granted=$granted placeId=$placeId")
        _uiState.update {
            val arCoreStatus = evaluateArCoreStatus()
            val newTracking = when {
                it.arContent == null -> ArTrackingState.UNSUPPORTED
                arCoreStatus.useFallback -> ArTrackingState.UNSUPPORTED
                granted -> ArTrackingState.SEARCHING
                else -> ArTrackingState.PERMISSION_DENIED
            }
            it.copy(
                isArSupported = it.placeArEnabled && arCoreStatus.canRunSession,
                needsArCoreInstall = arCoreStatus.needsInstall,
                trackingState = newTracking,
                useFallbackStaticView = when {
                    it.arContent == null -> true
                    arCoreStatus.useFallback -> true
                    !granted -> true
                    else -> false
                }
            )
        }
    }

    fun onTrackingUpdate(state: ArTrackingState, screenRect: RectF?) {
        val previous = _uiState.value.trackingState
        val displayRect = when {
            state.isAlignedEnough() && screenRect != null -> {
                smoothedTrackedRect = smoothToward(smoothedTrackedRect, screenRect, factor = 0.32f)
                smoothedTrackedRect
            }
            state.isAlignedEnough() && smoothedTrackedRect != null -> {
                smoothedTrackedRect
            }
            else -> {
                smoothedTrackedRect = null
                null
            }
        }
        if (state != previous) {
            ArDebugLog.log(
                "trackingState $previous → $state rect=$screenRect smoothed=$displayRect placeId=$placeId"
            )
        }
        _uiState.update {
            it.copy(
                trackingState = state,
                trackedImageScreenRect = displayRect,
                useFallbackStaticView = state == ArTrackingState.UNSUPPORTED ||
                    state == ArTrackingState.PERMISSION_DENIED
            )
        }
    }

    private fun smoothToward(previous: RectF?, target: RectF, factor: Float): RectF {
        if (previous == null) return RectF(target)
        return RectF(
            lerp(previous.left, target.left, factor),
            lerp(previous.top, target.top, factor),
            lerp(previous.right, target.right, factor),
            lerp(previous.bottom, target.bottom, factor)
        )
    }

    private fun lerp(start: Float, end: Float, fraction: Float): Float =
        start + (end - start) * fraction.coerceIn(0f, 1f)

    fun recheckArCoreAfterResume() {
        val current = _uiState.value
        if (current.arContent == null) return

        val arCoreStatus = evaluateArCoreStatus()
        _uiState.update {
            it.copy(
                isArSupported = it.placeArEnabled && arCoreStatus.canRunSession,
                needsArCoreInstall = arCoreStatus.needsInstall,
                useFallbackStaticView = arCoreStatus.useFallback ||
                    it.trackingState == ArTrackingState.PERMISSION_DENIED,
                trackingState = when {
                    arCoreStatus.useFallback -> ArTrackingState.UNSUPPORTED
                    it.trackingState == ArTrackingState.PERMISSION_DENIED -> ArTrackingState.PERMISSION_DENIED
                    else -> ArTrackingState.SEARCHING
                }
            )
        }
    }

    fun enableFallbackStaticView() {
        _uiState.update { it.copy(useFallbackStaticView = true) }
    }

    private fun markVisited() {
        if (placeId.isBlank()) return
        viewModelScope.launch {
            runCatching { userPlaceRepository.setVisited(placeId, true) }
                .onFailure { AppLogger.e("AR: gezildi işaretlenemedi (placeId=$placeId).", it) }
        }
    }

    fun toggleFavorite() {
        if (placeId.isBlank()) return
        val target = !_uiState.value.isFavorite
        viewModelScope.launch {
            runCatching { userPlaceRepository.setFavorite(placeId, target) }
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            message = if (target) ArMessage.FAVORITE_ADDED
                            else ArMessage.FAVORITE_REMOVED
                        )
                    }
                }
                .onFailure {
                    AppLogger.e("AR: favori güncellenemedi (placeId=$placeId).", it)
                    _uiState.update { it.copy(message = ArMessage.ACTION_FAILED) }
                }
        }
    }

    fun consumeMessage() {
        _uiState.update { it.copy(message = null) }
    }

    private fun evaluateArCoreStatus(): ArCoreRuntimeStatus {
        return when (ArCoreApk.getInstance().checkAvailability(context)) {
            ArCoreApk.Availability.SUPPORTED_INSTALLED -> ArCoreRuntimeStatus(
                canRunSession = true,
                needsInstall = false,
                useFallback = false
            )
            ArCoreApk.Availability.SUPPORTED_NOT_INSTALLED,
            ArCoreApk.Availability.SUPPORTED_APK_TOO_OLD -> ArCoreRuntimeStatus(
                canRunSession = false,
                needsInstall = true,
                useFallback = false
            )
            else -> ArCoreRuntimeStatus(
                canRunSession = false,
                needsInstall = false,
                useFallback = true
            )
        }
    }

    private data class ArCoreRuntimeStatus(
        val canRunSession: Boolean,
        val needsInstall: Boolean,
        val useFallback: Boolean
    )
}
