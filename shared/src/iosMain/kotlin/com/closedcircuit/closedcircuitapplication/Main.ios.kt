package com.closedcircuit.closedcircuitapplication

import com.closedcircuit.closedcircuitapplication.core.storage.storageDir
import com.moriatsushi.insetsx.WindowInsetsUIViewController
import platform.Foundation.NSHomeDirectory
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle


fun MainViewController() = WindowInsetsUIViewController {
    storageDir = NSHomeDirectory()
    val isDarkTheme =
        UIScreen.mainScreen.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
    EntryPoint(isDarkTheme)
}