package com.closedcircuit.closedcircuitapplication.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.view.WindowCompat
import com.closedcircuit.closedcircuitapplication.EntryPoint
import com.closedcircuit.closedcircuitapplication.core.storage.Storage
import com.closedcircuit.closedcircuitapplication.core.storage.storageDir

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        storageDir = filesDir.path
        setContent {
            EntryPoint(useDarkTheme = isSystemInDarkTheme(), dynamicColors = true)
        }
    }
}
