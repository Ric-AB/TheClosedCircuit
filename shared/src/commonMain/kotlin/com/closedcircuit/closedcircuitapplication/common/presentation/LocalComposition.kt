package com.closedcircuit.closedcircuitapplication.common.presentation

import androidx.compose.runtime.staticCompositionLocalOf
import com.closedcircuit.closedcircuitapplication.common.presentation.util.ImagePicker

val LocalImagePicker = staticCompositionLocalOf<ImagePicker> {
    throw RuntimeException("Image picker not initialized yet")
}