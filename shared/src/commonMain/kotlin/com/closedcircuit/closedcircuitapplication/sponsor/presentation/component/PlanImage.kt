package com.closedcircuit.closedcircuitapplication.sponsor.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage

@Composable
fun PlanImage(modifier: Modifier = Modifier, shape: Shape = Shapes().small, imageUrl: String) {
    CoilImage(
        imageModel = { imageUrl },
        modifier = modifier
            .clip(shape),
    )
}