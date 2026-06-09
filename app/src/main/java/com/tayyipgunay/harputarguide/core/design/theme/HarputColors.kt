package com.tayyipgunay.harputarguide.core.design.theme

import androidx.compose.ui.graphics.Color

/**
 * Uygulama genelinde tekrar eden Harput tema renkleri için tek kaynak.
 * Ekranlardaki dağınık `Color(0xFF...)` tanımları bunun üzerinden referanslanır.
 */
object HarputColors {
    val Cream = Color(0xFFF5EBDD)
    val CardCream = Color(0xFFF8F1E6)
    val CardLight = Color(0xFFFDFBF7)
    val CardWarm = Color(0xFFFAF6F0)
    val DarkBrown = Color(0xFF4B2E1F)
    val DarkBrownText = Color(0xFF2D241E)
    val SoftBrown = Color(0xFF72533C)
    val BottomBarBg = Color(0xFFF0E4D4)
    val VisitedGreen = Color(0xFF5A8F4A)
    val Beige = Color(0xFFE4D3BD)
    val IconBg = Color(0xFFE8DFD0)
    val Divider = Color(0xFFD8C9B6)
    val FavoriteGold = Color(0xFFB8860B)
    val DeleteRed = Color(0xFF9B3D3D)

    /** Liste kartı görseli yüklenene kadar gösterilen degrade placeholder setleri. */
    val PlaceholderGradients: List<List<Color>> = listOf(
        listOf(Color(0xFFDCC7A8), Color(0xFFBEA07A)),
        listOf(Color(0xFFC9B59A), Color(0xFF9A7B5C)),
        listOf(Color(0xFFE0D0B8), Color(0xFFB8956E)),
        listOf(Color(0xFFD4C4AA), Color(0xFFA6845F))
    )
}
