package com.closedcircuit.closedcircuitapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.lifecycle.NavigatorDisposable
import cafe.adriel.voyager.navigator.lifecycle.NavigatorLifecycleStore

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