package com.closedcircuit.closedcircuitapplication.domain.wallet

import com.closedcircuit.closedcircuitapplication.domain.model.Price

data class Wallet(
    val totalFunds: Price,
    val percentageIncrease: Int
)
