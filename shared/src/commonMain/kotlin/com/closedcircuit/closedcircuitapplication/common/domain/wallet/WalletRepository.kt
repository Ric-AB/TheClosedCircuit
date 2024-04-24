package com.closedcircuit.closedcircuitapplication.beneficiary.domain.wallet

import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    val walletFlow: Flow<Wallet>
    suspend fun getUserWallet(): Wallet
}