package com.closedcircuit.closedcircuitapplication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.closedcircuit.closedcircuitapplication.presentation.navigation.AppNavigation
import com.closedcircuit.closedcircuitapplication.presentation.theme.AppTheme
import com.moriatsushi.insetsx.rememberWindowInsetsController

@Composable
fun EntryPoint(darkTheme: Boolean) {
    AppTheme(darkTheme) {
        AppNavigation()
    }
}

