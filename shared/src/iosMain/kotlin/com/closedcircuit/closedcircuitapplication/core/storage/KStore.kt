package com.closedcircuit.closedcircuitapplication.core.storage

import com.closedcircuit.closedcircuitapplication.common.domain.app.AppSettings
import com.closedcircuit.closedcircuitapplication.common.domain.session.Session
import com.closedcircuit.closedcircuitapplication.common.domain.user.User
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import okio.Path.Companion.toPath

actual val appSettingsStore: KStore<AppSettings> by lazy {
    storeOf(Storage.appSettingsFile.toPath(), AppSettings())
}

actual val sessionStore: KStore<Session> by lazy {
    storeOf(Storage.sessionFile.toPath())
}

actual val userStore: KStore<User> by lazy {
    storeOf(Storage.userFile.toPath())
}