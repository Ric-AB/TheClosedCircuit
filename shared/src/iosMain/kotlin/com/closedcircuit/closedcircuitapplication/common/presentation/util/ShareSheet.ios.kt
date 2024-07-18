package com.closedcircuit.closedcircuitapplication.common.presentation.util

import platform.UIKit.UIActivityTypeAddToReadingList
import platform.UIKit.UIActivityTypeAirDrop
import platform.UIKit.UIActivityTypeAssignToContact
import platform.UIKit.UIActivityTypePostToFlickr
import platform.UIKit.UIActivityTypePostToTencentWeibo
import platform.UIKit.UIActivityTypePostToVimeo
import platform.UIKit.UIActivityTypePostToWeibo
import platform.UIKit.UIActivityTypePrint
import platform.UIKit.UIActivityTypeSaveToCameraRoll
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIViewController

class IOSShareHandler(private val viewController: UIViewController) : ShareHandler {
    override fun sharePlanLink(text: String) {
        val items = listOf(text)
        val activityViewController =
            UIActivityViewController(activityItems = items, applicationActivities = null)
        activityViewController.excludedActivityTypes = listOf(
            UIActivityTypePostToWeibo,
            UIActivityTypePrint,
            UIActivityTypeAssignToContact,
            UIActivityTypeSaveToCameraRoll,
            UIActivityTypeAddToReadingList,
            UIActivityTypePostToFlickr,
            UIActivityTypePostToVimeo,
            UIActivityTypePostToTencentWeibo,
            UIActivityTypeAirDrop
        )

        viewController.presentViewController(
            activityViewController,
            animated = true,
            completion = null
        )
    }
}