package com.tayyipgunay.harputarguide.feature.modelviewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.filament.View
import dev.romainguy.kotlin.math.Float3
import com.tayyipgunay.harputarguide.R
import com.tayyipgunay.harputarguide.core.design.theme.HarputColors
import io.github.sceneview.Scene
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironment
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode

private const val MODEL_ENVIRONMENT_IBL = "environments/neutral/neutral_ibl.ktx"
private const val MODEL_ENVIRONMENT_SKYBOX = "environments/neutral/neutral_skybox.ktx"

@Composable
fun GltfModelSceneView(
    modelAssetPath: String,
    modifier: Modifier = Modifier,
    darkBackground: Boolean = false,
    onLoadFailed: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (darkBackground) Color(0xFF2A2A2A) else HarputColors.CardLight)
    ) {
        key(modelAssetPath) {
            GltfModelSceneContent(
                modelAssetPath = modelAssetPath,
                onLoadFailed = onLoadFailed,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun GltfModelSceneContent(
    modelAssetPath: String,
    onLoadFailed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val environmentLoader = rememberEnvironmentLoader(engine)

    val modelInstance = remember(modelAssetPath) {
        runCatching {
            modelLoader.createModelInstance(assetFileLocation = modelAssetPath)
        }.getOrNull()
    }

    LaunchedEffect(modelInstance) {
        if (modelInstance == null) {
            onLoadFailed()
        }
    }

    if (modelInstance == null) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.model_viewer_load_failed),
                color = HarputColors.SoftBrown,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
        return
    }

    val centerNode = rememberNode(engine)
    val modelNode = remember(modelInstance) {
        ModelNode(
            modelInstance = modelInstance,
            autoAnimate = false,
            scaleToUnits = 1.0f,
            centerOrigin = Position(x = 0.0f, y = 0.0f, z = 0.0f)
        ).apply {
            isShadowCaster = true
            isShadowReceiver = true
        }
    }
    DisposableEffect(modelNode, centerNode) {
        centerNode.addChildNode(modelNode)
        onDispose {
            centerNode.removeChildNode(modelNode)
            modelNode.destroy()
        }
    }

    val cameraNode = rememberCameraNode(engine) {
        position = Position(x = 0.0f, y = 0.12f, z = 2.35f)
        lookAt(centerNode)
    }

    val environment = rememberEnvironment(environmentLoader) {
        environmentLoader.createKTX1Environment(
            iblAssetFile = MODEL_ENVIRONMENT_IBL,
            skyboxAssetFile = MODEL_ENVIRONMENT_SKYBOX
        )
    }

    val mainLightNode = rememberMainLightNode(engine) {
        intensity = 110_000.0f
        lightDirection = Float3(x = -0.35f, y = -1.0f, z = -0.25f)
        isShadowCaster = true
    }

    Scene(
        modifier = modifier,
        engine = engine,
        modelLoader = modelLoader,
        environmentLoader = environmentLoader,
        environment = environment,
        mainLightNode = mainLightNode,
        cameraNode = cameraNode,
        cameraManipulator = rememberCameraManipulator(
            orbitHomePosition = cameraNode.worldPosition,
            targetPosition = centerNode.worldPosition
        ),
        childNodes = listOf(centerNode),
        onViewCreated = {
            view.setShadowingEnabled(true)
            view.ambientOcclusionOptions = view.ambientOcclusionOptions.apply {
                enabled = true
                quality = View.QualityLevel.MEDIUM
            }
            view.bloomOptions = view.bloomOptions.apply {
                enabled = true
                strength = 0.12f
            }
        }
    )
}
