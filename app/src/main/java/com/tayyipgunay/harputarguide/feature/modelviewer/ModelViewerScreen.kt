package com.tayyipgunay.harputarguide.feature.modelviewer

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.tayyipgunay.harputarguide.core.design.component.ScreenSection

@Composable
fun ModelViewerScreen(
    placeId: String,
    onBack: () -> Unit
) {
    ScreenSection(
        title = "3D Model Görüntüleyici",
        description = "placeId: $placeId için 3D model ekranı burada olacak."
    ) {
        Button(onClick = onBack) {
            Text(text = "Geri Dön")
        }
    }
}
