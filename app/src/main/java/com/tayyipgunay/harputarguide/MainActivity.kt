package com.tayyipgunay.harputarguide

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tayyipgunay.harputarguide.core.design.theme.HarputARGuideTheme
import com.tayyipgunay.harputarguide.core.locale.AppLocaleManager
import com.tayyipgunay.harputarguide.core.navigation.AppNavGraph

class MainActivity : ComponentActivity() {

    private val localeManager: AppLocaleManager by lazy {
        AppLocaleManager.getInstance(applicationContext)
    }

    override fun attachBaseContext(newBase: Context) {
        val manager = AppLocaleManager.getInstance(newBase)
        super.attachBaseContext(manager.wrapContext(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HarputARGuideTheme {
                AppNavGraph(
                    localeManager = localeManager,
                    onAppLocaleChanged = { recreate() }
                )
            }
        }
    }
}
