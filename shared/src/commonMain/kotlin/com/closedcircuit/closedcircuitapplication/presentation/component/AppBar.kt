package com.closedcircuit.closedcircuitapplication.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultAppBar(
    title: String = "",
    mainIcon: ImageVector? = Icons.Default.ArrowBack,
    mainAction: () -> Unit = {}
) {
    TopAppBar(
        navigationIcon = {
            if (mainIcon != null) {
                IconButton(onClick = mainAction) {
                    Icon(imageVector = mainIcon, contentDescription = null)
                }
            }
        },
        title = { TopAppBarTitle(text = title) }
    )
}