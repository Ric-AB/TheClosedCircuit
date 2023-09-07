package com.closedcircuit.closedcircuitapplication

import androidx.compose.ui.window.ComposeUIViewController
import com.closedcircuit.closedcircuitapplication.core.storage.storageDir
import platform.Foundation.NSHomeDirectory
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle


fun MainViewController() = ComposeUIViewController {
    storageDir = NSHomeDirectory()
    val isDarkTheme =
        UIScreen.mainScreen.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
    EntryPoint(useDarkTheme = isDarkTheme, dynamicColors = false)
}