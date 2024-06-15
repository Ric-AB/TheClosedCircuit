package com.closedcircuit.closedcircuitapplication.common.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.lifecycle.NavigatorDisposable
import cafe.adriel.voyager.navigator.lifecycle.NavigatorLifecycleStore
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun findRootNavigator(navigator: Navigator): Navigator {
    return if (navigator.parent == null) navigator
    else findRootNavigator(navigator.parent!!)
}

@OptIn(InternalVoyagerApi::class)
fun findNavigator(key: String, navigator: Navigator): Navigator {
    if (navigator.key == key) return navigator
    val parentNavigator = navigator.parent ?: return navigator
    return findNavigator(key, parentNavigator)
}

suspend fun Navigator.delayPush(screen: Screen, delay: Duration = 3.milliseconds) {
    delay(delay)
    push(screen)
}

suspend fun Navigator.delayReplaceAll(screen: Screen, delay: Duration = 3.milliseconds) {
    delay(delay)
    replaceAll(screen)
}

suspend fun Navigator.delayPop(delay: Duration = 3.milliseconds) {
    delay(delay)
    pop()
}

val Navigator.navigationResult: VoyagerResultExtension
    @Composable get() = remember {
        NavigatorLifecycleStore.get(this) {
            VoyagerResultExtension(this)
        }
    }

// OR

@Composable
fun rememberNavigationResultExtension(): VoyagerResultExtension {
    val navigator = LocalNavigator.currentOrThrow

    return remember {
        NavigatorLifecycleStore.get(navigator) {
            VoyagerResultExtension(navigator)
        }
    }
}

class VoyagerResultExtension(
    private val navigator: Navigator
) : NavigatorDisposable {
    private val results = mutableStateMapOf<String, Any?>()

    override fun onDispose(navigator: Navigator) {
        // not used
    }

    fun setResult(screenKey: String, result: Any?) {
        results[screenKey] = result
    }

    fun popWithResult(result: Any? = null) {
        val currentScreen = navigator.lastItem
        results[currentScreen.key] = result
        navigator.pop()
    }

    fun clearResults() {
        results.clear()
    }

    fun popUntilWithResult(predicate: (Screen) -> Boolean, result: Any? = null) {
        val currentScreen = navigator.lastItem
        results[currentScreen.key] = result
        navigator.popUntil(predicate)
    }

    @Composable
    fun <T> getResult(screenKey: String): State<T?> {
        val result = results[screenKey] as? T
        val resultState = remember(screenKey, result) {
            derivedStateOf {
                results.remove(screenKey)
                result
            }
        }
        return resultState
    }
}