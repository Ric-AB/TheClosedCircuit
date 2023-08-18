package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.password_recovery

import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.getOrCreateScope
import org.koin.core.scope.Scope

class ResetPasswordKoinContainer: KoinScopeComponent {
    override val scope: Scope
        get() = getOrCreateScope().value
}