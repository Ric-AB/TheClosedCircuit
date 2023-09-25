package com.closedcircuit.closedcircuitapplication.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.DpSize
import com.closedcircuit.closedcircuitapplication.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.presentation.theme.Elevation

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    avatar: Avatar,
    shape: Shape = CircleShape,
    size: DpSize
) {
    OutlinedCard(
        modifier = modifier.size(size),
        shape = shape,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(Elevation.Level1)
        )
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