package com.closedcircuit.closedcircuitapplication.beneficiary.data.wallet

import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.wallet.Wallet
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.wallet.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class WalletRepositoryImpl : WalletRepository {

    private val _walletFlow = MutableStateFlow(
        Wallet(
            totalFunds = Amount(4000.0),
            percentageIncrease = 10
        )
    )
    override val walletFlow: Flow<Wallet>
        get() = _walletFlow

    override suspend fun getUserWallet(): Wallet {
        return Wallet(
            totalFunds = Amount(4000.0),
            percentageIncrease = 10
        )
    }
}