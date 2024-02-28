package com.closedcircuit.closedcircuitapplication.presentation.util

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

abstract class BaseScreenModel<UiState, Result> : ScreenModel {
    protected val _resultChannel = Channel<Result>()
    val resultChannel: ReceiveChannel<Result> = _resultChannel


    @Composable
    open fun uiState(): UiState {
        throw RuntimeException("Can only be implemented")
    }
}