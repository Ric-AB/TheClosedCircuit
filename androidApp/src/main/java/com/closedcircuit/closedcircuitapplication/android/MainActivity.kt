package com.closedcircuit.closedcircuitapplication.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.closedcircuit.closedcircuitapplication.EntryPoint
import com.closedcircuit.closedcircuitapplication.core.storage.storageDir
import com.closedcircuit.closedcircuitapplication.common.presentation.LocalImagePicker
import com.closedcircuit.closedcircuitapplication.common.presentation.util.ImagePickerFactory
import com.google.firebase.Firebase
import com.google.firebase.initialize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        storageDir = filesDir.path
        Firebase.initialize(applicationContext)
        setContent {
            CompositionLocalProvider(LocalImagePicker provides ImagePickerFactory().createPicker()) {
                EntryPoint(useDarkTheme = isSystemInDarkTheme(), dynamicColors = false)
            }
        }
    }
}
