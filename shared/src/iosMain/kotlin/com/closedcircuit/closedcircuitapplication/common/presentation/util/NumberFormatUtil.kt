package com.closedcircuit.closedcircuitapplication.common.presentation.util

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter

actual fun formatNumberToCurrency(number: Double, symbol: String): String {
    val formatter = NSNumberFormatter()
    formatter.currencySymbol = symbol
    return formatter.stringFromNumber(NSNumber(number)).orEmpty()
}