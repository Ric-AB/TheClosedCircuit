package com.closedcircuit.closedcircuitapplication.common.presentation.components.table

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.util.TypeWithStringProperties

/**
 * @param data The list of data items to display in the table.
 * @param enableTableHeaderTitles show or hide the table header titles. If not set, by default the table header titles will be shown.
 * @param headerTableTitles The list of header titles to display at the top of the table.
 * @param headerTitlesBorderColor The color of the border for the header titles, by default it will be [Color.LightGray].
 * @param headerTitlesTextStyle The text style to apply to the header titles, by default it will be [MaterialTheme.typography.bodySmall].
 * @param headerTitlesBackGroundColor The background color for the header titles, by default it will be [Color.White].
 * @param tableRowColors The list of background colors to alternate between rows in the table, by default it will be a list of: [Color.White], [Color.White].
 * @param rowBorderColor The color of the border for the table rows, by default it will be [Color.LightGray].
 * @param rowTextStyle The text style to apply to the data cells in the table rows, by default it will be [MaterialTheme.typography.bodySmall].
 * @param tableElevation The elevation of the entire table (Card elevation) in DP, by default it will be "6.dp".
 * @param shape The shape of the table's corners, by default it will be "RoundedCornerShape(4.dp)".
 * @param disableVerticalDividers show or hide the vertical dividers between the table cells. If not set, by default the vertical dividers will be shown.
 * @param horizontalDividerColor The color of the horizontal dividers, by default it will be [Color.LightGray]. Note: This will only be visible if [disableVerticalDividers] is set to true.
 * @param contentAlignment The alignment of the content in the table cells, by default it will be [Alignment.Center].
 * @param textAlign The alignment of the text in the table cells, by default it will be [TextAlign.Center].
 */
@Composable
inline fun <reified T : TypeWithStringProperties> Table(
    data: List<T>,
    enableTableHeaderTitles: Boolean = true,
    headerTableTitles: List<String>,
    headerTitlesBorderColor: Color = Color.LightGray,
    headerTitlesTextStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
    headerTitlesBackGroundColor: Color = Color.White,
    tableRowColors: List<Color> = listOf(Color.White, Color.White),
    rowBorderColor: Color = Color.LightGray,
    rowTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    tableElevation: Dp = 0.dp,
    shape: Shape = Shapes().medium,
    borderStroke: BorderStroke = BorderStroke(
        width = 1.dp,
        color = Color.LightGray,
    ),
    disableVerticalDividers: Boolean = false,
    dividerThickness: Dp = 1.dp,
    horizontalDividerColor: Color = Color.LightGray,
    contentAlignment: Alignment = Alignment.Center,
    textAlign: TextAlign = TextAlign.Center,
    tablePadding: Dp = 0.dp,
    columnToIndexIncreaseWidth: Int? = null,
) {
    OutlinedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = tableElevation),
        shape = shape,
        border = borderStroke,
    ) {
        Column {
            if (enableTableHeaderTitles) {
                if (disableVerticalDividers) {
                    TableHeaderComponentWithoutColumnDividers(
                        headerTableTitles = headerTableTitles,
                        headerTitlesTextStyle = headerTitlesTextStyle,
                        headerTitlesBackGroundColor = headerTitlesBackGroundColor,
                        dividerThickness = dividerThickness,
                        contentAlignment = contentAlignment,
                        textAlign = textAlign,
                        tablePadding = tablePadding,
                        columnToIndexIncreaseWidth = columnToIndexIncreaseWidth,
                    )
                } else {
                    TableHeaderComponent(
                        headerTableTitles = headerTableTitles,
                        headerTitlesBorderColor = headerTitlesBorderColor,
                        headerTitlesTextStyle = headerTitlesTextStyle,
                        headerTitlesBackGroundColor = headerTitlesBackGroundColor,
                        contentAlignment = contentAlignment,
                        textAlign = textAlign,
                        tablePadding = tablePadding,
                        dividerThickness = dividerThickness,
                        columnToIndexIncreaseWidth = columnToIndexIncreaseWidth,
                    )
                }
            }

            data.forEachIndexed { index, data ->
                val rowData = data.properties

                // alternate background colors between rows
                val tableRowBackgroundColor = if (index % 2 == 0) {
                    tableRowColors[0]
                } else {
                    tableRowColors[1]
                }

                if (disableVerticalDividers) {
                    TableRowComponentWithoutDividers(
                        data = rowData,
                        rowTextStyle = rowTextStyle,
                        rowBackGroundColor = tableRowBackgroundColor,
                        dividerThickness = dividerThickness,
                        horizontalDividerColor = horizontalDividerColor,
                        contentAlignment = contentAlignment,
                        textAlign = textAlign,
                        tablePadding = tablePadding,
                        columnToIndexIncreaseWidth = columnToIndexIncreaseWidth,
                    )
                } else {
                    TableRowComponent(
                        data = rowData,
                        rowBorderColor = rowBorderColor,
                        dividerThickness = dividerThickness,
                        rowTextStyle = rowTextStyle,
                        rowBackGroundColor = tableRowBackgroundColor,
                        contentAlignment = contentAlignment,
                        textAlign = textAlign,
                        tablePadding = tablePadding,
                        columnToIndexIncreaseWidth = columnToIndexIncreaseWidth,
                    )
                }
            }
        }
    }
}