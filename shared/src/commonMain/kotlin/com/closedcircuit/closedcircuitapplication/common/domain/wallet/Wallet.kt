package com.closedcircuit.closedcircuitapplication.beneficiary.domain.wallet

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Amount

data class Wallet(
    val totalFunds: Amount,
    val percentageIncrease: Int
)
