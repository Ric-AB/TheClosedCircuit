package com.closedcircuit.closedcircuitapplication.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.closedcircuit.closedcircuitapplication.EntryPoint
import com.closedcircuit.closedcircuitapplication.common.presentation.LocalImagePicker
import com.closedcircuit.closedcircuitapplication.common.presentation.LocalShareHandler
import com.closedcircuit.closedcircuitapplication.common.presentation.util.AndroidShareHandler
import com.closedcircuit.closedcircuitapplication.common.presentation.util.ImagePickerFactory
import com.closedcircuit.closedcircuitapplication.core.storage.storageDir
import com.google.firebase.Firebase
import com.google.firebase.initialize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        storageDir = filesDir.path
        Firebase.initialize(applicationContext)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val appLinkIntent: Intent = intent
        val appLinkData: Uri? = appLinkIntent.data
        val planId = appLinkData?.lastPathSegment
        setContent {
            CompositionLocalProvider(
                LocalImagePicker provides ImagePickerFactory().createPicker(),
                LocalShareHandler provides AndroidShareHandler(LocalContext.current as ComponentActivity)
            ) {
                EntryPoint(
                    useDarkTheme = isSystemInDarkTheme(),
                    dynamicColors = false,
                    planId = planId
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }
}
