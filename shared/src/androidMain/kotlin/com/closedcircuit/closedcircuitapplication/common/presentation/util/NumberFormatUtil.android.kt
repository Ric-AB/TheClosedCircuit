package com.closedcircuit.closedcircuitapplication.common.presentation.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

actual fun formatNumberToCurrency(number: Double, symbol: String): String {
    val formatter: DecimalFormat = NumberFormat.getCurrencyInstance(Locale.US) as DecimalFormat
    val symbols: DecimalFormatSymbols = formatter.decimalFormatSymbols
    symbols.currencySymbol = ""
    formatter.decimalFormatSymbols = symbols
    formatter.maximumFractionDigits = 2
    return "$symbol ${formatter.format(number)}"
}