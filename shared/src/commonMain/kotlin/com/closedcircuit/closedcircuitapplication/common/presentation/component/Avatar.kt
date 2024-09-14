package com.closedcircuit.closedcircuitapplication.common.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.Elevation
import com.closedcircuit.closedcircuitapplication.common.presentation.util.rememberBitmapFromBytes
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    imageUrl: String,
    shape: Shape = CircleShape,
    size: DpSize,
    onEditClick: (() -> Unit)? = null
) {
    Box {
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
                if (imageUrl.isEmpty()) {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = "person",
                        modifier = Modifier.size(size.div(2)),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    CoilImage(
                        imageModel = { imageUrl },
                        imageOptions = ImageOptions(contentScale = ContentScale.Crop)
                    )
                }
            }
        }

        if (onEditClick != null) {
            Row(
                modifier = Modifier
                    .padding(end = 2.dp, bottom = 2.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .border(1.5.dp, MaterialTheme.colorScheme.inverseOnSurface, CircleShape)
                    .align(Alignment.BottomEnd)
                    .clickable { onEditClick.invoke() }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(4.dp).size(12.dp)
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