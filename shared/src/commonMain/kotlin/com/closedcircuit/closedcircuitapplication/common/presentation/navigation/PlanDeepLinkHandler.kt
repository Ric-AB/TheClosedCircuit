package com.closedcircuit.closedcircuitapplication.common.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer.MakeOfferNavigator

@Composable
fun planDeepLinkHandler(planId: String?, navigator: Navigator) {
    remember(planId) {
        if (planId != null) {
            navigator.replaceAll(MakeOfferNavigator(planID = ID(planId)))
        }
    }
}