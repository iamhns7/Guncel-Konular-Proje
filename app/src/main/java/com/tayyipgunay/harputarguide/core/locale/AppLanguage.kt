package com.tayyipgunay.harputarguide.core.locale

object AppLanguage {
    const val TR = "tr"
    const val EN = "en"
    const val DEFAULT = TR

    val supported: List<String> = listOf(TR, EN)

    fun normalize(code: String?): String {
        val lower = code?.lowercase()?.takeIf { it.isNotBlank() } ?: return DEFAULT
        return if (lower in supported) lower else DEFAULT
    }
}
