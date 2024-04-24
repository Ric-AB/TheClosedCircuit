package com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.passwordrecovery

import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.getOrCreateScope
import org.koin.core.scope.Scope

class ResetPasswordKoinContainer: KoinScopeComponent {
    override val scope: Scope
        get() = getOrCreateScope().value
}