package com.hayidev.app

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.hayidev.app.ui.navigation.HayiDevNavHost
import com.hayidev.app.ui.theme.HayiDevTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var deepLinkUri: Uri? = null

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences(HayiDevApplication.PREFS_NAME, Context.MODE_PRIVATE)
        val savedLanguage = prefs.getString(HayiDevApplication.KEY_LANGUAGE, null)
        
        val locale = if (!savedLanguage.isNullOrEmpty()) {
            Locale(savedLanguage)
        } else {
            @Suppress("DEPRECATION")
            newBase.resources.configuration.locales[0]
        }
        
        Locale.setDefault(locale)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        val context = newBase.createConfigurationContext(config)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setupEdgeToEdge()
        handleDeepLink(intent)
        
        setContent {
            HayiDevTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HayiDevNavHost(
                        initialDeepLink = deepLinkUri
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        val data = intent?.data ?: return
        
        when {
            data.scheme == "hayidev" && data.host == "hayidev.app" -> {
                deepLinkUri = data
            }
            data.scheme == "https" && data.host == "www.hayidev.com" -> {
                deepLinkUri = data
            }
        }
    }

    private fun setupEdgeToEdge() {
        enableEdgeToEdge()
        
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        window.statusBarColor = android.graphics.Color.TRANSPARENT
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
