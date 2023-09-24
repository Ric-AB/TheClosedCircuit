package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.createplan

import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.getOrCreateScope
import org.koin.core.scope.Scope

class CreatePlanKoinContainer : KoinScopeComponent {
    override val scope: Scope
        get() = getOrCreateScope().value
}