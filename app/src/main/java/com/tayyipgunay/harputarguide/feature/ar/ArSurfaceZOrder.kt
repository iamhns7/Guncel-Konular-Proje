package com.tayyipgunay.harputarguide.feature.ar

import android.opengl.GLSurfaceView
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup

internal object ArSurfaceZOrder {

    /** AR kamera yüzeyini Compose hotspot katmanının altına iter. */
    fun pushCameraBehindOverlays(root: View) {
        var adjusted = false
        visitViews(root) { view ->
            when (view) {
                is SurfaceView -> {
                    view.setZOrderOnTop(false)
                    view.setZOrderMediaOverlay(false)
                    adjusted = true
                }
                is GLSurfaceView -> {
                    view.setZOrderOnTop(false)
                    adjusted = true
                }
            }
        }
        ArDebugLog.log("ArSurfaceZOrder: cameraSurfaceAdjusted=$adjusted view=${root.javaClass.simpleName}")
    }

    private fun visitViews(view: View, block: (View) -> Unit) {
        block(view)
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                visitViews(view.getChildAt(i), block)
            }
        }
    }
}
