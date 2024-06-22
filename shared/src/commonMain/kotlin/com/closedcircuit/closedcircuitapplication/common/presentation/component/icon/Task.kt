package com.closedcircuit.closedcircuitapplication.common.presentation.component.icon

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
fun rememberTask(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "task",
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
                moveTo(18.125f, 25.875f)
                lineTo(15f, 22.708f)
                quadToRelative(-0.208f, -0.208f, -0.417f, -0.291f)
                quadToRelative(-0.208f, -0.084f, -0.458f, -0.084f)
                reflectiveQuadToRelative(-0.479f, 0.105f)
                quadToRelative(-0.229f, 0.104f, -0.396f, 0.312f)
                quadToRelative(-0.417f, 0.375f, -0.417f, 0.875f)
                reflectiveQuadToRelative(0.417f, 0.875f)
                lineToRelative(3.958f, 3.958f)
                quadToRelative(0.209f, 0.25f, 0.438f, 0.334f)
                quadToRelative(0.229f, 0.083f, 0.479f, 0.083f)
                quadToRelative(0.292f, 0f, 0.521f, -0.083f)
                quadToRelative(0.229f, -0.084f, 0.437f, -0.334f)
                lineToRelative(7.5f, -7.5f)
                quadToRelative(0.417f, -0.375f, 0.417f, -0.896f)
                quadToRelative(0f, -0.52f, -0.417f, -0.895f)
                quadToRelative(-0.375f, -0.375f, -0.875f, -0.375f)
                reflectiveQuadToRelative(-0.875f, 0.375f)
                close()
                moveToRelative(-8.583f, 10.5f)
                quadToRelative(-1.042f, 0f, -1.834f, -0.771f)
                quadToRelative(-0.791f, -0.771f, -0.791f, -1.854f)
                verticalLineTo(6.25f)
                quadToRelative(0f, -1.083f, 0.791f, -1.854f)
                quadToRelative(0.792f, -0.771f, 1.834f, -0.771f)
                horizontalLineToRelative(13.291f)
                quadToRelative(0.542f, 0f, 1.021f, 0.208f)
                quadToRelative(0.479f, 0.209f, 0.854f, 0.542f)
                lineToRelative(7.584f, 7.583f)
                quadToRelative(0.375f, 0.375f, 0.583f, 0.875f)
                quadToRelative(0.208f, 0.5f, 0.208f, 1f)
                verticalLineTo(33.75f)
                quadToRelative(0f, 1.083f, -0.791f, 1.854f)
                quadToRelative(-0.792f, 0.771f, -1.834f, 0.771f)
                close()
                moveToRelative(13.083f, -23.708f)
                verticalLineTo(6.25f)
                horizontalLineTo(9.542f)
                verticalLineToRelative(27.5f)
                horizontalLineToRelative(20.916f)
                verticalLineTo(14f)
                horizontalLineToRelative(-6.5f)
                quadToRelative(-0.583f, 0f, -0.958f, -0.396f)
                reflectiveQuadToRelative(-0.375f, -0.937f)
                close()
                moveTo(9.542f, 6.25f)
                verticalLineTo(14f)
                verticalLineTo(6.25f)
                verticalLineToRelative(27.5f)
                verticalLineToRelative(-27.5f)
                close()
            }
        }.build()
    }
}