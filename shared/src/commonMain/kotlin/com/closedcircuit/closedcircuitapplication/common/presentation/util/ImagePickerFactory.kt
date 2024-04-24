package com.closedcircuit.closedcircuitapplication.common.presentation.util

import androidx.compose.runtime.Composable
import com.closedcircuit.closedcircuitapplication.common.presentation.util.ImagePicker

expect class ImagePickerFactory {

    @Composable
    fun createPicker(): ImagePicker
}