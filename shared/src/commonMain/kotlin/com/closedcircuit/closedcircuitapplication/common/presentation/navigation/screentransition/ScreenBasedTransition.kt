package com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.transitions.ScreenTransitionContent

@Composable
fun ScreenBasedTransition(
    navigator: cafe.adriel.voyager.navigator.Navigator,
    modifier: Modifier = Modifier,
    defaultTransition: CustomScreenTransition = NoTransition,
    content: ScreenTransitionContent = { currentScreen ->
        currentScreen.Content()
    }
) {
    ScreenTransition(
        navigator = navigator,
        modifier = modifier,
        content = content,
        transition = {
            val transitionSource = when (navigator.lastEvent) {
                StackEvent.Pop, StackEvent.Replace -> initialState
                StackEvent.Idle, StackEvent.Push -> targetState
            }

            val screenTransition = (transitionSource as? CustomScreenTransition)
                ?.screenTransition(this, navigator)
                ?: defaultTransition.screenTransition(this, navigator)

            val stackSize = navigator.items.size
            screenTransition.targetContentZIndex = when (navigator.lastEvent) {
                StackEvent.Pop, StackEvent.Replace -> (stackSize - 1)
                StackEvent.Idle, StackEvent.Push -> stackSize
            }.toFloat()

            screenTransition
        }
    )
}

@Composable
fun ScreenTransition(
    navigator: cafe.adriel.voyager.navigator.Navigator,
    transition: AnimatedContentTransitionScope<Screen>.() -> ContentTransform,
    modifier: Modifier = Modifier,
    content: ScreenTransitionContent = { it.Content() }
) {
    AnimatedContent(
        targetState = navigator.lastItem,
        transitionSpec = transition,
        modifier = modifier
    ) { screen ->
        navigator.saveableState("transition", screen) {
            content(screen)
        }
    }
}