package com.closedcircuit.closedcircuitapplication.presentation.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

inline fun Modifier.conditional(
    condition: Boolean,
    ifTrue: Modifier.() -> Modifier,
    ifFalse: Modifier.() -> Modifier = { this },
): Modifier = if (condition) {
    then(ifTrue(Modifier))
} else {
    then(ifFalse(Modifier))
}

inline fun Modifier.justPadding(all: Dp) =
    this.then(Modifier.background(Color.Unspecified).padding(all))

inline fun Modifier.justPadding(horizontal: Dp, vertical: Dp) =
    this.then(Modifier.background(Color.Unspecified).padding(horizontal, vertical))