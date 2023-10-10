package com.closedcircuit.closedcircuitapplication.presentation.util

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.closedcircuit.closedcircuitapplication.presentation.util.ImagePicker

actual class ImagePickerFactory {

    @Composable
    actual fun createPicker(): ImagePicker {
        val activity = LocalContext.current as ComponentActivity
        return remember(activity) {
            com.closedcircuit.closedcircuitapplication.presentation.util.ImagePicker(
                activity
            )
        }
    }
}