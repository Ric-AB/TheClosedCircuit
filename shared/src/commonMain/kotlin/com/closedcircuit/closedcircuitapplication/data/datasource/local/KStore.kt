package com.closedcircuit.closedcircuitapplication.data.datasource.local

import com.closedcircuit.closedcircuitapplication.domain.app.AppSettings
import io.github.xxfast.kstore.KStore

expect val appSettingsStore: KStore<AppSettings>