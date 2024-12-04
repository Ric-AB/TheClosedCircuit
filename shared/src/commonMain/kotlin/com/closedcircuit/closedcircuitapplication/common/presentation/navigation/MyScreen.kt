package com.closedcircuit.closedcircuitapplication.common.presentation.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.serialization.Serializable

interface MyScreen : Screen {

    @get:Composable
    val title: String
}