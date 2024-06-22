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
fun rememberArrowLeftAlt(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "arrow_left_alt",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
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
                moveTo(285.999f, -448.5f)
                lineToRelative(137.334f, 137.334f)
                quadToRelative(9.334f, 9.333f, 9.167f, 22.166f)
                quadToRelative(-0.167f, 12.834f, -9.333f, 22.334f)
                quadToRelative(-9.834f, 9.499f, -22.667f, 9.583f)
                quadToRelative(-12.833f, 0.083f, -22.333f, -9.75f)
                lineToRelative(-191.001f, -191f)
                quadToRelative(-9.833f, -9.5f, -9.833f, -22.167f)
                quadToRelative(0f, -12.667f, 9.833f, -22.5f)
                lineTo(379f, -694.334f)
                quadToRelative(9.5f, -9.499f, 22.25f, -9.333f)
                quadTo(414f, -703.5f, 423.833f, -694f)
                quadToRelative(9.167f, 9.5f, 9f, 22.666f)
                quadToRelative(-0.166f, 13.167f, -9.5f, 22.167f)
                lineTo(285.999f, -511.833f)
                horizontalLineTo(764.5f)
                quadToRelative(13.167f, 0f, 22.333f, 9.166f)
                quadTo(796f, -493.5f, 796f, -480f)
                quadToRelative(0f, 13.5f, -9.167f, 22.5f)
                quadToRelative(-9.166f, 9f, -22.333f, 9f)
                horizontalLineTo(285.999f)
                close()
            }
        }.build()
    }
}