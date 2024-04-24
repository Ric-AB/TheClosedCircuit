package com.closedcircuit.closedcircuitapplication

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.interop.LocalUIViewController
import androidx.compose.ui.window.ComposeUIViewController
import com.closedcircuit.closedcircuitapplication.core.storage.storageDir
import com.closedcircuit.closedcircuitapplication.common.presentation.LocalImagePicker
import com.closedcircuit.closedcircuitapplication.common.presentation.util.ImagePickerFactory
import platform.Foundation.NSHomeDirectory
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle


fun MainViewController() = ComposeUIViewController {
    storageDir = NSHomeDirectory()
    val isDarkTheme =
        UIScreen.mainScreen.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
    CompositionLocalProvider(LocalImagePicker provides ImagePickerFactory(LocalUIViewController.current).createPicker()) {
        EntryPoint(useDarkTheme = isDarkTheme, dynamicColors = false)
    }
}