package com.closedcircuit.closedcircuitapplication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.closedcircuit.closedcircuitapplication.navigation.AppNavigation
import com.closedcircuit.closedcircuitapplication.theme.AppTheme
import com.moriatsushi.insetsx.rememberWindowInsetsController

@Composable
fun EntryPoint(darkTheme: Boolean) {
    AppTheme(darkTheme) {
        AppNavigation()
    }
}

