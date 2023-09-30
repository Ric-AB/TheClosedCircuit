package com.closedcircuit.closedcircuitapplication.data.donation

import com.closedcircuit.closedcircuitapplication.domain.donation.Donation
import com.closedcircuit.closedcircuitapplication.domain.donation.DonationRepository
import com.closedcircuit.closedcircuitapplication.domain.donation.Donations
import com.closedcircuit.closedcircuitapplication.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Name
import com.closedcircuit.closedcircuitapplication.domain.model.Price
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DonationRepositoryImpl : DonationRepository {

    private val _donationsFlow = MutableStateFlow(donations)
    override val recentDonationsFlow: Flow<Donations>
        get() = _donationsFlow.asStateFlow()

    override suspend fun getRecentDonations(): ImmutableList<Donation> {
        return donations.take(5).toImmutableList()
    }
}

private val donations = List(10) {
    Donation(
        id = ID("0d410f4e-4cd6-11ee-be56-0242ac120002"),
        sponsorAvatar = Avatar(""),
        sponsorFullName = Name("Walter White"),
        planName = "Drugs",
        amount = Price(5000.0)
    )
}.toImmutableList()