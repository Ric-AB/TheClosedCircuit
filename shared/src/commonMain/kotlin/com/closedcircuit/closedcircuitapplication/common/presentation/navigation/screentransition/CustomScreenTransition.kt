package com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.closedcircuit.closedcircuitapplication.core.serialization.JavaSerializable

interface CustomScreenTransition: JavaSerializable {
    fun screenTransition(
        scope: AnimatedContentTransitionScope<Screen>,
        navigator: Navigator
    ): ContentTransform
}