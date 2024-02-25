package com.closedcircuit.closedcircuitapplication.presentation.util

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

abstract class BaseScreenModel<T> : ScreenModel {
    protected val _resultChannel = Channel<T>()
    val resultChannel: ReceiveChannel<T> = _resultChannel
}