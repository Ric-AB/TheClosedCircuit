package com.closedcircuit.closedcircuitapplication.data.datasource.local

import com.closedcircuit.closedcircuitapplication.domain.app.AppSettings
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf

var appSettingsStorage = ""
actual val appSettingsStore: KStore<AppSettings> by lazy {
    storeOf("${appSettingsStorage}/app_settings.json", AppSettings())
}