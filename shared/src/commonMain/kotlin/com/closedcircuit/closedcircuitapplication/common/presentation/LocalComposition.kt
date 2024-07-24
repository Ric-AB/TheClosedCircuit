package com.closedcircuit.closedcircuitapplication.common.presentation

import androidx.compose.runtime.staticCompositionLocalOf
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.presentation.util.ImagePicker
import com.closedcircuit.closedcircuitapplication.common.presentation.util.ShareHandler

val LocalImagePicker = staticCompositionLocalOf<ImagePicker> {
    throw RuntimeException("Image picker not initialized yet")
}

val LocalShareHandler = staticCompositionLocalOf<ShareHandler> {
    throw RuntimeException("Share handler not initialized yet")
}

val LocalCurrency = staticCompositionLocalOf<Currency> {
    throw RuntimeException("Local Currency not initialized yet")
}