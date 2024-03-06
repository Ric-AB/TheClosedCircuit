package com.closedcircuit.closedcircuitapplication.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier.fillMaxWidth(),
    height: Dp = 50.dp,
    shape: Shape = ButtonDefaults.shape,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        enabled = enabled
    ) {
        content()
    }
}

@Composable
fun DefaultOutlinedButton(
    modifier: Modifier = Modifier.fillMaxWidth(),
    shape: Shape = ButtonDefaults.shape,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        enabled = enabled
    ) {
        content()
    }
}