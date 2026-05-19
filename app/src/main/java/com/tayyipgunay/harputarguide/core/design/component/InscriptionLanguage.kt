package com.tayyipgunay.harputarguide.core.design.component

enum class InscriptionLanguage(val code: String) {
    TR("TR"),
    EN("EN"),
    AR("AR"),
    KU("KU")
}

object SampleInscriptionTexts {
    fun translation(language: InscriptionLanguage): String = when (language) {
        InscriptionLanguage.TR ->
            "Bu kalan yapı, iyi niyetli, değerli ve faziletli bey Hacı Yusuf oğlu Mehmed tarafından yaptırılmıştır. Hicri 1233 senesinde tamamlanmıştır."
        InscriptionLanguage.EN ->
            "This remaining structure was built by the benevolent, esteemed and virtuous lord Mehmed, son of Hacı Yusuf. It was completed in the Hijri year 1233."
        InscriptionLanguage.AR ->
            "بُني هذا البناء المتبقي على يد السيد المحسن الفاضل محمد بن الحاج يوسف. اكتمل في سنة ١٢٣٣ هجرية."
        InscriptionLanguage.KU ->
            "Ev bînayê mayî ji aliyê mêrê baş, bi nirx û bi fazîlet Mehmedê kurê Hacî Yusuf ve hat çêkirin. Di sala 1233'ê Hijrî de qediya."
    }

    fun hijriDate(language: InscriptionLanguage): String = when (language) {
        InscriptionLanguage.AR -> "١٢٣٣ هـ"
        else -> "1233 H."
    }
}
