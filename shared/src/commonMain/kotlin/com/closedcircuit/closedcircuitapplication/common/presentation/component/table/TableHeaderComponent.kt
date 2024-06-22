package com.closedcircuit.closedcircuitapplication.common.presentation.component.table

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TableHeaderComponent(
    headerTableTitles: List<String>,
    headerTitlesBorderColor: Color,
    headerTitlesTextStyle: TextStyle,
    headerTitlesBackgroundColor: Color,
    contentAlignment: Alignment,
    textAlign: TextAlign,
    tablePadding: Dp,
    columnToIndexIncreaseWidth: Int?,
    dividerThickness: Dp,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(headerTitlesBackgroundColor)
            .padding(horizontal = tablePadding),
    ) {
        headerTableTitles.forEachIndexed { index, title ->
            val weight = if (index == columnToIndexIncreaseWidth) 8f else 2f
            Box(
                modifier = Modifier
                    .weight(weight)
                    .border(
                        width = dividerThickness,
                        color = headerTitlesBorderColor,
                    ),
                contentAlignment = contentAlignment,
            ) {
                Text(
                    text = title,
                    style = headerTitlesTextStyle,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .height(38.dp)
                        .wrapContentHeight(),
                    textAlign = textAlign,
                )
            }
        }
    }
}