package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.navigation.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.ui.unit.IntOffset
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent.Pop
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.serialization.Serializable

@Serializable
object SlideOverTransition : CustomScreenTransition {
    //Fixme ugly transition
    override fun screenTransition(
        scope: AnimatedContentTransitionScope<Screen>,
        navigator: Navigator
    ): ContentTransform {
        val (initialOffset, targetOffset) = when (navigator.lastEvent) {
            Pop -> ({ size: Int -> -size }) to ({ size: Int -> size })
            else -> ({ size: Int -> size }) to ({ size: Int -> -size })
        }

        val animationSpec = spring(
            stiffness = Spring.StiffnessMediumLow,
            visibilityThreshold = IntOffset.VisibilityThreshold
        )

        return slideInHorizontally(animationSpec, initialOffset) togetherWith
                slideOutHorizontally(animationSpec, targetOffset) +
                scaleOut(tween(300), targetScale = 0.7F)
    }
}