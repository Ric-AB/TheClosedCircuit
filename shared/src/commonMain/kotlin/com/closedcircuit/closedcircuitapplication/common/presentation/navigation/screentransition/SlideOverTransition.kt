package com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.ui.unit.IntOffset
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent.Pop
import cafe.adriel.voyager.navigator.Navigator

object SlideOverTransition : CustomScreenTransition {
    private const val SLIDING_SCALE = 1.2f
    override fun screenTransition(
        scope: AnimatedContentTransitionScope<Screen>,
        navigator: Navigator
    ): ContentTransform {
        val animationSpec = spring(
            stiffness = Spring.StiffnessMediumLow,
            visibilityThreshold = IntOffset.VisibilityThreshold,
        )

        return when (navigator.lastEvent) {
            Pop -> slideInHorizontally(animationSpec) { -it / 10 } +
                    scaleIn(initialScale = .9f) +
                    fadeIn(initialAlpha = .8f) togetherWith
                    slideOutHorizontally(animationSpec) { (it * SLIDING_SCALE).toInt() } +
                    scaleOut(targetScale = SLIDING_SCALE)

            else -> slideInHorizontally(animationSpec) { (it * SLIDING_SCALE).toInt() } +
                    scaleIn(initialScale = .5F) togetherWith
                    scaleOut(targetScale = .95f) +
                    fadeOut(targetAlpha = .8f) +
                    slideOutHorizontally { -it / 10 }
        }

    }
}