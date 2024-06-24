package com.closedcircuit.closedcircuitapplication.sponsor.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.coil3.CoilImage

@Composable
fun PlanImage(modifier: Modifier = Modifier, imageUrl: String, shape: Shape) {
    CoilImage(
        imageModel = { imageUrl },
        modifier = modifier
            .clip(shape)
    )
}