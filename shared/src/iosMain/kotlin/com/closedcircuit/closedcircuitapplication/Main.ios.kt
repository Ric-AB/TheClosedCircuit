package com.closedcircuit.closedcircuitapplication

import com.moriatsushi.insetsx.WindowInsetsUIViewController
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

fun MainViewController() = WindowInsetsUIViewController {
    val isDarkTheme =
        UIScreen.mainScreen.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
    EntryPoint(isDarkTheme)
}