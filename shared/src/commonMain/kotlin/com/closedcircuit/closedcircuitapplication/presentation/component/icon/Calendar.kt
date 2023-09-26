package com.closedcircuit.closedcircuitapplication.presentation.component.icon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Composable
fun rememberCalendarMonth(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "calendar_month",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20f, 23.458f)
                quadToRelative(-0.625f, 0f, -1.042f, -0.437f)
                quadToRelative(-0.416f, -0.438f, -0.416f, -1.021f)
                quadToRelative(0f, -0.625f, 0.416f, -1.042f)
                quadToRelative(0.417f, -0.416f, 1.042f, -0.416f)
                reflectiveQuadToRelative(1.042f, 0.416f)
                quadToRelative(0.416f, 0.417f, 0.416f, 1.042f)
                quadToRelative(0f, 0.583f, -0.416f, 1.021f)
                quadToRelative(-0.417f, 0.437f, -1.042f, 0.437f)
                close()
                moveToRelative(-6.667f, 0f)
                quadToRelative(-0.625f, 0f, -1.041f, -0.437f)
                quadToRelative(-0.417f, -0.438f, -0.417f, -1.021f)
                quadToRelative(0f, -0.625f, 0.417f, -1.042f)
                quadToRelative(0.416f, -0.416f, 1.041f, -0.416f)
                reflectiveQuadToRelative(1.042f, 0.416f)
                quadToRelative(0.417f, 0.417f, 0.417f, 1.042f)
                quadToRelative(0f, 0.583f, -0.417f, 1.021f)
                quadToRelative(-0.417f, 0.437f, -1.042f, 0.437f)
                close()
                moveToRelative(13.334f, 0f)
                quadToRelative(-0.625f, 0f, -1.042f, -0.437f)
                quadToRelative(-0.417f, -0.438f, -0.417f, -1.021f)
                quadToRelative(0f, -0.625f, 0.417f, -1.042f)
                quadToRelative(0.417f, -0.416f, 1.042f, -0.416f)
                reflectiveQuadToRelative(1.041f, 0.416f)
                quadToRelative(0.417f, 0.417f, 0.417f, 1.042f)
                quadToRelative(0f, 0.583f, -0.417f, 1.021f)
                quadToRelative(-0.416f, 0.437f, -1.041f, 0.437f)
                close()
                moveTo(20f, 30f)
                quadToRelative(-0.625f, 0f, -1.042f, -0.438f)
                quadToRelative(-0.416f, -0.437f, -0.416f, -1.02f)
                quadToRelative(0f, -0.625f, 0.416f, -1.042f)
                quadToRelative(0.417f, -0.417f, 1.042f, -0.417f)
                reflectiveQuadToRelative(1.042f, 0.417f)
                quadToRelative(0.416f, 0.417f, 0.416f, 1.042f)
                quadToRelative(0f, 0.583f, -0.416f, 1.02f)
                quadTo(20.625f, 30f, 20f, 30f)
                close()
                moveToRelative(-6.667f, 0f)
                quadToRelative(-0.625f, 0f, -1.041f, -0.438f)
                quadToRelative(-0.417f, -0.437f, -0.417f, -1.02f)
                quadToRelative(0f, -0.625f, 0.417f, -1.042f)
                quadToRelative(0.416f, -0.417f, 1.041f, -0.417f)
                reflectiveQuadToRelative(1.042f, 0.417f)
                quadToRelative(0.417f, 0.417f, 0.417f, 1.042f)
                quadToRelative(0f, 0.583f, -0.417f, 1.02f)
                quadToRelative(-0.417f, 0.438f, -1.042f, 0.438f)
                close()
                moveToRelative(13.334f, 0f)
                quadToRelative(-0.625f, 0f, -1.042f, -0.438f)
                quadToRelative(-0.417f, -0.437f, -0.417f, -1.02f)
                quadToRelative(0f, -0.625f, 0.417f, -1.042f)
                quadToRelative(0.417f, -0.417f, 1.042f, -0.417f)
                reflectiveQuadToRelative(1.041f, 0.417f)
                quadToRelative(0.417f, 0.417f, 0.417f, 1.042f)
                quadToRelative(0f, 0.583f, -0.417f, 1.02f)
                quadToRelative(-0.416f, 0.438f, -1.041f, 0.438f)
                close()
                moveTo(8.5f, 35.625f)
                quadToRelative(-1f, 0f, -1.75f, -0.729f)
                reflectiveQuadTo(6f, 33.125f)
                verticalLineTo(9.75f)
                quadToRelative(0f, -1f, 0.75f, -1.75f)
                reflectiveQuadToRelative(1.75f, -0.75f)
                horizontalLineToRelative(2.958f)
                verticalLineTo(5.208f)
                quadToRelative(0f, -0.458f, 0.292f, -0.77f)
                quadToRelative(0.292f, -0.313f, 0.75f, -0.313f)
                reflectiveQuadToRelative(0.771f, 0.313f)
                quadToRelative(0.312f, 0.312f, 0.312f, 0.77f)
                verticalLineTo(7.25f)
                horizontalLineToRelative(12.875f)
                verticalLineTo(5.167f)
                quadToRelative(0f, -0.417f, 0.313f, -0.729f)
                quadToRelative(0.312f, -0.313f, 0.729f, -0.313f)
                quadToRelative(0.458f, 0f, 0.771f, 0.313f)
                quadToRelative(0.312f, 0.312f, 0.312f, 0.729f)
                verticalLineTo(7.25f)
                horizontalLineTo(31.5f)
                quadToRelative(1f, 0f, 1.75f, 0.75f)
                reflectiveQuadTo(34f, 9.75f)
                verticalLineToRelative(23.375f)
                quadToRelative(0f, 1.042f, -0.75f, 1.771f)
                quadToRelative(-0.75f, 0.729f, -1.75f, 0.729f)
                close()
                moveToRelative(0f, -2f)
                horizontalLineToRelative(23f)
                quadToRelative(0.208f, 0f, 0.354f, -0.146f)
                reflectiveQuadToRelative(0.146f, -0.354f)
                verticalLineTo(17f)
                horizontalLineTo(8f)
                verticalLineToRelative(16.125f)
                quadToRelative(0f, 0.208f, 0.146f, 0.354f)
                reflectiveQuadToRelative(0.354f, 0.146f)
                close()
                moveTo(8f, 15.042f)
                horizontalLineToRelative(24f)
                verticalLineTo(9.75f)
                quadToRelative(0f, -0.208f, -0.146f, -0.354f)
                reflectiveQuadTo(31.5f, 9.25f)
                horizontalLineToRelative(-23f)
                quadToRelative(-0.208f, 0f, -0.354f, 0.146f)
                reflectiveQuadTo(8f, 9.75f)
                close()
                moveToRelative(0f, 0f)
                verticalLineTo(9.25f)
                verticalLineToRelative(5.792f)
                close()
            }
        }.build()
    }
}