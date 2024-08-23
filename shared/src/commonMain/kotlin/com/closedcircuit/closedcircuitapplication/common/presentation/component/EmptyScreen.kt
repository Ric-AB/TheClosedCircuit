package com.closedcircuit.closedcircuitapplication.common.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier.fillMaxHeight(0.7F).fillMaxWidth(),
    image: Painter = painterResource(SharedRes.images.ic_red_caution),
    title: String,
    message: String,
    action: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier.padding(horizontal = horizontalScreenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )

        Spacer(Modifier.height(16.dp))
        SubTitleText(text = title)

        Spacer(Modifier.height(8.dp))
        BodyText(text = message, textAlign = TextAlign.Center)

        Spacer(Modifier.height(20.dp))
        action?.invoke()
    }
}