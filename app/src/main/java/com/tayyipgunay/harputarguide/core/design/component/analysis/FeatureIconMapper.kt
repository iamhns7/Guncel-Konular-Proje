package com.tayyipgunay.harputarguide.core.design.component.analysis

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.ViewInAr
import androidx.compose.ui.graphics.vector.ImageVector

object FeatureIconMapper {
    fun iconFor(label: String): ImageVector {
        val normalized = label.lowercase()
        return when {
            normalized.contains("malzeme") || normalized.contains("bağlayıcı") -> Icons.Filled.Category
            normalized.contains("yapı") || normalized.contains("alan türü") || normalized.contains("eleman") -> Icons.Filled.Home
            normalized.contains("işlev") || normalized.contains("kullanım") || normalized.contains("savunma") -> Icons.Filled.Shield
            normalized.contains("konum") || normalized.contains("cephe") || normalized.contains("hatt") -> Icons.Filled.LocationOn
            normalized.contains("doku") || normalized.contains("örgü") || normalized.contains("ritim") -> Icons.Filled.GridOn
            normalized.contains("model") -> Icons.Outlined.ViewInAr
            normalized.contains("dayanık") || normalized.contains("özellik") || normalized.contains("etki") -> Icons.Filled.Star
            normalized.contains("teknik") || normalized.contains("teknik") -> Icons.Filled.Link
            normalized.contains("algı") || normalized.contains("yorum") -> Icons.Filled.Visibility
            else -> Icons.Filled.Info
        }
    }
}
