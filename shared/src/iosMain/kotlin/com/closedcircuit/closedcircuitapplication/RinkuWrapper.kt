package com.closedcircuit.closedcircuitapplication

import dev.theolm.rinku.RinkuIos
import platform.Foundation.NSUserActivity

class RinkuWrapper {
    private val rinku = RinkuIos()

    fun onDeepLinkReceived(url: String) {
        rinku.onDeepLinkReceived(url)
    }

    fun onDeepLinkReceivedActivity(userActivity: NSUserActivity) {
        rinku.onDeepLinkReceived(userActivity)
    }
}