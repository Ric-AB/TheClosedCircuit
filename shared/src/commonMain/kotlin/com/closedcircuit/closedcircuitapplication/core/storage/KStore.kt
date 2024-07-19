package com.closedcircuit.closedcircuitapplication.core.storage

import com.closedcircuit.closedcircuitapplication.common.domain.app.AppSettings
import com.closedcircuit.closedcircuitapplication.common.domain.session.Session
import com.closedcircuit.closedcircuitapplication.common.domain.user.User
import io.github.xxfast.kstore.KStore

expect val appSettingsStore: KStore<AppSettings>
expect val sessionStore: KStore<Session>
expect val userStore: KStore<User>