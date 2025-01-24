package com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent.Pop
import cafe.adriel.voyager.navigator.Navigator

object SlideUpTransition : CustomScreenTransition {

    private val RISING_SCALE = 1.2f
    override fun screenTransition(
        scope: AnimatedContentTransitionScope<Screen>,
        navigator: Navigator
    ): ContentTransform =
        when (navigator.lastEvent) {
            Pop -> scaleIn(initialScale = .9f) +
                    fadeIn(initialAlpha = .8f) +
                    slideInVertically { -it / 10 } togetherWith
                    slideOutVertically { (it * RISING_SCALE).toInt() } +
                    scaleOut(targetScale = RISING_SCALE)

            else -> slideInVertically { (it * RISING_SCALE).toInt() } +
                    scaleIn(initialScale = RISING_SCALE) togetherWith
                    scaleOut(targetScale = .9f) +
                    fadeOut(targetAlpha = .8f) +
                    slideOutVertically { -it / 10 }
        }
}