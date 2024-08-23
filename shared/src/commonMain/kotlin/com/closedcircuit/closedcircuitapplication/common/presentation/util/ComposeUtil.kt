package com.closedcircuit.closedcircuitapplication.common.presentation.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.closedcircuit.closedcircuitapplication.common.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.StringResource

inline fun Modifier.conditional(
    condition: Boolean,
    ifTrue: Modifier.() -> Modifier,
    ifFalse: Modifier.() -> Modifier = { this },
): Modifier = if (condition) {
    then(ifTrue(Modifier))
} else {
    then(ifFalse(Modifier))
}

inline fun Modifier.justPadding(all: Dp) =
    this then Modifier.background(Color.Unspecified).padding(all)

inline fun Modifier.justPadding(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) =
    this then Modifier.background(Color.Unspecified).padding(horizontal, vertical)

inline fun Modifier.justPadding(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp
) = this then Modifier.background(Color.Unspecified).padding(start, top, end, bottom)

@Composable
fun getEmptyStateText(loanStatus: LoanStatus): Pair<StringResource, StringResource> {
    return remember {
        when (loanStatus) {
            LoanStatus.PENDING -> {
                Pair(
                    SharedRes.strings.no_pending_loans_label,
                    SharedRes.strings.no_pending_loans_message
                )
            }

            LoanStatus.ACCEPTED -> {
                Pair(
                    SharedRes.strings.no_accepted_loans_label,
                    SharedRes.strings.no_accepted_loans_message
                )
            }

            LoanStatus.PAID -> {
                Pair(
                    SharedRes.strings.no_active_loans_label,
                    SharedRes.strings.no_active_loans_message
                )
            }

            LoanStatus.DECLINED -> {
                Pair(
                    SharedRes.strings.no_declined_loans_label,
                    SharedRes.strings.no_declined_loans_message
                )
            }

            LoanStatus.CANCELLED -> {
                Pair(
                    SharedRes.strings.no_cancelled_loans_label,
                    SharedRes.strings.no_cancelled_loans_message
                )
            }
        }
    }
}