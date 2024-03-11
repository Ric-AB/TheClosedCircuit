package com.closedcircuit.closedcircuitapplication.domain.wallet

import com.closedcircuit.closedcircuitapplication.domain.model.Amount

data class Wallet(
    val totalFunds: Amount,
    val percentageIncrease: Int
)
