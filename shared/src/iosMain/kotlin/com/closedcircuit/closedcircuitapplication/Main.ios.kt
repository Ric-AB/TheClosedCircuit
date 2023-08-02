package com.closedcircuit.closedcircuitapplication

import com.closedcircuit.closedcircuitapplication.data.datasource.local.appSettingsStore
import com.closedcircuit.closedcircuitapplication.di.appSettingsStorage
import com.moriatsushi.insetsx.WindowInsetsUIViewController
import platform.Foundation.NSHomeDirectory
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle


fun MainViewController() = WindowInsetsUIViewController {
    appSettingsStorage = NSHomeDirectory()
    val isDarkTheme =
        UIScreen.mainScreen.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
    EntryPoint(isDarkTheme)
}