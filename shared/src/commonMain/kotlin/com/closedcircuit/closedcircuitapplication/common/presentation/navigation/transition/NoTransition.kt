package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.navigation.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent.Pop
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.serialization.Serializable

@Serializable
object NoTransition : CustomScreenTransition {

    override fun screenTransition(
        scope: AnimatedContentTransitionScope<Screen>,
        navigator: Navigator
    ): ContentTransform =
        when (navigator.lastEvent) {
            // Fade from 1 -> 99 so compose doesn't skip the animation and hides the screen instantly
            // see comment on [androidx.compose.animation.ExitTransition.None

            Pop -> EnterTransition.None togetherWith fadeOut(targetAlpha = .99f)
            else -> EnterTransition.None togetherWith fadeOut(targetAlpha = .99f)
        }
}