package com.closedcircuit.closedcircuitapplication.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup

//import androidx.compose.ui.window.PopupProperties

@Composable
fun LoadingDialog(visible: Boolean = false) {
    AnimatedVisibility(visible, enter = fadeIn(), exit = fadeOut()) {
        Popup(
//        properties = PopupProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            alignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.White.copy(0.8f)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.size(100.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = Shapes().medium
                        ),
                    shape = Shapes().medium
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun CircularIndicator(
    size: Dp = 32.dp,
    sweepAngle: Float = 90f,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth
) {
    val transition = rememberInfiniteTransition()
    val currentArcStartAngle by transition.animateValue(
        initialValue = 0,
        targetValue = 360,
        Int.VectorConverter,
        infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = LinearEasing
            )
        )
    )

    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
    }

    Canvas(
        modifier = Modifier
            .progressSemantics()
            .size(size)
            .padding(strokeWidth / 2)
    ) {
        drawCircle(Color.LightGray, style = stroke)

        drawArc(
            color = color,
            startAngle = currentArcStartAngle.toFloat() - 90,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = stroke
        )
    }
}