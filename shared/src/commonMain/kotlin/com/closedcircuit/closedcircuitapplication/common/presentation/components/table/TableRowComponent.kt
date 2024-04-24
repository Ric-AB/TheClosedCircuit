package com.closedcircuit.closedcircuitapplication.common.presentation.components.table

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
fun TableRowComponent(
    data: List<String>,
    rowBorderColor: Color,
    rowTextStyle: TextStyle,
    rowBackGroundColor: Color,
    contentAlignment: Alignment,
    textAlign: TextAlign,
    tablePadding: Dp,
    columnToIndexIncreaseWidth: Int?,
    dividerThickness: Dp,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(rowBackGroundColor)
            .padding(tablePadding),
    ) {
        data.forEachIndexed { index, title ->
            val weight = if (index == columnToIndexIncreaseWidth) 8f else 2f
            Box(
                modifier = Modifier
                    .weight(weight)
                    .border(
                        width = dividerThickness,
                        color = rowBorderColor,
                    ),
                contentAlignment = contentAlignment,
            ) {
                Text(
                    text = title,
                    style = rowTextStyle,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .height(38.dp)
                        .wrapContentHeight()
                        .padding(end = 8.dp),
                    textAlign = textAlign,
                )
            }
        }
    }
}