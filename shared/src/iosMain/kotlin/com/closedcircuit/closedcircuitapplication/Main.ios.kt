package com.closedcircuit.closedcircuitapplication

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.interop.LocalUIViewController
import androidx.compose.ui.window.ComposeUIViewController
import com.closedcircuit.closedcircuitapplication.common.presentation.LocalImagePicker
import com.closedcircuit.closedcircuitapplication.common.presentation.LocalShareHandler
import com.closedcircuit.closedcircuitapplication.common.presentation.util.IOSShareHandler
import com.closedcircuit.closedcircuitapplication.common.presentation.util.ImagePickerFactory
import com.closedcircuit.closedcircuitapplication.core.storage.storageDir
import io.github.xxfast.kstore.file.utils.DocumentDirectory
import io.github.xxfast.kstore.utils.ExperimentalKStoreApi
import platform.Foundation.NSFileManager
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle


@OptIn(ExperimentalKStoreApi::class)
fun MainViewController() = ComposeUIViewController {
    storageDir = NSFileManager.defaultManager.DocumentDirectory?.relativePath.orEmpty()
    val isDarkTheme =
        UIScreen.mainScreen.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
    CompositionLocalProvider(
        LocalImagePicker provides ImagePickerFactory(LocalUIViewController.current).createPicker(),
        LocalShareHandler provides IOSShareHandler(LocalUIViewController.current)
    ) {
        EntryPoint(useDarkTheme = isDarkTheme, dynamicColors = false)
    }
}