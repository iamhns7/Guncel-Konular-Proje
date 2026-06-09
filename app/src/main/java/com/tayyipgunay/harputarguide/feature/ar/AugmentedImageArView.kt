package com.tayyipgunay.harputarguide.feature.ar

import android.graphics.Bitmap
import android.graphics.RectF
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.ar.core.AugmentedImage
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.TrackingState
import com.google.ar.core.exceptions.UnavailableApkTooOldException
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException
import com.google.ar.core.exceptions.UnavailableSdkTooOldException
import io.github.sceneview.ar.ARSceneView

@Composable
fun AugmentedImageArView(
    referenceBitmap: Bitmap,
    physicalWidthMeters: Float,
    onTrackingUpdate: (ArTrackingState, RectF?) -> Unit,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val arSceneViewHolder = remember { ArSceneViewHolder() }
    val debugState = remember { ArFrameDebugState() }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            arSceneViewHolder.sceneView?.destroy()
            arSceneViewHolder.sceneView = null
        }
    }

    AndroidView(
        modifier = modifier.zIndex(0f),
        factory = { context ->
            try {
                ArDebugLog.log(
                    "ARSceneView oluşturuluyor — bitmap=${referenceBitmap.width}x${referenceBitmap.height}, " +
                        "physicalWidth=${physicalWidthMeters}m"
                )
                ARSceneView(context).also { sceneView ->
                    arSceneViewHolder.sceneView = sceneView
                    sceneView.lifecycle = lifecycleOwner.lifecycle
                    sceneView.planeRenderer.isVisible = false
                    sceneView.post { ArSurfaceZOrder.pushCameraBehindOverlays(sceneView) }
                    sceneView.sessionConfiguration = { session: Session, config: Config ->
                        config.focusMode = Config.FocusMode.AUTO
                        config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
                        config.planeFindingMode = Config.PlaneFindingMode.DISABLED
                        config.lightEstimationMode = Config.LightEstimationMode.DISABLED
                        val database = AugmentedImageDatabase(session)
                        val index = database.addImage("reference", referenceBitmap, physicalWidthMeters)
                        if (index < 0) {
                            ArDebugLog.error(
                                "AugmentedImageDatabase.addImage BAŞARISIZ (index=$index). " +
                                    "Görsel çok büyük veya ARCore uyumsuz olabilir."
                            )
                        } else {
                            ArDebugLog.log("AugmentedImageDatabase.addImage OK — index=$index, name=reference")
                        }
                        config.augmentedImageDatabase = database
                        config.augmentedImageDatabase?.let {
                            ArDebugLog.log("AR session yapılandırıldı — db imageCount=${it.numImages}")
                        }
                    }
                    sceneView.onSessionUpdated = { session, frame ->
                        val updatedImages = frame.getUpdatedTrackables(AugmentedImage::class.java)
                        val allImages = session.getAllTrackables(AugmentedImage::class.java)
                        var state = ArTrackingState.SEARCHING
                        var trackedRect: RectF? = null

                        var bestAlignedImage: AugmentedImage? = null
                        for (image in allImages) {
                            when (image.trackingState) {
                                TrackingState.TRACKING -> {
                                    state = ArTrackingState.TRACKING
                                    bestAlignedImage = image
                                    break
                                }
                                TrackingState.PAUSED -> {
                                    if (state != ArTrackingState.TRACKING) {
                                        state = ArTrackingState.ALIGNED
                                        bestAlignedImage = image
                                    }
                                }
                                else -> Unit
                            }
                        }
                        if (bestAlignedImage != null && sceneView.width > 0 && sceneView.height > 0) {
                            trackedRect = AugmentedImageProjector.projectToScreenRect(
                                image = bestAlignedImage,
                                frame = frame,
                                viewWidth = sceneView.width,
                                viewHeight = sceneView.height
                            )
                        }

                        debugState.logFrame(
                            state = state,
                            updatedCount = updatedImages.size,
                            allCount = allImages.size,
                            viewWidth = sceneView.width,
                            viewHeight = sceneView.height,
                            trackedRect = trackedRect,
                            allImages = allImages
                        )
                        onTrackingUpdate(state, trackedRect)
                    }
                }
            } catch (ex: Exception) {
                ArDebugLog.error("ARSceneView oluşturulamadı", ex)
                val trackingState = when (ex) {
                    is UnavailableArcoreNotInstalledException,
                    is UnavailableApkTooOldException,
                    is UnavailableSdkTooOldException,
                    is UnavailableDeviceNotCompatibleException -> ArTrackingState.UNSUPPORTED
                    else -> ArTrackingState.UNSUPPORTED
                }
                onTrackingUpdate(trackingState, null)
                View(context)
            }
        }
    )
}

private class ArSceneViewHolder {
    var sceneView: ARSceneView? = null
}

private class ArFrameDebugState {
    private var lastLoggedState: ArTrackingState? = null
    private var frameCounter = 0

    fun logFrame(
        state: ArTrackingState,
        updatedCount: Int,
        allCount: Int,
        viewWidth: Int,
        viewHeight: Int,
        trackedRect: RectF?,
        allImages: Collection<AugmentedImage>
    ) {
        frameCounter++
        val stateChanged = state != lastLoggedState
        val periodic = frameCounter % 60 == 0
        if (!stateChanged && !periodic) return

        lastLoggedState = state
        val imageStates = allImages.joinToString { img ->
            "${img.name}:${img.trackingState}"
        }
        ArDebugLog.log(
            "frame#$frameCounter state=$state updated=$updatedCount all=$allCount " +
                "view=${viewWidth}x$viewHeight rect=$trackedRect images=[$imageStates]"
        )
    }
}
