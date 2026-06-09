package com.tayyipgunay.harputarguide.feature.ar



import android.graphics.RectF

import com.google.ar.core.AugmentedImage

import com.google.ar.core.Camera

import com.google.ar.core.Frame

import com.google.ar.core.TrackingState

import kotlin.math.max

import kotlin.math.min



object AugmentedImageProjector {



    fun projectToScreenRect(

        image: AugmentedImage,

        frame: Frame,

        viewWidth: Int,

        viewHeight: Int

    ): RectF? {

        if (viewWidth <= 0 || viewHeight <= 0) return null

        if (image.trackingState != TrackingState.TRACKING &&

            image.trackingState != TrackingState.PAUSED

        ) {

            return null

        }



        val halfWidth = image.extentX / 2f

        val halfHeight = image.extentZ / 2f

        val localCorners = arrayOf(

            floatArrayOf(-halfWidth, 0f, -halfHeight),

            floatArrayOf(halfWidth, 0f, -halfHeight),

            floatArrayOf(halfWidth, 0f, halfHeight),

            floatArrayOf(-halfWidth, 0f, halfHeight)

        )



        val centerPose = image.centerPose

        val viewMatrix = FloatArray(16)

        val projectionMatrix = FloatArray(16)

        frame.camera.getViewMatrix(viewMatrix, 0)

        frame.camera.getProjectionMatrix(projectionMatrix, 0, 0.1f, 100f)



        var minX = Float.MAX_VALUE

        var minY = Float.MAX_VALUE

        var maxX = Float.MIN_VALUE

        var maxY = Float.MIN_VALUE

        var visibleCornerCount = 0



        localCorners.forEach { local ->

            val world = FloatArray(3)

            centerPose.transformPoint(local, 0, world, 0)

            val screen = worldToScreenPoint(

                world = world,

                viewMatrix = viewMatrix,

                projectionMatrix = projectionMatrix,

                viewWidth = viewWidth,

                viewHeight = viewHeight

            ) ?: return@forEach



            visibleCornerCount++

            minX = min(minX, screen[0])

            minY = min(minY, screen[1])

            maxX = max(maxX, screen[0])

            maxY = max(maxY, screen[1])

        }



        if (visibleCornerCount >= 2) {

            return clampRect(

                rect = RectF(minX, minY, maxX, maxY),

                viewWidth = viewWidth,

                viewHeight = viewHeight

            )

        }



        val centerWorld = floatArrayOf(

            centerPose.tx(),

            centerPose.ty(),

            centerPose.tz()

        )

        val centerScreen = worldToScreenPoint(

            world = centerWorld,

            viewMatrix = viewMatrix,

            projectionMatrix = projectionMatrix,

            viewWidth = viewWidth,

            viewHeight = viewHeight

        ) ?: return null



        val estimatedWidth = estimateScreenSpan(

            halfExtentMeters = halfWidth,

            world = centerWorld,

            viewMatrix = viewMatrix,

            projectionMatrix = projectionMatrix,

            viewWidth = viewWidth,

            viewHeight = viewHeight,

            horizontal = true

        )

        val estimatedHeight = estimateScreenSpan(

            halfExtentMeters = halfHeight,

            world = centerWorld,

            viewMatrix = viewMatrix,

            projectionMatrix = projectionMatrix,

            viewWidth = viewWidth,

            viewHeight = viewHeight,

            horizontal = false

        )



        ArDebugLog.log(

            "projectToScreenRect: köşe az ($visibleCornerCount) — merkez tahmini kullanılıyor"

        )

        return clampRect(

            rect = RectF(

                centerScreen[0] - estimatedWidth,

                centerScreen[1] - estimatedHeight,

                centerScreen[0] + estimatedWidth,

                centerScreen[1] + estimatedHeight

            ),

            viewWidth = viewWidth,

            viewHeight = viewHeight

        )

    }



    private fun estimateScreenSpan(

        halfExtentMeters: Float,

        world: FloatArray,

        viewMatrix: FloatArray,

        projectionMatrix: FloatArray,

        viewWidth: Int,

        viewHeight: Int,

        horizontal: Boolean

    ): Float {

        val offset = FloatArray(3)

        if (horizontal) {

            offset[0] = world[0] + halfExtentMeters

            offset[1] = world[1]

            offset[2] = world[2]

        } else {

            offset[0] = world[0]

            offset[1] = world[1]

            offset[2] = world[2] + halfExtentMeters

        }

        val center = worldToScreenPoint(world, viewMatrix, projectionMatrix, viewWidth, viewHeight)

        val edge = worldToScreenPoint(offset, viewMatrix, projectionMatrix, viewWidth, viewHeight)

        if (center == null || edge == null) {

            return min(viewWidth, viewHeight) * 0.28f

        }

        return max(

            kotlin.math.abs(edge[0] - center[0]),

            kotlin.math.abs(edge[1] - center[1])

        ).coerceAtLeast(min(viewWidth, viewHeight) * 0.18f)

    }



    private fun clampRect(rect: RectF, viewWidth: Int, viewHeight: Int): RectF {

        val minSize = min(viewWidth, viewHeight) * 0.22f

        var left = rect.left

        var top = rect.top

        var right = rect.right

        var bottom = rect.bottom



        if (right - left < minSize) {

            val cx = (left + right) / 2f

            left = cx - minSize / 2f

            right = cx + minSize / 2f

        }

        if (bottom - top < minSize) {

            val cy = (top + bottom) / 2f

            top = cy - minSize / 2f

            bottom = cy + minSize / 2f

        }



        return RectF(

            left.coerceIn(0f, viewWidth.toFloat()),

            top.coerceIn(0f, viewHeight.toFloat()),

            right.coerceIn(0f, viewWidth.toFloat()),

            bottom.coerceIn(0f, viewHeight.toFloat())

        )

    }



    private fun worldToScreenPoint(

        world: FloatArray,

        viewMatrix: FloatArray,

        projectionMatrix: FloatArray,

        viewWidth: Int,

        viewHeight: Int

    ): FloatArray? {

        val viewProjection = FloatArray(16)

        android.opengl.Matrix.multiplyMM(viewProjection, 0, projectionMatrix, 0, viewMatrix, 0)



        val worldHomogeneous = floatArrayOf(world[0], world[1], world[2], 1f)

        val clip = FloatArray(4)

        android.opengl.Matrix.multiplyMV(clip, 0, viewProjection, 0, worldHomogeneous, 0)



        if (clip[3] == 0f) return null



        val ndcX = clip[0] / clip[3]

        val ndcY = clip[1] / clip[3]

        if (ndcX < -2f || ndcX > 2f || ndcY < -2f || ndcY > 2f) return null



        val screenX = ((ndcX.coerceIn(-1.5f, 1.5f) + 1f) / 2f) * viewWidth

        val screenY = ((1f - ndcY.coerceIn(-1.5f, 1.5f)) / 2f) * viewHeight

        return floatArrayOf(screenX, screenY)

    }

}

