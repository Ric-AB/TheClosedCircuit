package com.closedcircuit.closedcircuitapplication.core.storage

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.app.AppSettings
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.session.Session
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.user.User
import io.github.xxfast.kstore.KStore

expect val appSettingsStore: KStore<AppSettings>
expect val sessionStore: KStore<Session>
expect val userStore: KStore<User>