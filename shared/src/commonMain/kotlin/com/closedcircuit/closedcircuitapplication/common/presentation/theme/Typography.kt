package com.closedcircuit.closedcircuitapplication.common.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.sp
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.fontFamilyResource


@Composable
fun getTypography(): Typography {
    val helvetica = fontFamilyResource(SharedRes.fonts.helvetica.helvetica)
    return Typography(
        headlineLarge = TextStyle(
            fontFamily = helvetica,
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = helvetica,
            fontWeight = FontWeight.Medium,
            fontSize = 28.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = helvetica,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            letterSpacing = 0.15.sp
        ),
        titleLarge = TextStyle(
            fontFamily = helvetica,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp
        ),
        titleMedium = TextStyle(
            fontFamily = helvetica,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        ),
        titleSmall = TextStyle(
            fontFamily = helvetica,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = helvetica,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = helvetica,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            baselineShift = BaselineShift(+0.3f),
        ),
        bodySmall = TextStyle(
            fontFamily = helvetica,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
        ),
        labelLarge = TextStyle(
            fontFamily = helvetica,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        ),
        labelMedium = TextStyle(
            fontFamily = helvetica,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        ),
        labelSmall = TextStyle(
            fontFamily = helvetica,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            letterSpacing = 1.5.sp
        )
    )
}
