package com.closedcircuit.closedcircuitapplication.common.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.closedcircuit.closedcircuitapplication.common.presentation.util.ImagePicker
import platform.UIKit.UIViewController

actual class ImagePickerFactory(
    private val rootController: UIViewController
) {

    @Composable
    actual fun createPicker(): ImagePicker {
        return remember {
            com.closedcircuit.closedcircuitapplication.common.presentation.util.ImagePicker(
                rootController
            )
        }
    }
}