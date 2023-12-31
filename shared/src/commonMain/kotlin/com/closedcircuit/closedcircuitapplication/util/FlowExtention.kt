package com.closedcircuit.closedcircuitapplication.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

@Composable
inline fun <reified T> Flow<T>.observerWithScreen(
    noinline action: suspend (T) -> Unit
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            this@observerWithScreen.cancellable().collectIndexed { _, value -> action(value) }
        }
    }
}