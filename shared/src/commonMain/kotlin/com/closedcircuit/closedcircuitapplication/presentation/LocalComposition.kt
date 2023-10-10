package com.closedcircuit.closedcircuitapplication.presentation

import androidx.compose.runtime.staticCompositionLocalOf
import com.closedcircuit.closedcircuitapplication.presentation.util.ImagePicker

val LocalImagePicker = staticCompositionLocalOf<ImagePicker> {
    throw RuntimeException("Image picker not initialized yet")
}