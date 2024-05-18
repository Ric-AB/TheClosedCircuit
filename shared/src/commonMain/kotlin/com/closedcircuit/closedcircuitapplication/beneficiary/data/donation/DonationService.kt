package com.closedcircuit.closedcircuitapplication.beneficiary.data.donation

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.beneficiary.data.donation.dto.RecentDonationsResponse
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints
import de.jensklingenberg.ktorfit.http.GET

interface DonationService {

    @GET(ClosedCircuitApiEndpoints.RECENT_DONATIONS)
    suspend fun fetchRecentDonations(): ApiResponse<RecentDonationsResponse>
}