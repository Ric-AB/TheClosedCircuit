package com.closedcircuit.closedcircuitapplication.common.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.DarkColorScheme
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.LightColorScheme
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.getTypography

@Composable
actual fun AppTheme(
    useDarkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit
) {
//    val colors = if (!useDarkTheme) {
//        LightColorScheme
//    } else {
//        DarkColorScheme
//    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content,
        typography = getTypography()
    )
}