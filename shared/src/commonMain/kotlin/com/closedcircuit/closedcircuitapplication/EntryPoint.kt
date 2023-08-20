package com.closedcircuit.closedcircuitapplication

import androidx.compose.runtime.Composable
import com.closedcircuit.closedcircuitapplication.presentation.navigation.AppNavigation
import com.closedcircuit.closedcircuitapplication.presentation.theme.AppTheme

@Composable
fun EntryPoint(useDarkTheme: Boolean, dynamicColors: Boolean) {
    AppTheme(useDarkTheme = useDarkTheme, dynamicColor = dynamicColors) {
        AppNavigation()
    }
}

