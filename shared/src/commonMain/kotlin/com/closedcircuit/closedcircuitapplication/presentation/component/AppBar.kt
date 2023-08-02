package com.closedcircuit.closedcircuitapplication.presentation.component

import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.closedcircuit.closedcircuitapplication.presentation.component.icon.rememberArrowLeftAlt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultAppBar(
    title: String = "",
    mainIcon: ImageVector = rememberArrowLeftAlt(),
    mainAction: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = mainAction) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        title = { Text(text = title, style = MaterialTheme.typography.titleLarge) }
    )
}