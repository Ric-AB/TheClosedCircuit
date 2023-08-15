package com.closedcircuit.closedcircuitapplication.core.storage

import com.closedcircuit.closedcircuitapplication.domain.app.AppSettings
import com.closedcircuit.closedcircuitapplication.domain.session.Session
import io.github.xxfast.kstore.KStore

expect val appSettingsStore: KStore<AppSettings>
expect val sessionStore: KStore<Session>