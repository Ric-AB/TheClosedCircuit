package com.closedcircuit.closedcircuitapplication.presentation.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
    this then Modifier.background(Color.Unspecified).padding(all)

inline fun Modifier.justPadding(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) =
    this then Modifier.background(Color.Unspecified).padding(horizontal, vertical)

inline fun Modifier.justPadding(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp
) = this then Modifier.background(Color.Unspecified).padding(start, top, end, bottom)