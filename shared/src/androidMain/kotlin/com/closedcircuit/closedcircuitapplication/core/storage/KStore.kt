package com.closedcircuit.closedcircuitapplication.core.storage

import com.closedcircuit.closedcircuitapplication.domain.app.AppSettings
import com.closedcircuit.closedcircuitapplication.domain.session.Session
import com.closedcircuit.closedcircuitapplication.domain.user.User
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf

actual val appSettingsStore: KStore<AppSettings> by lazy {
    storeOf(Storage.appSettingsFile, AppSettings())
}
actual val sessionStore: KStore<Session> by lazy {
    storeOf(Storage.sessionFile)
}

actual val userStore: KStore<User> by lazy {
    storeOf(Storage.userFile)
}