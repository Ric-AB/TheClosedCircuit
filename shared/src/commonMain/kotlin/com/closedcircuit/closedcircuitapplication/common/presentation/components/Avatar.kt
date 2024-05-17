@file:OptIn(ExperimentalMaterial3Api::class)

package com.closedcircuit.closedcircuitapplication.common.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import com.closedcircuit.closedcircuitapplication.common.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.Elevation
import com.closedcircuit.closedcircuitapplication.common.presentation.util.rememberBitmapFromBytes

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    avatar: Avatar,
    shape: Shape = CircleShape,
    size: DpSize,
) {
    OutlinedCard(
        modifier = modifier.size(size),
        shape = shape,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(Elevation.Level1)
        ),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.CenterHorizontally)
        ) {
            if (avatar.isEmpty()) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = "person",
                    modifier = Modifier.size(size.div(2)),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun EditableAvatar(
    modifier: Modifier = Modifier,
    bytes: ByteArray?,
    shape: Shape = CircleShape,
    size: DpSize,
    onClick: () -> Unit
) {
    val bitmap = rememberBitmapFromBytes(bytes)
    OutlinedCard(
        modifier = modifier.size(size),
        shape = shape,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(Elevation.Level1)
        ),
        onClick = onClick
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.CenterHorizontally)
        ) {
            if (bitmap == null) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "add",
                    modifier = Modifier.size(size.div(2.5F)),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Image(
                    bitmap = bitmap,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(size),
                )
            }
        }
    }
}