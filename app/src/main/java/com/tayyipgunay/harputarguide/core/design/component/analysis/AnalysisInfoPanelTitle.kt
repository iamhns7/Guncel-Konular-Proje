package com.tayyipgunay.harputarguide.core.design.component.analysis

import com.tayyipgunay.harputarguide.R

object AnalysisInfoPanelTitle {
    private val architecturalHotspots = setOf(
        "ana_burc",
        "sag_burc",
        "orta_burc",
        "ust_sur_hatti",
        "egri_minare",
        "basamaklar",
        "tunel_gecidi",
        "yapi_kalintisi",
        "giris_kemeri"
    )

    private val materialHotspots = setOf(
        "tas_duvar_dokusu",
        "tas_duvar",
        "seramik_kap_kup",
        "moloz_tas_duvar",
        "tugla_orgu",
        "kaya_oyma_yuzey"
    )

    fun resolveResId(
        contentType: String?,
        modelFileName: String,
        hotspotId: String? = null
    ): Int {
        val architectural = when {
            hotspotId in architecturalHotspots -> true
            hotspotId in materialHotspots -> false
            contentType == "material_analysis" -> false
            contentType == "history_slider" || contentType == "model_3d" -> true
            else -> when (modelFileName.lowercase()) {
                "tower-bastion.glb", "leaning-minaret.glb" -> true
                else -> false
            }
        }
        return if (architectural) {
            R.string.architectural_info_title
        } else {
            R.string.material_info_title
        }
    }
}
